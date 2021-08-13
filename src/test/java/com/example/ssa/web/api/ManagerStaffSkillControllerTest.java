package com.example.ssa.web.api;

import com.example.ssa.entity.skill.Category;
import com.example.ssa.entity.skill.ManagerStaffSkill;
import com.example.ssa.entity.skill.Skill;
import com.example.ssa.entity.user.AppUser;
import com.example.ssa.entity.user.UserRole;
import com.example.ssa.exceptions.requests.bad.ManagerStaffSkillDoesNotExistException;
import com.example.ssa.repository.ManagerStaffSkillRepository;
import com.example.ssa.service.ManagerStaffSkillService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ManagerStaffSkillController.class)
public class ManagerStaffSkillControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    ManagerStaffSkillService managerStaffSkillService;

    AppUser appUserOne = new AppUser(1L, "Test", "User", "test@user.com", "password", UserRole.STAFF, "Test User");
    AppUser appUserTwo = new AppUser(2L, "Test", "Two", "test@two.com", "password", UserRole.STAFF, "Test Two");

    Category category = new Category(1L, "Category One", 26530);
    Skill skillOne = new Skill(1L, "Skill One", category);
    Skill skillTwo = new Skill(2L, "Skill Two", category);

    ManagerStaffSkill managerStaffSkillOne = new ManagerStaffSkill(1L, skillOne, List.of(appUserOne));
    ManagerStaffSkill managerStaffSkillTwo = new ManagerStaffSkill(2L, skillTwo, List.of(appUserOne, appUserTwo));

    @Test
    public void findAllManagerStaffSkills_success() throws Exception {
        List<ManagerStaffSkill> records = new ArrayList<>(List.of(managerStaffSkillOne, managerStaffSkillTwo));

        when(managerStaffSkillService.findAllManagerStaffSkills()).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/skill/manager/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].skill.name", is(skillOne.getName())));
    }

    @Test
    public void findAllManagerStaffSkills_empty() throws Exception {
        List<ManagerStaffSkill> records = new ArrayList<>(List.of());

        when(managerStaffSkillService.findAllManagerStaffSkills()).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/skill/manager/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void findById_success() throws Exception {
        when(managerStaffSkillService.findManagerStaffSkillById(1L)).thenReturn(java.util.Optional.ofNullable(managerStaffSkillOne));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/skill/manager/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.skill.name", is(skillOne.getName())));
    }

    @Test
    public void findById_notFound() throws Exception {
        when(managerStaffSkillService.findManagerStaffSkillById(1L)).thenThrow(new ManagerStaffSkillDoesNotExistException("Skill not found with that id"));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/skill/manager/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ManagerStaffSkillDoesNotExistException))
                .andExpect(result -> assertEquals("Skill not found with that id",
                        Objects.requireNonNull(result.getResolvedException()).getMessage())
                );
    }
}
