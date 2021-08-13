package com.example.ssa.web.api;

import com.example.ssa.entity.skill.Category;
import com.example.ssa.entity.skill.Skill;
import com.example.ssa.exceptions.requests.bad.SkillDoesNotExistException;
import com.example.ssa.service.SkillService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@WebMvcTest(SkillController.class)
public class SkillControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    SkillService skillService;

    Category categoryOne = new Category(1L, "Category One", 57718);
    Category categoryTwo = new Category(2L, "Category Two", 57718);

    Skill skillOne = new Skill(1L, "Skill One", categoryOne);
    Skill skillTwo = new Skill(1L, "Skill Two", categoryTwo);

    @Test
    public void findAllSkills_success() throws Exception {
        List<Skill> records = new ArrayList<>(Arrays.asList(skillOne, skillTwo));

        when(skillService.findAllSkills()).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/skill/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Skill One")));
    }

    @Test
    public void findAllSkills_empty() throws Exception {
        List<Skill> records = new ArrayList<>(Arrays.asList());

        when(skillService.findAllSkills()).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/skill/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void findById_success() throws Exception {
        when(skillService.findSkillById(1L)).thenReturn(java.util.Optional.ofNullable(skillOne));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/skill/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Skill One")));
    }

    @Test
    public void findById_notFound() throws Exception {
        when(skillService.findSkillById(1L)).thenThrow(new SkillDoesNotExistException("Skill not found with that id"));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/skill/1")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(result -> assertTrue(result.getResolvedException() instanceof SkillDoesNotExistException))
                        .andExpect(result -> assertEquals("Skill not found with that id",
                                Objects.requireNonNull(result.getResolvedException()).getMessage())
                        );
    }

    @Test
    public void createSkill_success() throws Exception {
        Skill skill = Skill.builder()
                .name("Skill One")
                .category(categoryOne)
                .build();

        when(skillService.createSkill(skill)).thenReturn(skill);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/skill/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(skill));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Skill One")));
    }

    @Test
    public void updateSkill_success() throws Exception {
        Skill skill = Skill.builder()
                .id(1L)
                .name("Skill One")
                .category(categoryOne)
                .build();

        when(skillService.updateSkill(skill)).thenReturn(skill);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/api/skill/update")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(skill));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Skill One")));
    }

    @Test
    public void updateSkill_nullId() throws Exception {
        Skill skill = Skill.builder()
                .name("Skill One")
                .category(categoryOne)
                .build();

        when(skillService.updateSkill(skill)).thenThrow(new SkillDoesNotExistException("Skill not found with that id"));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/api/skill/update")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(skill));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof SkillDoesNotExistException))
                .andExpect(result -> assertEquals("Skill not found with that id",
                        Objects.requireNonNull(result.getResolvedException()).getMessage())
                );
    }

    @Test
    public void deleteSkill_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/skill/delete/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteSkill_notFound() throws Exception {
        doThrow(new SkillDoesNotExistException("Skill not found with that id")).when(skillService).deleteSkillById(1L);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/skill/delete/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof SkillDoesNotExistException))
                .andExpect(result -> assertEquals("Skill not found with that id",
                        Objects.requireNonNull(result.getResolvedException()).getMessage())
                );
    }
}
