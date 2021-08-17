package com.example.ssa.web.api;

import com.example.ssa.entity.skill.Category;
import com.example.ssa.entity.skill.Skill;
import com.example.ssa.entity.skill.StaffSkill;
import com.example.ssa.entity.user.AppUser;
import com.example.ssa.entity.user.UserRole;
import com.example.ssa.exceptions.requests.bad.CategoryDoesNotExistException;
import com.example.ssa.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserDetailsService userDetailsService;
    @MockBean
    CategoryService categoryService;

    Category categoryOne = new Category(1L, "Test Category One", 26932);
    Category categoryTwo = new Category(2L, "Test Category Two", 19165);

    Skill skillOne = new Skill(1L, "Skill One", categoryOne);

    AppUser appUserOne = new AppUser(1L, "Test", "User", "test@user.com","password", UserRole.STAFF, "Test User");

    StaffSkill staffSkillOne = new StaffSkill(1L, skillOne, appUserOne, 5, LocalDateTime.now(), LocalDateTime.now().plusDays(30));

    @WithMockUser(roles = "MANAGER")
    @Test
    public void findAllCategories_success() throws Exception {
        List<Category> records = new ArrayList<>(List.of(categoryOne, categoryTwo));

        when(categoryService.findAllCategories()).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/category/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(categoryOne.getName())));
    }

    @WithMockUser(roles = "MANAGER")
    @Test
    public void findAllCategories_empty() throws Exception {
        List<Category> records = new ArrayList<>(List.of());

        when(categoryService.findAllCategories()).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/category/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @WithMockUser(roles = "MANAGER")
    @Test
    public void findById_success() throws Exception {
        when(categoryService.findCategoryById(1L)).thenReturn(java.util.Optional.ofNullable(categoryOne));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/category/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is(categoryOne.getName())));
    }

    @WithMockUser(roles = "MANAGER")
    @Test
    public void findById_notFound() throws Exception {
        when(categoryService.findCategoryById(1L)).thenThrow(new CategoryDoesNotExistException("Category not found with that id"));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/category/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CategoryDoesNotExistException))
                .andExpect(result -> assertEquals("Category not found with that id",
                        Objects.requireNonNull(result.getResolvedException()).getMessage())
                );
    }

    @WithMockUser(roles = "MANAGER")
    @Test
    public void createCategory_success() throws Exception {
        Category category = Category.builder()
                .name(categoryOne.getName())
                .icon(categoryOne.getIcon())
                .build();
        when(categoryService.createCategory(category)).thenReturn(category);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/category/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(category));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is(categoryOne.getName())));
    }

    @WithMockUser(roles = "MANAGER")
    @Test
    public void updateCategory_success() throws Exception {
        Category category = Category.builder()
                .id(categoryOne.getId())
                .name(categoryOne.getName())
                .icon(categoryOne.getIcon())
                .build();

        when(categoryService.updateCategory(category)).thenReturn(category);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/api/category/update")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(category));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is(categoryOne.getName())));
    }

    @WithMockUser(roles = "MANAGER")
    @Test
    public void updateCategory_nullId() throws Exception {
        Category category = Category.builder()
                .name(categoryOne.getName())
                .icon(categoryOne.getIcon())
                .build();

        when(categoryService.updateCategory(any())).thenThrow(new CategoryDoesNotExistException("Category not found with that id"));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/api/category/update")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(category));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CategoryDoesNotExistException))
                .andExpect(result -> assertEquals("Category not found with that id",
                        Objects.requireNonNull(result.getResolvedException()).getMessage())
                );
    }

    @WithMockUser(roles = "MANAGER")
    @Test
    public void deleteCategory_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/category/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "MANAGER")
    @Test
    public void deleteCategory_notFound() throws Exception {
        doThrow(new CategoryDoesNotExistException("Category not found with that id")).when(categoryService).deleteCategoryById(1L);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/category/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CategoryDoesNotExistException))
                .andExpect(result -> assertEquals("Category not found with that id",
                        Objects.requireNonNull(result.getResolvedException()).getMessage())
                );
    }
}
