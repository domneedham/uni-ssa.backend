package service;

import com.example.ssa.entity.skill.Category;
import com.example.ssa.entity.skill.Skill;
import com.example.ssa.exceptions.requests.bad.CategoryDoesNotExistException;
import com.example.ssa.repository.CategoryRepository;
import com.example.ssa.repository.SkillRepository;
import com.example.ssa.repository.StaffSkillRepository;
import com.example.ssa.service.CategoryServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceTest {
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    SkillRepository skillRepository;
    @Mock
    StaffSkillRepository staffSkillRepository;

    @InjectMocks
    CategoryServiceImpl categoryService;

    final Category categoryOne = new Category(1L, "Test Category One", 26932);
    final Category categoryTwo = new Category(2L, "Test Category Two", 19165);

    final Skill skillOne = new Skill(1L, "Skill One", categoryOne);

    @Test
    public void findAllCategoriesShouldReturnListOfCategories() {
        List<Category> records = new ArrayList<>(List.of(categoryOne, categoryTwo));

        when(categoryRepository.findAll()).thenReturn(records);

        List<Category> value = categoryService.findAllCategories();

        assertThat(value).isSameAs(records);
    }

    @Test
    public void findAllCategoriesShouldReturnEmptyListIfNoCategories() {
        List<Category> records = new ArrayList<>(List.of());

        when(categoryRepository.findAll()).thenReturn(records);

        List<Category> value = categoryService.findAllCategories();

        assertThat(value).isSameAs(records);
    }

    @Test
    public void findCategoryByIdShouldReturnTheFoundCategoryIfExists() {
        when(categoryRepository.findById(categoryOne.getId())).thenReturn(Optional.of(categoryOne));

        Optional<Category> value = categoryService.findCategoryById(categoryOne.getId());

        assertThat(value.get()).isSameAs(categoryOne);
    }

    @Test
    public void findCategoryByIdShouldThrowExceptionIfCategoryDoesNotExist() {
        when(categoryRepository.findById(-1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(CategoryDoesNotExistException.class, () -> categoryService.findCategoryById(-1L));

        String expectedMessage = "Category not found with that id";
        String actualMessage = exception.getMessage();

        assertThat(expectedMessage).isEqualTo(actualMessage);
    }

    @Test
    public void createCategoryShouldReturnTheCreatedCategory() {
        when(categoryRepository.save(categoryOne)).thenReturn(categoryOne);

        Category value = categoryService.createCategory(categoryOne);

        verify(categoryRepository, times(1)).save(any());
        assertThat(value).isSameAs(categoryOne);
    }

    @Test
    public void updateCategoryShouldReturnTheUpdatedCategory() {
        when(categoryRepository.save(categoryOne)).thenReturn(categoryOne);

        Category value = categoryService.createCategory(categoryOne);

        verify(categoryRepository, times(1)).save(any());
        assertThat(value).isSameAs(categoryOne);
    }

    @Test
    public void updateCategoryShouldThrowExceptionIfCategoryDoesNotExist() {
        when(categoryRepository.findById(categoryOne.getId())).thenReturn(java.util.Optional.empty());

        Exception exception = assertThrows(CategoryDoesNotExistException.class, () -> categoryService.updateCategory(categoryOne));

        String expectedMessage = "Category not found with that id";
        String actualMessage = exception.getMessage();

        verify(categoryRepository, never()).save(any());
        assertThat(expectedMessage).isEqualTo(actualMessage);
    }

    @Test
    public void deleteCategoryShouldDeleteAllCategoriesAndSkillsAndStaffSkillsUsingTheCategory() {
        List<Skill> skills = List.of(skillOne);
        when(skillRepository.findAllByCategoryId(categoryOne.getId())).thenReturn(skills);
        when(categoryRepository.findById(categoryOne.getId())).thenReturn(Optional.of(categoryOne));

        categoryService.deleteCategoryById(categoryOne.getId());

        ArrayList<Long> ids = new ArrayList<>();
        skills.forEach(skill -> ids.add(skill.getId()));

        verify(categoryRepository, times(1)).deleteById(categoryOne.getId());
        verify(skillRepository, times(1)).deleteAllByCategoryId(categoryOne.getId());
        verify(staffSkillRepository, times(1)).deleteAllBySkillIdIsIn(ids);
    }

    @Test
    public void deleteCategoryShouldDeleteNothingIfCategoryIsNotFound() {
        when(categoryRepository.findById(categoryOne.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(CategoryDoesNotExistException.class, () -> categoryService.deleteCategoryById(categoryOne.getId()));

        String expectedMessage = "Category not found with that id";
        String actualMessage = exception.getMessage();

        assertThat(expectedMessage).isSameAs(actualMessage);
        verify(categoryRepository, never()).deleteById(categoryOne.getId());
        verify(skillRepository, never()).deleteAllByCategoryId(any());
        verify(staffSkillRepository, never()).deleteAllBySkillIdIsIn(any());
    }
}
