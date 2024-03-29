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

    final AppUser appUserManager = new AppUser(3L, "Test", "User", "test@user.com", "password", UserRole.MANAGER, "Test User");
    final AppUser appUserStaffOne = new AppUser(1L, "Test", "User", "test@user.com","password", UserRole.STAFF, "Test User");
    final AppUser appUserStaffTwo = new AppUser(2L, "Test", "Staff", "test@staff.com", "password", UserRole.STAFF, "Test Staff");

    final Category categoryOne = new Category(1L, "Category One", 57718);
    final Category categoryTwo = new Category(2L, "Category Two", 57718);

    final Skill skillOne = new Skill(1L, "Skill One", categoryOne);
    final Skill skillTwo = new Skill(2L, "Skill Two", categoryTwo);

    final StaffSkill staffSkillOneOne = new StaffSkill(1L, skillOne, appUserStaffOne, 5, LocalDateTime.now(), LocalDateTime.now().plusDays(30));
    final StaffSkill staffSkillOneTwo = new StaffSkill(2L, skillTwo, appUserStaffOne, 5, LocalDateTime.now(), LocalDateTime.now().plusDays(30));
    final StaffSkill staffSkillTwoOne = new StaffSkill(1L, skillOne, appUserStaffOne, 5, LocalDateTime.now(), LocalDateTime.now().plusDays(30));
    final StaffSkill staffSkillTwoTwo = new StaffSkill(2L, skillTwo, appUserStaffOne, 5, LocalDateTime.now(), LocalDateTime.now().plusDays(30));
    final List<StaffSkill> staffSkillsOne = new ArrayList<>(List.of(staffSkillOneOne, staffSkillOneTwo));
    final List<StaffSkill> staffSkillsTwo = new ArrayList<>(List.of(staffSkillTwoOne, staffSkillTwoTwo));
    final List<StaffSkill> staffSkillsEmpty = new ArrayList<>();

    final Staff staffOne = new Staff(1L, appUserStaffOne, appUserManager, staffSkillsOne);
    final Staff staffTwo = new Staff(2L, appUserStaffTwo, appUserManager, staffSkillsTwo);

    @WithMockUser(authorities = "MANAGER")
    @Test
    public void findAllStaff_success_manager() throws Exception {
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

    @WithMockUser(authorities = "STAFF")
    @Test
    public void findAllStaff_success_staff() throws Exception {
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

    @WithMockUser(authorities = "MANAGER")
    @Test
    public void findById_success_manager() throws Exception {
        when(staffService.findStaffById(1L)).thenReturn(staffOne);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/staff/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.userDetails.surname", is(staffOne.getUserDetails().getSurname())));
    }

    @WithMockUser(authorities = "STAFF")
    @Test
    public void findById_success_staff() throws Exception {
        when(staffService.findStaffById(1L)).thenReturn(staffOne);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/staff/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.userDetails.surname", is(staffOne.getUserDetails().getSurname())));
    }

    @WithMockUser(authorities = "MANAGER")
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

    @WithMockUser(authorities = "MANAGER")
    @Test
    public void findByName_success_manager() throws Exception {
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

    @WithMockUser(authorities = "STAFF")
    @Test
    public void findByName_success_staff() throws Exception {
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

    @WithMockUser(authorities = "MANAGER")
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

    @WithMockUser(authorities = "MANAGER")
    @Test
    public void createStaff_success_manager() throws Exception {
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

    @WithMockUser(authorities = "STAFF")
    @Test
    public void createStaff_forbidden_staff() throws Exception {
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
                .andExpect(status().isForbidden());
    }

    @WithMockUser(authorities = "MANAGER")
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

    @WithMockUser(authorities = "MANAGER")
    @Test
    public void updateStaff_success_manager() throws Exception {
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

    @WithMockUser(authorities = "STAFF")
    @Test
    public void updateStaff_success_staff() throws Exception {
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

    @WithMockUser(authorities = "MANAGER")
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
