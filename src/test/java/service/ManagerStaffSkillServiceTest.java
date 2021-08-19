package service;

import com.example.ssa.entity.skill.Category;
import com.example.ssa.entity.skill.ManagerStaffSkill;
import com.example.ssa.entity.skill.Skill;
import com.example.ssa.entity.user.AppUser;
import com.example.ssa.entity.user.UserRole;
import com.example.ssa.exceptions.requests.bad.ManagerStaffSkillDoesNotExistException;
import com.example.ssa.repository.ManagerStaffSkillRepository;
import com.example.ssa.service.ManagerStaffSkillServiceImpl;
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
public class ManagerStaffSkillServiceTest {
    @Mock
    ManagerStaffSkillRepository managerStaffSkillRepository;

    @InjectMocks
    ManagerStaffSkillServiceImpl managerStaffSkillService;

    final AppUser appUserOne = new AppUser(1L, "Test", "User", "test@user.com", "password", UserRole.STAFF, "Test User");
    final AppUser appUserTwo = new AppUser(2L, "Test", "Two", "test@two.com", "password", UserRole.STAFF, "Test Two");

    final Category category = new Category(1L, "Category One", 26530);
    final Skill skillOne = new Skill(1L, "Skill One", category);
    final Skill skillTwo = new Skill(2L, "Skill Two", category);

    final ManagerStaffSkill managerStaffSkillOne = new ManagerStaffSkill(1L, skillOne, List.of(appUserOne));
    final ManagerStaffSkill managerStaffSkillTwo = new ManagerStaffSkill(2L, skillTwo, List.of(appUserOne, appUserTwo));

    @Test
    public void findAllSkillsShouldReturnListOfSkills() {
        List<ManagerStaffSkill> records = new ArrayList<>(List.of(managerStaffSkillOne, managerStaffSkillTwo));

        when(managerStaffSkillRepository.findAll()).thenReturn(records);

        List<ManagerStaffSkill> value = managerStaffSkillService.findAllManagerStaffSkills();

        assertThat(value).isSameAs(records);
    }

    @Test
    public void findAllSkillsShouldReturnEmptyListIfNoSkills() {
        List<ManagerStaffSkill> records = new ArrayList<>(List.of());

        when(managerStaffSkillRepository.findAll()).thenReturn(records);

        List<ManagerStaffSkill> value = managerStaffSkillService.findAllManagerStaffSkills();

        assertThat(value).isSameAs(records);
    }

    @Test
    public void findSkillByIdShouldReturnTheFoundSkillIfExists() {
        when(managerStaffSkillRepository.findById(managerStaffSkillOne.getId())).thenReturn(Optional.of(managerStaffSkillOne));

        Optional<ManagerStaffSkill> value = managerStaffSkillService.findManagerStaffSkillById(managerStaffSkillOne.getId());

        assertThat(value.get()).isSameAs(managerStaffSkillOne);
    }

    @Test
    public void findSkillByIdShouldThrowExceptionIfSkillDoesNotExist() {
        when(managerStaffSkillRepository.findById(-1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ManagerStaffSkillDoesNotExistException.class, () -> managerStaffSkillService.findManagerStaffSkillById(-1L));

        String expectedMessage = "Skill not found with that id";
        String actualMessage = exception.getMessage();

        assertThat(expectedMessage).isEqualTo(actualMessage);
    }
}
