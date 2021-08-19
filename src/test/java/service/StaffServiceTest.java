package service;

import com.example.ssa.entity.skill.Category;
import com.example.ssa.entity.skill.Skill;
import com.example.ssa.entity.skill.StaffSkill;
import com.example.ssa.entity.user.AppUser;
import com.example.ssa.entity.user.Staff;
import com.example.ssa.entity.user.UserRole;
import com.example.ssa.exceptions.requests.bad.ManagerDoesNotExistException;
import com.example.ssa.exceptions.requests.bad.StaffDoesNotExistException;
import com.example.ssa.repository.AppUserRepository;
import com.example.ssa.repository.StaffRepository;
import com.example.ssa.service.StaffServiceImpl;
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

@SuppressWarnings("OptionalGetWithoutIsPresent")
@RunWith(MockitoJUnitRunner.class)
public class StaffServiceTest {
    @Mock
    AppUserRepository appUserRepository;
    @Mock
    StaffRepository staffRepository;

    @InjectMocks
    StaffServiceImpl staffService;

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

    @Test
    public void findAllStaffShouldReturnAListOfStaffIfExists() {
        List<Staff> records = List.of(staffOne, staffTwo);

        when(staffRepository.findAll()).thenReturn(records);

        List<Staff> value = staffService.findAllStaff();

        assertThat(value).isSameAs(records);
    }

    @Test
    public void findAllStaffShouldReturnAnEmptyListIfNone() {
        List<Staff> records = List.of();

        when(staffRepository.findAll()).thenReturn(records);

        List<Staff> value = staffService.findAllStaff();

        assertThat(value).isSameAs(records);
    }

    @Test
    public void findStaffByIdShouldReturnStaffIfExists() {
        when(staffRepository.findById(staffOne.getId())).thenReturn(Optional.of(staffOne));

        Optional<Staff> value = staffService.findStaffById(staffOne.getId());

        assertThat(value.get()).isSameAs(staffOne);
    }

    @Test
    public void findStaffByIdShouldThrowExceptionIfNotExists() {
        when(staffRepository.findById(staffOne.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(StaffDoesNotExistException.class, () -> staffService.findStaffById(staffOne.getId()));

        String expectedMessage = "Staff not found with that id";
        String actualMessage = exception.getMessage();

        assertThat(expectedMessage).isEqualTo(actualMessage);
    }

    @Test
    public void findStaffByNameShouldReturnAListOfStaffIfExists() {
        List<Staff> records = List.of(staffOne);

        when(staffRepository.findAllByUserDetailsNameContainingIgnoreCase("test")).thenReturn(records);

        List<Staff> value = staffService.findStaffByName("test");

        assertThat(value).isSameAs(records);
    }

    @Test
    public void findStaffByNameShouldReturnAnEmptyListIfNone() {
        List<Staff> records = List.of();

        when(staffRepository.findAllByUserDetailsNameContainingIgnoreCase("test")).thenReturn(records);

        List<Staff> value = staffService.findStaffByName("test");

        assertThat(value).isSameAs(records);
    }

    @Test
    public void createStaffShouldReturnTheCreatedStaffIfValid() {
        when(staffRepository.save(staffOne)).thenReturn(staffOne);
        when(appUserRepository.findById(staffOne.getManagerDetails().getId())).thenReturn(Optional.of(staffOne.getManagerDetails()));

        Staff value = staffService.createStaff(staffOne);

        verify(staffRepository, times(1)).save(any());
        assertThat(value).isSameAs(staffOne);
    }

    @Test
    public void createStaffShouldThrowExceptionIfManagerDoesNotExist() {
        when(appUserRepository.findById(staffOne.getManagerDetails().getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ManagerDoesNotExistException.class, () -> staffService.createStaff(staffOne));

        String expectedMessage = "Manager not found with that id";
        String actualMessage = exception.getMessage();

        assertThat(expectedMessage).isEqualTo(actualMessage);
        verify(staffRepository, never()).save(any());
    }

    @Test
    public void updateStaffShouldReturnTheUpdatedStaffIfValid() {
        when(staffRepository.findById(staffOne.getId())).thenReturn(Optional.of(staffOne));
        when(staffRepository.save(staffOne)).thenReturn(staffOne);

        Staff value = staffService.updateStaff(staffOne);

        verify(staffRepository, times(1)).save(any());
        assertThat(value).isSameAs(staffOne);
    }

    @Test
    public void updateStaffShouldThrowExceptionIfStaffDoesNotExist() {
        when(staffRepository.findById(staffOne.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(StaffDoesNotExistException.class, () -> staffService.updateStaff(staffOne));

        String expectedMessage = "Staff not found with that id";
        String actualMessage = exception.getMessage();

        verify(staffRepository, never()).save(any());
        assertThat(expectedMessage).isEqualTo(actualMessage);
    }

    @Test
    public void findStaffByEmailShouldReturnStaffIfExists() {
        when(staffRepository.findByUserDetailsEmail(staffOne.getUserDetails().getEmail())).thenReturn(Optional.of(staffOne));

        Optional<Staff> value = staffService.findStaffByEmail(staffOne.getUserDetails().getEmail());

        assertThat(value.get()).isSameAs(staffOne);
    }

    @Test
    public void findStaffByEmailShouldThrowExceptionIfNotExists() {
        when(staffRepository.findByUserDetailsEmail(staffOne.getUserDetails().getEmail())).thenReturn(Optional.empty());

        Exception exception = assertThrows(StaffDoesNotExistException.class, () -> staffService.findStaffByEmail(staffOne.getUserDetails().getEmail()));

        String expectedMessage = "Staff not found with that email";
        String actualMessage = exception.getMessage();

        assertThat(expectedMessage).isEqualTo(actualMessage);
    }

}
