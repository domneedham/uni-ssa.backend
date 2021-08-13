package com.example.ssa.web.api;

import com.example.ssa.entity.skill.Category;
import com.example.ssa.entity.skill.Skill;
import com.example.ssa.entity.skill.StaffSkill;
import com.example.ssa.entity.user.AppUser;
import com.example.ssa.entity.user.UserRole;
import com.example.ssa.exceptions.requests.bad.StaffSkillDoesNotExistException;
import com.example.ssa.repository.StaffSkillRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StaffSkillController.class)
public class StaffSkillControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    StaffSkillRepository staffSkillRepository;

    Category categoryOne = new Category(1L, "Category One", 57718);
    Category categoryTwo = new Category(2L, "Category Two", 57718);

    Skill skillOne = new Skill(1L, "Skill One", categoryOne);
    Skill skillTwo = new Skill(2L, "Skill Two", categoryTwo);

    AppUser appUserOne = new AppUser(1L, "Test", "User", "test@user.com", "password", UserRole.STAFF, "Test User");
    AppUser appUserTwo = new AppUser(2L, "Test", "Staff", "test@staff.com", "password", UserRole.STAFF, "Test Staff");

    StaffSkill staffSkillOne = new StaffSkill(1L, skillOne, appUserOne, 5, LocalDateTime.now(), LocalDateTime.now().plusDays(30));
    StaffSkill staffSkillTwo = new StaffSkill(1L, skillTwo, appUserTwo, 3, LocalDateTime.now(), null);

    @Test
    public void findAllStaffSkills_success() throws Exception {
        List<StaffSkill> records = new ArrayList<>(Arrays.asList(staffSkillOne, staffSkillTwo));

        when(staffSkillRepository.findAll()).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/skill/staff/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].skill.name", is("Skill One")))
                .andExpect(jsonPath("$[0].staffDetails.firstname", is("Test")));
    }

    @Test
    public void findById_success() throws Exception {
        when(staffSkillRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(staffSkillOne));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/skill/staff/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.skill.name", is("Skill One")))
                .andExpect(jsonPath("$.staffDetails.firstname", is("Test")));
    }

    @Test
    public void findById_notFound() throws Exception {
        when(staffSkillRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/skill/staff/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof StaffSkillDoesNotExistException))
                .andExpect(result -> assertEquals("Staff skill not found with that id",
                        Objects.requireNonNull(result.getResolvedException()).getMessage())
                );
    }

    @Test
    public void findBySkillIdAndStaffId_success() throws Exception {
        when(staffSkillRepository.findBySkillIdAndStaffDetailsId(1L, 1L)).thenReturn(java.util.Optional.ofNullable(staffSkillOne));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/skill/staff/1/sid/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.skill.name", is("Skill One")))
                .andExpect(jsonPath("$.staffDetails.firstname", is("Test")));
    }

    @Test
    public void findBySkillIdAndStaffId_notFound() throws Exception {
        when(staffSkillRepository.findBySkillIdAndStaffDetailsId(1L, 1L)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/skill/staff/1/sid/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof StaffSkillDoesNotExistException))
                .andExpect(result -> assertEquals("Staff skill not found with that id",
                        Objects.requireNonNull(result.getResolvedException()).getMessage())
                );
    }

    @Test
    public void findAllStaffSkillsByStaffId_success() throws Exception {
        List<StaffSkill> records = new ArrayList<>(List.of(staffSkillOne));

        when(staffSkillRepository.findByStaffDetailsId(1L)).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/skill/staff/sid/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].skill.name", is("Skill One")))
                .andExpect(jsonPath("$[0].staffDetails.firstname", is("Test")));
    }

//    @Test
//    public void assignStaffSkill_success() throws Exception {
//        StaffSkill skill = StaffSkill.builder()
//                .skill(skillOne)
//                .staffDetails(appUserOne)
//                .rating(5)
//                .lastUpdated(LocalDateTime.now())
//                .expires(LocalDateTime.now().plusDays(30))
//                .build();
//
//        when(staffSkillRepository.save(skill)).thenReturn(skill);
//
//        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/skill/staff/assign")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(this.mapper.writeValueAsString(skill));
//
//        mockMvc.perform(mockRequest)
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", notNullValue()))
//                .andExpect(jsonPath("$.skill.name", is("Skill One")))
//                .andExpect(jsonPath("$.staffDetails.firstname", is("Test")));
//    }
//
//    @Test
//    public void updateStaffSkill_success() throws Exception {
//        StaffSkill skill = StaffSkill.builder()
//                .id(1L)
//                .skill(skillOne)
//                .staffDetails(appUserOne)
//                .rating(5)
//                .lastUpdated(LocalDateTime.now())
//                .expires(LocalDateTime.now().plusDays(30))
//                .build();
//
//        when(staffSkillRepository.save(skill)).thenReturn(skill);
//        when(staffSkillRepository.findBySkillIdAndStaffDetailsId(1L, 1L)).thenReturn(java.util.Optional.of(skill));
//
//        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/api/skill/update")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(this.mapper.writeValueAsString(skill));
//
//        mockMvc.perform(mockRequest)
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", notNullValue()))
//                .andExpect(jsonPath("$.skill.name", is("Skill One")))
//                .andExpect(jsonPath("$.staffDetails.firstname", is("Test")));
//    }
}
