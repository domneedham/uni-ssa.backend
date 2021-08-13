package com.example.ssa.web.api;

import com.example.ssa.entity.skill.Category;
import com.example.ssa.entity.skill.Skill;
import com.example.ssa.entity.skill.StaffSkill;
import com.example.ssa.entity.user.AppUser;
import com.example.ssa.entity.user.UserRole;
import com.example.ssa.exceptions.requests.bad.CategoryDoesNotExistException;
import com.example.ssa.repository.CategoryRepository;
import com.example.ssa.repository.SkillRepository;
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
    CategoryRepository categoryRepository;
    @MockBean
    SkillRepository skillRepository;
    @MockBean
    StaffSkillRepository staffSkillRepository;

    Category categoryOne = new Category(1L, "Test Category One", 26932);
    Category categoryTwo = new Category(2L, "Test Category Two", 19165);

    Skill skillOne = new Skill(1L, "Skill One", categoryOne);

    AppUser appUserOne = new AppUser(1L, "Test", "User", "test@user.com", UserRole.STAFF, "Test User");

    StaffSkill staffSkillOne = new StaffSkill(1L, skillOne, appUserOne, 5, LocalDateTime.now(), LocalDateTime.now().plusDays(30));

    @Test
    public void findAllCategories_success() throws Exception {
        List<Category> records = new ArrayList<>(List.of(categoryOne, categoryTwo));

        when(categoryRepository.findAll()).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/category/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(categoryOne.getName())));
    }

    @Test
    public void findAllCategories_empty() throws Exception {
        List<Category> records = new ArrayList<>(List.of());

        when(categoryRepository.findAll()).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/category/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void findById_success() throws Exception {
        when(categoryRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(categoryOne));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/category/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is(categoryOne.getName())));
    }

    @Test
    public void findById_notFound() throws Exception {
        when(categoryRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/category/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CategoryDoesNotExistException))
                .andExpect(result -> assertEquals("Category not found with that id",
                        Objects.requireNonNull(result.getResolvedException()).getMessage())
                );
    }

    @Test
    public void createCategory_success() throws Exception {
        Category category = Category.builder()
                .name(categoryOne.getName())
                .icon(categoryOne.getIcon())
                .build();
        when(categoryRepository.save(category)).thenReturn(category);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/category/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(category));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is(categoryOne.getName())));
    }

    @Test
    public void updateCategory_success() throws Exception {
        Category category = Category.builder()
                .id(categoryOne.getId())
                .name(categoryOne.getName())
                .icon(categoryOne.getIcon())
                .build();

        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(categoryOne));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/api/category/update")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(category));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is(categoryOne.getName())));
    }

    @Test
    public void updateCategory_nullId() throws Exception {
        Category category = Category.builder()
                .name(categoryOne.getName())
                .icon(categoryOne.getIcon())
                .build();

        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryRepository.findById(1L)).thenReturn(java.util.Optional.empty());

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

    @Test
    public void deleteCategory_success() throws Exception {
        when(categoryRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(categoryOne));
        when(skillRepository.findAllByCategoryId(any())).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/category/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(skillRepository, times(1)).findAllByCategoryId(any());
        verify(skillRepository, times(1)).deleteAllByCategoryId(any());
        verify(staffSkillRepository, times(1)).deleteAllBySkillIdIsIn(any());
        verify(categoryRepository, times(1)).deleteById(any());
    }

    @Test
    public void deleteCategory_notFound() throws Exception {
        when(categoryRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/category/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CategoryDoesNotExistException))
                .andExpect(result -> assertEquals("Category not found with that id",
                        Objects.requireNonNull(result.getResolvedException()).getMessage())
                );
        verify(skillRepository, never()).findAllByCategoryId(any());
        verify(skillRepository, never()).deleteAllByCategoryId(any());
        verify(staffSkillRepository, never()).deleteAllBySkillIdIsIn(any());
        verify(categoryRepository, never()).deleteById(any());
    }
}
