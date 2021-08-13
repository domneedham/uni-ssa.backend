package com.example.ssa.web.api;

import com.example.ssa.entity.user.AppUser;
import com.example.ssa.entity.user.Manager;
import com.example.ssa.entity.user.UserRole;
import com.example.ssa.exceptions.requests.bad.ManagerDoesNotExistException;
import com.example.ssa.repository.ManagerRepository;
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
    ManagerRepository managerRepository;

    AppUser appUserManagerOne = new AppUser(3L, "Test", "User", "test@user.com", UserRole.MANAGER, "Test User");
    AppUser appUserManagerTwo = new AppUser(3L, "Test", "Manager", "test@manager.com", UserRole.MANAGER, "Test Manager");
    AppUser appUserStaffOne = new AppUser(1L, "Test", "User", "test@user.com", UserRole.STAFF, "Test User");
    AppUser appUserStaffTwo = new AppUser(2L, "Test", "Manager", "test@manager.com", UserRole.STAFF, "Test Manager");

    List<AppUser> staffListOne = new ArrayList<>(List.of(appUserStaffOne));
    List<AppUser> staffListTwo = new ArrayList<>(List.of(appUserStaffTwo));

    Manager managerOne = new Manager(3L, appUserManagerOne, staffListOne);
    Manager managerTwo = new Manager(3L, appUserManagerTwo, staffListTwo);

    @Test
    public void findAllManagers_success() throws Exception {
        List<Manager> records = new ArrayList<>(List.of(managerOne, managerTwo));

        when(managerRepository.findAll()).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/manager/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userDetails.surname", is(managerOne.getUserDetails().getSurname())))
                .andExpect(jsonPath("$[1].userDetails.surname", is(managerTwo.getUserDetails().getSurname())));
    }

    @Test
    public void findById_success() throws Exception {
        when(managerRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(managerOne));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/manager/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.userDetails.surname", is(managerOne.getUserDetails().getSurname())));
    }

    @Test
    public void findById_notFound() throws Exception {
        when(managerRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/manager/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ManagerDoesNotExistException))
                .andExpect(result -> assertEquals("Manager not found with that id",
                        Objects.requireNonNull(result.getResolvedException()).getMessage())
                );
    }

    @Test
    public void findByName_success() throws Exception {
        List<Manager> records = new ArrayList<>(List.of(managerOne, managerTwo));
        when(managerRepository.findAllByUserDetailsNameContainingIgnoreCase("test")).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/manager/search/test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userDetails.surname", is(managerOne.getUserDetails().getSurname())))
                .andExpect(jsonPath("$[1].userDetails.surname", is(managerTwo.getUserDetails().getSurname())));
    }

    @Test
    public void findByName_noMatch() throws Exception {
        List<Manager> records = new ArrayList<>(List.of());
        when(managerRepository.findAllByUserDetailsNameContainingIgnoreCase("testyuser")).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/manager/search/testyuser")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}