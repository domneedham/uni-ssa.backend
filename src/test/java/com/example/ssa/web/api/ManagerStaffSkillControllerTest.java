package com.example.ssa.web.api;

import com.example.ssa.entity.skill.Category;
import com.example.ssa.entity.skill.ManagerStaffSkill;
import com.example.ssa.entity.skill.Skill;
import com.example.ssa.entity.user.AppUser;
import com.example.ssa.entity.user.UserRole;
import com.example.ssa.exceptions.requests.bad.ManagerStaffSkillDoesNotExistException;
import com.example.ssa.service.ManagerStaffSkillService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
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
    UserDetailsService userDetailsService;
    @MockBean
    ManagerStaffSkillService managerStaffSkillService;

    final AppUser appUserOne = new AppUser(1L, "Test", "User", "test@user.com", "password", UserRole.STAFF, "Test User");
    final AppUser appUserTwo = new AppUser(2L, "Test", "Two", "test@two.com", "password", UserRole.STAFF, "Test Two");

    final Category category = new Category(1L, "Category One", 26530);
    final Skill skillOne = new Skill(1L, "Skill One", category);
    final Skill skillTwo = new Skill(2L, "Skill Two", category);

    final ManagerStaffSkill managerStaffSkillOne = new ManagerStaffSkill(1L, skillOne, List.of(appUserOne));
    final ManagerStaffSkill managerStaffSkillTwo = new ManagerStaffSkill(2L, skillTwo, List.of(appUserOne, appUserTwo));

    @WithMockUser(authorities = "MANAGER")
    @Test
    public void findAllManagerStaffSkills_success_manager() throws Exception {
        List<ManagerStaffSkill> records = new ArrayList<>(List.of(managerStaffSkillOne, managerStaffSkillTwo));

        when(managerStaffSkillService.findAllManagerStaffSkills()).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/skill/manager/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].skill.name", is(skillOne.getName())));
    }

    @WithMockUser(authorities = "STAFF")
    @Test
    public void findAllManagerStaffSkills_forbidden_staff() throws Exception {
        List<ManagerStaffSkill> records = new ArrayList<>(List.of(managerStaffSkillOne, managerStaffSkillTwo));

        when(managerStaffSkillService.findAllManagerStaffSkills()).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/skill/manager/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    @WithMockUser(authorities = "MANAGER")
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

    @WithMockUser(authorities = "MANAGER")
    @Test
    public void findById_success_manager() throws Exception {
        when(managerStaffSkillService.findManagerStaffSkillById(1L)).thenReturn(managerStaffSkillOne);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/skill/manager/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.skill.name", is(skillOne.getName())));
    }

    @WithMockUser(authorities = "STAFF")
    @Test
    public void findById_forbidden_staff() throws Exception {
        when(managerStaffSkillService.findManagerStaffSkillById(1L)).thenReturn(managerStaffSkillOne);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/skill/manager/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(authorities = "MANAGER")
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
