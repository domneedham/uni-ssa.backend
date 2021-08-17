package com.example.ssa.web.api;

import com.example.ssa.entity.skill.Category;
import com.example.ssa.entity.skill.Skill;
import com.example.ssa.entity.skill.StaffSkill;
import com.example.ssa.entity.user.AppUser;
import com.example.ssa.entity.user.Staff;
import com.example.ssa.entity.user.UserRole;
import com.example.ssa.exceptions.requests.bad.ManagerDoesNotExistException;
import com.example.ssa.exceptions.requests.bad.StaffDoesNotExistException;
import com.example.ssa.service.StaffService;
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

@WebMvcTest(StaffController.class)
public class StaffControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserDetailsService userDetailsService;
    @MockBean
    StaffService staffService;

    AppUser appUserManager = new AppUser(3L, "Test", "User", "test@user.com", "password", UserRole.MANAGER, "Test User");
    AppUser appUserStaffOne = new AppUser(1L, "Test", "User", "test@user.com","password", UserRole.STAFF, "Test User");
    AppUser appUserStaffTwo = new AppUser(2L, "Test", "Staff", "test@staff.com", "password", UserRole.STAFF, "Test Staff");

    Category categoryOne = new Category(1L, "Category One", 57718);
    Category categoryTwo = new Category(2L, "Category Two", 57718);

    Skill skillOne = new Skill(1L, "Skill One", categoryOne);
    Skill skillTwo = new Skill(2L, "Skill Two", categoryTwo);

    StaffSkill staffSkillOneOne = new StaffSkill(1L, skillOne, appUserStaffOne, 5, LocalDateTime.now(), LocalDateTime.now().plusDays(30));
    StaffSkill staffSkillOneTwo = new StaffSkill(2L, skillTwo, appUserStaffOne, 5, LocalDateTime.now(), LocalDateTime.now().plusDays(30));
    StaffSkill staffSkillTwoOne = new StaffSkill(1L, skillOne, appUserStaffOne, 5, LocalDateTime.now(), LocalDateTime.now().plusDays(30));
    StaffSkill staffSkillTwoTwo = new StaffSkill(2L, skillTwo, appUserStaffOne, 5, LocalDateTime.now(), LocalDateTime.now().plusDays(30));
    List<StaffSkill> staffSkillsOne = new ArrayList<>(List.of(staffSkillOneOne, staffSkillOneTwo));
    List<StaffSkill> staffSkillsTwo = new ArrayList<>(List.of(staffSkillTwoOne, staffSkillTwoTwo));
    List<StaffSkill> staffSkillsEmpty = new ArrayList<>();

    Staff staffOne = new Staff(1L, appUserStaffOne, appUserManager, staffSkillsOne);
    Staff staffTwo = new Staff(2L, appUserStaffTwo, appUserManager, staffSkillsTwo);

    @WithMockUser(roles = "MANAGER")
    @Test
    public void findAllStaff_success() throws Exception {
        List<Staff> records = new ArrayList<>(List.of(staffOne, staffTwo));

        when(staffService.findAllStaff()).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/staff/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userDetails.surname", is(staffOne.getUserDetails().getSurname())))
                .andExpect(jsonPath("$[1].userDetails.surname", is(staffTwo.getUserDetails().getSurname())));
    }

    @WithMockUser(roles = "MANAGER")
    @Test
    public void findById_success() throws Exception {
        when(staffService.findStaffById(1L)).thenReturn(java.util.Optional.ofNullable(staffOne));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/staff/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.userDetails.surname", is(staffOne.getUserDetails().getSurname())));
    }

    @WithMockUser(roles = "MANAGER")
    @Test
    public void findById_notFound() throws Exception {
        when(staffService.findStaffById(1L)).thenThrow(new StaffDoesNotExistException("Staff not found with that id"));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/staff/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof StaffDoesNotExistException))
                .andExpect(result -> assertEquals("Staff not found with that id",
                        Objects.requireNonNull(result.getResolvedException()).getMessage())
                );
    }

    @WithMockUser(roles = "MANAGER")
    @Test
    public void findByName_success() throws Exception {
        List<Staff> records = new ArrayList<>(List.of(staffOne, staffTwo));
        when(staffService.findStaffByName("test")).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/staff/search/test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userDetails.surname", is(staffOne.getUserDetails().getSurname())))
                .andExpect(jsonPath("$[1].userDetails.surname", is(staffTwo.getUserDetails().getSurname())));
    }

    @WithMockUser(roles = "MANAGER")
    @Test
    public void findByName_noMatch() throws Exception {
        List<Staff> records = new ArrayList<>(List.of());
        when(staffService.findStaffByName("test")).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/staff/search/test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @WithMockUser(roles = "MANAGER")
    @Test
    public void createStaff_success() throws Exception {
        Staff staff = Staff.builder()
                .userDetails(appUserStaffOne)
                .managerDetails(appUserManager)
                .skills(staffSkillsEmpty)
                .build();

        when(staffService.createStaff(ArgumentMatchers.any())).thenReturn(staff);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/staff/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(staff));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.userDetails.surname", is(staffOne.getUserDetails().getSurname())));
    }

    @WithMockUser(roles = "MANAGER")
    @Test
    public void createStaff_noManager() throws Exception {
        Staff staff = Staff.builder()
                .userDetails(appUserStaffOne)
                .managerDetails(appUserManager)
                .skills(staffSkillsEmpty)
                .build();

        when(staffService.createStaff(ArgumentMatchers.any())).thenThrow(new ManagerDoesNotExistException("Manager not found with that id"));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/staff/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(staff));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ManagerDoesNotExistException))
                .andExpect(result -> assertEquals("Manager not found with that id",
                        Objects.requireNonNull(result.getResolvedException()).getMessage())
                );
    }

    @WithMockUser(roles = "MANAGER")
    @Test
    public void updateStaff_success() throws Exception {
        Staff staff = Staff.builder()
                .id(1L)
                .userDetails(appUserStaffTwo)
                .managerDetails(appUserManager)
                .skills(staffSkillsEmpty)
                .build();

        when(staffService.updateStaff(ArgumentMatchers.any())).thenReturn(staff);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/api/staff/update")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(staff));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.userDetails.surname", is(appUserStaffTwo.getSurname())));
    }

    @WithMockUser(roles = "MANAGER")
    @Test
    public void updateStaff_noId() throws Exception {
        Staff staff = Staff.builder()
                .userDetails(appUserStaffTwo)
                .managerDetails(appUserManager)
                .skills(staffSkillsEmpty)
                .build();

        when(staffService.updateStaff(ArgumentMatchers.any())).thenThrow(new StaffDoesNotExistException("Staff not found with that id"));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/api/staff/update")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(staff));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof StaffDoesNotExistException))
                .andExpect(result -> assertEquals("Staff not found with that id",
                        Objects.requireNonNull(result.getResolvedException()).getMessage())
                );
    }
}
