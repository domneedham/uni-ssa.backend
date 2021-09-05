package service;

import com.example.ssa.entity.skill.Category;
import com.example.ssa.entity.skill.Skill;
import com.example.ssa.entity.skill.StaffSkill;
import com.example.ssa.entity.user.AppUser;
import com.example.ssa.entity.user.UserRole;
import com.example.ssa.exceptions.requests.bad.StaffSkillDoesNotExistException;
import com.example.ssa.repository.StaffSkillRepository;
import com.example.ssa.service.StaffSkillServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StaffSkillServiceTest {
    @Mock
    StaffSkillRepository staffSkillRepository;

    @InjectMocks
    StaffSkillServiceImpl staffSkillService;

    final Category categoryOne = new Category(1L, "Category One", 57718);
    final Category categoryTwo = new Category(2L, "Category Two", 57718);

    final Skill skillOne = new Skill(1L, "Skill One", categoryOne);
    final Skill skillTwo = new Skill(2L, "Skill Two", categoryTwo);

    final AppUser appUserOne = new AppUser(1L, "Test", "User", "test@user.com", "password", UserRole.STAFF, "Test User");
    final AppUser appUserTwo = new AppUser(2L, "Test", "Staff", "test@staff.com", "password", UserRole.STAFF, "Test Staff");

    final StaffSkill staffSkillOne = new StaffSkill(1L, skillOne, appUserOne, 5, LocalDateTime.now(), LocalDateTime.now().plusDays(30));
    final StaffSkill staffSkillTwo = new StaffSkill(1L, skillTwo, appUserTwo, 3, LocalDateTime.now(), null);

    @Test
    public void findAllSkillsShouldReturnListOfSkills() {
        List<StaffSkill> records = new ArrayList<>(List.of(staffSkillOne, staffSkillTwo));

        when(staffSkillRepository.findAll()).thenReturn(records);

        List<StaffSkill> value = staffSkillService.findAllStaffSkills();

        assertThat(value).isSameAs(records);
    }

    @Test
    public void findAllSkillsShouldReturnEmptyListIfNoSkills() {
        List<StaffSkill> records = new ArrayList<>(List.of());

        when(staffSkillRepository.findAll()).thenReturn(records);

        List<StaffSkill> value = staffSkillService.findAllStaffSkills();

        assertThat(value).isSameAs(records);
    }

    @Test
    public void findSkillByIdShouldReturnTheFoundSkillIfExists() {
        when(staffSkillRepository.findById(staffSkillOne.getId())).thenReturn(Optional.of(staffSkillOne));

        StaffSkill value = staffSkillService.findStaffSkillById(staffSkillOne.getId());

        assertThat(value).isSameAs(staffSkillOne);
    }

    @Test
    public void findSkillByIdShouldThrowExceptionIfSkillDoesNotExist() {
        when(staffSkillRepository.findById(-1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(StaffSkillDoesNotExistException.class, () -> staffSkillService.findStaffSkillById(-1L));

        String expectedMessage = "Staff skill not found with that id";
        String actualMessage = exception.getMessage();

        assertThat(expectedMessage).isEqualTo(actualMessage);
    }

    @Test
    public void findSkillStaffByStaffIdAndSkillIdShouldReturnTheFoundSkillIfExists() {
        when(staffSkillRepository.findBySkillIdAndStaffDetailsId(staffSkillOne.getId(), staffSkillOne.getStaffDetails().getId())).thenReturn(Optional.of(staffSkillOne));

        StaffSkill value = staffSkillService.findStaffSkillBySkillIdAndStaffId(staffSkillOne.getId(), staffSkillOne.getStaffDetails().getId());

        assertThat(value).isSameAs(staffSkillOne);
    }

    @Test
    public void findSkillStaffByStaffIdAndSkillIdShouldThrowExceptionIfSkillDoesNotExist() {
        when(staffSkillRepository.findBySkillIdAndStaffDetailsId(staffSkillOne.getId(), staffSkillOne.getStaffDetails().getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(StaffSkillDoesNotExistException.class, () -> staffSkillService.findStaffSkillBySkillIdAndStaffId(staffSkillOne.getId(), staffSkillOne.getStaffDetails().getId()));

        String expectedMessage = "Staff skill not found with that id";
        String actualMessage = exception.getMessage();

        assertThat(expectedMessage).isEqualTo(actualMessage);
    }

    @Test
    public void findAllSkillsByStaffIdShouldReturnListOfSkills() {
        List<StaffSkill> records = new ArrayList<>(List.of(staffSkillOne, staffSkillTwo));

        when(staffSkillRepository.findByStaffDetailsId(appUserOne.getId())).thenReturn(records);

        List<StaffSkill> value = staffSkillService.findAllStaffSkillsByStaffId(appUserOne.getId());

        assertThat(value).isSameAs(records);
    }

    @Test
    public void findAllSkillsByStaffIdShouldReturnEmptyListIfNoSkills() {
        List<StaffSkill> records = new ArrayList<>(List.of());

        when(staffSkillRepository.findByStaffDetailsId(appUserOne.getId())).thenReturn(records);

        List<StaffSkill> value = staffSkillService.findAllStaffSkillsByStaffId(appUserOne.getId());

        assertThat(value).isSameAs(records);
    }

    @Test
    public void assignStaffSkillShouldReturnTheCreatedStaffSkill() {
        when(staffSkillRepository.save(staffSkillOne)).thenReturn(staffSkillOne);

        StaffSkill value = staffSkillService.assignStaffSkill(staffSkillOne);

        verify(staffSkillRepository, times(1)).save(any());
        assertThat(value).isSameAs(staffSkillOne);
    }

    @Test
    public void updateStaffSkillShouldReturnTheUpdatedStaffSkillIfValid() {
        when(staffSkillRepository.findBySkillIdAndStaffDetailsId(staffSkillOne.getId(), staffSkillOne.getStaffDetails().getId())).thenReturn(Optional.of(staffSkillOne));
        when(staffSkillRepository.save(staffSkillOne)).thenReturn(staffSkillOne);

        StaffSkill value = staffSkillService.updateStaffSkill(staffSkillOne);

        verify(staffSkillRepository, times(1)).save(any());
        assertThat(value).isSameAs(staffSkillOne);
    }

    @Test
    public void updateCategoryShouldThrowExceptionIfCategoryDoesNotExist() {
        when(staffSkillRepository.findBySkillIdAndStaffDetailsId(staffSkillOne.getId(), staffSkillOne.getStaffDetails().getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(StaffSkillDoesNotExistException.class, () -> staffSkillService.updateStaffSkill(staffSkillOne));

        String expectedMessage = "Staff skill not found with that id";
        String actualMessage = exception.getMessage();

        verify(staffSkillRepository, never()).save(any());
        assertThat(expectedMessage).isEqualTo(actualMessage);
    }
}
