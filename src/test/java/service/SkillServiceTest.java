package service;

import com.example.ssa.entity.skill.Category;
import com.example.ssa.entity.skill.Skill;
import com.example.ssa.exceptions.requests.bad.SkillDoesNotExistException;
import com.example.ssa.repository.SkillRepository;
import com.example.ssa.repository.StaffSkillRepository;
import com.example.ssa.service.SkillServiceImpl;
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

@RunWith(MockitoJUnitRunner.class)
public class SkillServiceTest {
    @Mock
    SkillRepository skillRepository;
    @Mock
    StaffSkillRepository staffSkillRepository;

    @InjectMocks
    SkillServiceImpl skillService;

    final Category categoryOne = new Category(1L, "Category One", 57718);
    final Category categoryTwo = new Category(2L, "Category Two", 57718);

    final Skill skillOne = new Skill(1L, "Skill One", categoryOne);
    final Skill skillTwo = new Skill(1L, "Skill Two", categoryTwo);

    @Test
    public void findAllSkillsShouldReturnListOfSkills() {
        List<Skill> records = new ArrayList<>(List.of(skillOne, skillTwo));

        when(skillRepository.findAll()).thenReturn(records);

        List<Skill> value = skillService.findAllSkills();

        assertThat(value).isSameAs(records);
    }

    @Test
    public void findAllSkillsShouldReturnEmptyListIfNoSkills() {
        List<Skill> records = new ArrayList<>(List.of());

        when(skillRepository.findAll()).thenReturn(records);

        List<Skill> value = skillService.findAllSkills();

        assertThat(value).isSameAs(records);
    }

    @Test
    public void findSkillByIdShouldReturnTheFoundSkillIfExists() {
        when(skillRepository.findById(skillOne.getId())).thenReturn(Optional.of(skillOne));

        Skill value = skillService.findSkillById(skillOne.getId());

        assertThat(value).isSameAs(skillOne);
    }

    @Test
    public void findSkillByIdShouldThrowExceptionIfSkillDoesNotExist() {
        when(skillRepository.findById(-1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(SkillDoesNotExistException.class, () -> skillService.findSkillById(-1L));

        String expectedMessage = "Skill not found with that id";
        String actualMessage = exception.getMessage();

        assertThat(expectedMessage).isEqualTo(actualMessage);
    }

    @Test
    public void findSkillsByNameShouldReturnListOfSkills() {
        List<Skill> records = new ArrayList<>(List.of(skillOne, skillTwo));

        when(skillRepository.findAllByNameContainingIgnoreCase("test")).thenReturn(records);

        List<Skill> value = skillService.findSkillsByName("test");

        assertThat(value).isSameAs(records);
    }

    @Test
    public void findSkillsByNameShouldReturnEmptyListIfNoSkills() {
        List<Skill> records = new ArrayList<>(List.of());

        when(skillRepository.findAllByNameContainingIgnoreCase("test")).thenReturn(records);

        List<Skill> value = skillService.findSkillsByName("test");

        assertThat(value).isSameAs(records);
    }

    @Test
    public void createSkillShouldReturnTheCreatedSkill() {
        when(skillRepository.save(skillOne)).thenReturn(skillOne);

        Skill value = skillService.createSkill(skillOne);

        verify(skillRepository, times(1)).save(any());
        assertThat(value).isSameAs(skillOne);
    }

    @Test
    public void updateSkillShouldReturnTheUpdatedSkill() {
        when(skillRepository.save(skillOne)).thenReturn(skillOne);

        Skill value = skillService.createSkill(skillOne);

        verify(skillRepository, times(1)).save(any());
        assertThat(value).isSameAs(skillOne);
    }

    @Test
    public void updateSkillShouldThrowExceptionIfSkillDoesNotExist() {
        when(skillRepository.findById(skillOne.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(SkillDoesNotExistException.class, () -> skillService.updateSkill(skillOne));

        String expectedMessage = "Skill not found with that id";
        String actualMessage = exception.getMessage();

        verify(skillRepository, never()).save(any());
        assertThat(expectedMessage).isEqualTo(actualMessage);
    }

    @Test
    public void deleteSkillShouldDeleteAllSkillsAndStaffSkillsUsingTheSkill() {
        when(skillRepository.findById(skillOne.getId())).thenReturn(Optional.of(skillOne));

        skillService.deleteSkillById(skillOne.getId());

        verify(skillRepository, times(1)).deleteById(skillOne.getId());
        verify(staffSkillRepository, times(1)).deleteAllBySkillId(skillOne.getId());
    }

    @Test
    public void deleteSkillShouldDeleteNothingIfSkillIsNotFound() {
        when(skillRepository.findById(skillOne.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(SkillDoesNotExistException.class, () -> skillService.deleteSkillById(skillOne.getId()));

        String expectedMessage = "Skill not found with that id";
        String actualMessage = exception.getMessage();

        assertThat(expectedMessage).isSameAs(actualMessage);
        verify(skillRepository, never()).deleteById(skillOne.getId());
        verify(staffSkillRepository, never()).deleteAllBySkillId(any());
    }
}
