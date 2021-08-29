package com.example.ssa.web.api;

import com.example.ssa.entity.skill.Category;
import com.example.ssa.entity.skill.Skill;
import com.example.ssa.entity.skill.StaffSkill;
import com.example.ssa.entity.user.AppUser;
import com.example.ssa.entity.user.UserRole;
import com.example.ssa.exceptions.requests.bad.StaffSkillDoesNotExistException;
import com.example.ssa.service.StaffSkillService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.*;
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
    UserDetailsService userDetailsService;
    @MockBean
    StaffSkillService staffSkillService;

    final Category categoryOne = new Category(1L, "Category One", 57718);
    final Category categoryTwo = new Category(2L, "Category Two", 57718);

    final Skill skillOne = new Skill(1L, "Skill One", categoryOne);
    final Skill skillTwo = new Skill(2L, "Skill Two", categoryTwo);

    final AppUser appUserOne = new AppUser(1L, "Test", "User", "test@user.com", "password", UserRole.STAFF, "Test User");
    final AppUser appUserTwo = new AppUser(2L, "Test", "Staff", "test@staff.com", "password", UserRole.STAFF, "Test Staff");

    final StaffSkill staffSkillOne = new StaffSkill(1L, skillOne, appUserOne, 5, LocalDateTime.now(), LocalDateTime.now().plusDays(30));
    final StaffSkill staffSkillTwo = new StaffSkill(1L, skillTwo, appUserTwo, 3, LocalDateTime.now(), null);

    @WithMockUser(authorities = "MANAGER")
    @Test
    public void findAllStaffSkills_success_manager() throws Exception {
        List<StaffSkill> records = new ArrayList<>(List.of(staffSkillOne, staffSkillTwo));

        when(staffSkillService.findAllStaffSkills()).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/skill/staff/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].skill.name", is("Skill One")))
                .andExpect(jsonPath("$[0].staffDetails.firstname", is("Test")));
    }

    @WithMockUser(authorities = "STAFF")
    @Test
    public void findAllStaffSkills_success_staff() throws Exception {
        List<StaffSkill> records = new ArrayList<>(List.of(staffSkillOne, staffSkillTwo));

        when(staffSkillService.findAllStaffSkills()).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/skill/staff/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].skill.name", is("Skill One")))
                .andExpect(jsonPath("$[0].staffDetails.firstname", is("Test")));
    }

    @WithMockUser(authorities = "MANAGER")
    @Test
    public void findAllStaffSkills_empty() throws Exception {
        List<StaffSkill> records = new ArrayList<>(List.of());

        when(staffSkillService.findAllStaffSkills()).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/skill/staff/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @WithMockUser(authorities = "MANAGER")
    @Test
    public void findById_success_manager() throws Exception {
        when(staffSkillService.findStaffSkillById(1L)).thenReturn(staffSkillOne);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/skill/staff/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.skill.name", is("Skill One")))
                .andExpect(jsonPath("$.staffDetails.firstname", is("Test")));
    }

    @WithMockUser(authorities = "STAFF")
    @Test
    public void findById_success_staff() throws Exception {
        when(staffSkillService.findStaffSkillById(1L)).thenReturn(staffSkillOne);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/skill/staff/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.skill.name", is("Skill One")))
                .andExpect(jsonPath("$.staffDetails.firstname", is("Test")));
    }

    @WithMockUser(authorities = "MANAGER")
    @Test
    public void findById_notFound() throws Exception {
        when(staffSkillService.findStaffSkillById(1L)).thenThrow(new StaffSkillDoesNotExistException("Staff skill not found with that id"));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/skill/staff/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof StaffSkillDoesNotExistException))
                .andExpect(result -> assertEquals("Staff skill not found with that id",
                        Objects.requireNonNull(result.getResolvedException()).getMessage())
                );
    }

    @WithMockUser(authorities = "MANAGER")
    @Test
    public void findBySkillIdAndStaffId_success_manager() throws Exception {
        when(staffSkillService.findStaffSkillBySkillIdAndStaffId(1L, 1L)).thenReturn(staffSkillOne);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/skill/staff/1/sid/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.skill.name", is("Skill One")))
                .andExpect(jsonPath("$.staffDetails.firstname", is("Test")));
    }

    @WithMockUser(authorities = "STAFF")
    @Test
    public void findBySkillIdAndStaffId_success_staff() throws Exception {
        when(staffSkillService.findStaffSkillBySkillIdAndStaffId(1L, 1L)).thenReturn(staffSkillOne);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/skill/staff/1/sid/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.skill.name", is("Skill One")))
                .andExpect(jsonPath("$.staffDetails.firstname", is("Test")));
    }

    @WithMockUser(authorities = "MANAGER")
    @Test
    public void findBySkillIdAndStaffId_notFound() throws Exception {
        when(staffSkillService.findStaffSkillBySkillIdAndStaffId(1L, 1L)).thenThrow(new StaffSkillDoesNotExistException("Staff skill not found with that id"));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/skill/staff/1/sid/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof StaffSkillDoesNotExistException))
                .andExpect(result -> assertEquals("Staff skill not found with that id",
                        Objects.requireNonNull(result.getResolvedException()).getMessage())
                );
    }

    @WithMockUser(authorities = "MANAGER")
    @Test
    public void findAllStaffSkillsByStaffId_success_manager() throws Exception {
        List<StaffSkill> records = new ArrayList<>(List.of(staffSkillOne));

        when(staffSkillService.findAllStaffSkillsByStaffId(1L)).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/skill/staff/sid/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].skill.name", is("Skill One")))
                .andExpect(jsonPath("$[0].staffDetails.firstname", is("Test")));
    }

    @WithMockUser(authorities = "STAFF")
    @Test
    public void findAllStaffSkillsByStaffId_success_staff() throws Exception {
        List<StaffSkill> records = new ArrayList<>(List.of(staffSkillOne));

        when(staffSkillService.findAllStaffSkillsByStaffId(1L)).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/skill/staff/sid/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].skill.name", is("Skill One")))
                .andExpect(jsonPath("$[0].staffDetails.firstname", is("Test")));
    }

    @WithMockUser(authorities = "MANAGER")
    @Test
    public void assignStaffSkill_success_manager() throws Exception {
        StaffSkill skill = StaffSkill.builder()
                .skill(skillOne)
                .staffDetails(appUserOne)
                .rating(5)
                .lastUpdated(LocalDateTime.now())
                .expires(LocalDateTime.now().plusDays(30))
                .build();

        when(staffSkillService.assignStaffSkill(ArgumentMatchers.any())).thenReturn(skill);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/skill/staff/assign")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(skill));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.skill.name", is("Skill One")))
                .andExpect(jsonPath("$.staffDetails.firstname", is("Test")));
    }

    @WithMockUser(authorities = "STAFF")
    @Test
    public void assignStaffSkill_success_staff() throws Exception {
        StaffSkill skill = StaffSkill.builder()
                .skill(skillOne)
                .staffDetails(appUserOne)
                .rating(5)
                .lastUpdated(LocalDateTime.now())
                .expires(LocalDateTime.now().plusDays(30))
                .build();

        when(staffSkillService.assignStaffSkill(ArgumentMatchers.any())).thenReturn(skill);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/skill/staff/assign")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(skill));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.skill.name", is("Skill One")))
                .andExpect(jsonPath("$.staffDetails.firstname", is("Test")));
    }

    @WithMockUser(authorities = "MANAGER")
    @Test
    public void updateStaffSkill_success_manager() throws Exception {
        StaffSkill skill = StaffSkill.builder()
                .id(1L)
                .skill(skillOne)
                .staffDetails(appUserOne)
                .rating(5)
                .lastUpdated(LocalDateTime.now())
                .expires(LocalDateTime.now().plusDays(30))
                .build();

        when(staffSkillService.updateStaffSkill(ArgumentMatchers.any())).thenReturn(skill);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/api/skill/staff/update")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(skill));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.skill.name", is("Skill One")))
                .andExpect(jsonPath("$.staffDetails.firstname", is("Test")));
    }

    @WithMockUser(authorities = "STAFF")
    @Test
    public void updateStaffSkill_success_staff() throws Exception {
        StaffSkill skill = StaffSkill.builder()
                .id(1L)
                .skill(skillOne)
                .staffDetails(appUserOne)
                .rating(5)
                .lastUpdated(LocalDateTime.now())
                .expires(LocalDateTime.now().plusDays(30))
                .build();

        when(staffSkillService.updateStaffSkill(ArgumentMatchers.any())).thenReturn(skill);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/api/skill/staff/update")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(skill));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.skill.name", is("Skill One")))
                .andExpect(jsonPath("$.staffDetails.firstname", is("Test")));
    }
}
