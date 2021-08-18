package com.example.ssa.web.api;

import com.example.ssa.entity.user.AppUser;
import com.example.ssa.entity.user.Manager;
import com.example.ssa.entity.user.UserRole;
import com.example.ssa.exceptions.requests.bad.ManagerDoesNotExistException;
import com.example.ssa.service.ManagerService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ManagerController.class)
public class ManagerControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserDetailsService userDetailsService;
    @MockBean
    ManagerService managerService;

    final AppUser appUserManagerOne = new AppUser(3L, "Test", "User", "test@user.com", "password",UserRole.MANAGER, "Test User");
    final AppUser appUserManagerTwo = new AppUser(3L, "Test", "Manager", "test@manager.com","password", UserRole.MANAGER, "Test Manager");
    final AppUser appUserStaffOne = new AppUser(1L, "Test", "User", "test@user.com", "password",UserRole.STAFF, "Test User");
    final AppUser appUserStaffTwo = new AppUser(2L, "Test", "Manager", "test@manager.com", "password",UserRole.STAFF, "Test Manager");

    final List<AppUser> staffListOne = new ArrayList<>(List.of(appUserStaffOne));
    final List<AppUser> staffListTwo = new ArrayList<>(List.of(appUserStaffTwo));

    final Manager managerOne = new Manager(3L, appUserManagerOne, staffListOne);
    final Manager managerTwo = new Manager(3L, appUserManagerTwo, staffListTwo);

    @WithMockUser(authorities = "MANAGER")
    @Test
    public void findAllManagers_success_manager() throws Exception {
        List<Manager> records = new ArrayList<>(List.of(managerOne, managerTwo));

        when(managerService.findAllManagers()).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/manager/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userDetails.surname", is(managerOne.getUserDetails().getSurname())))
                .andExpect(jsonPath("$[1].userDetails.surname", is(managerTwo.getUserDetails().getSurname())));
    }

    @WithMockUser(authorities = "STAFF")
    @Test
    public void findAllManagers_forbidden_staff() throws Exception {
        List<Manager> records = new ArrayList<>(List.of(managerOne, managerTwo));

        when(managerService.findAllManagers()).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/manager/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(authorities = "MANAGER")
    @Test
    public void findById_success_manager() throws Exception {
        when(managerService.findManagerById(1L)).thenReturn(java.util.Optional.of(managerOne));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/manager/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.userDetails.surname", is(managerOne.getUserDetails().getSurname())));
    }

    @WithMockUser(authorities = "STAFF")
    @Test
    public void findById_success_staff() throws Exception {
        when(managerService.findManagerById(1L)).thenReturn(java.util.Optional.of(managerOne));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/manager/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.userDetails.surname", is(managerOne.getUserDetails().getSurname())));
    }

    @WithMockUser(authorities = "MANAGER")
    @Test
    public void findById_notFound() throws Exception {
        when(managerService.findManagerById(1L)).thenThrow(new ManagerDoesNotExistException("Manager not found with that id"));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/manager/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ManagerDoesNotExistException))
                .andExpect(result -> assertEquals("Manager not found with that id",
                        Objects.requireNonNull(result.getResolvedException()).getMessage())
                );
    }

    @WithMockUser(authorities = "MANAGER")
    @Test
    public void findByName_success_manager() throws Exception {
        List<Manager> records = new ArrayList<>(List.of(managerOne, managerTwo));
        when(managerService.findManagersByName("test")).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/manager/search/test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userDetails.surname", is(managerOne.getUserDetails().getSurname())))
                .andExpect(jsonPath("$[1].userDetails.surname", is(managerTwo.getUserDetails().getSurname())));
    }

    @WithMockUser(authorities = "STAFF")
    @Test
    public void findByName_forbidden_staff() throws Exception {
        List<Manager> records = new ArrayList<>(List.of(managerOne, managerTwo));
        when(managerService.findManagersByName("test")).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/manager/search/test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(authorities = "MANAGER")
    @Test
    public void findByName_noMatch() throws Exception {
        List<Manager> records = new ArrayList<>(List.of());
        when(managerService.findManagersByName("test")).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/manager/search/test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
