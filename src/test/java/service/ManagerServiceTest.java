package service;

import com.example.ssa.entity.user.AppUser;
import com.example.ssa.entity.user.Manager;
import com.example.ssa.entity.user.UserRole;
import com.example.ssa.exceptions.requests.bad.ManagerDoesNotExistException;
import com.example.ssa.repository.ManagerRepository;
import com.example.ssa.service.ManagerServiceImpl;
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
public class ManagerServiceTest {
    @Mock
    ManagerRepository managerRepository;

    @InjectMocks
    ManagerServiceImpl managerService;

    final AppUser appUserManagerOne = new AppUser(3L, "Test", "User", "test@user.com", "password", UserRole.MANAGER, "Test User");
    final AppUser appUserManagerTwo = new AppUser(3L, "Test", "Manager", "test@manager.com","password", UserRole.MANAGER, "Test Manager");
    final AppUser appUserStaffOne = new AppUser(1L, "Test", "User", "test@user.com", "password",UserRole.STAFF, "Test User");
    final AppUser appUserStaffTwo = new AppUser(2L, "Test", "Manager", "test@manager.com", "password",UserRole.STAFF, "Test Manager");

    final List<AppUser> staffListOne = new ArrayList<>(List.of(appUserStaffOne));
    final List<AppUser> staffListTwo = new ArrayList<>(List.of(appUserStaffTwo));

    final Manager managerOne = new Manager(3L, appUserManagerOne, staffListOne);
    final Manager managerTwo = new Manager(3L, appUserManagerTwo, staffListTwo);

    @Test
    public void findAllManagersShouldReturnAListOfManagersIfExists() {
        List<Manager> records = List.of(managerOne, managerTwo);

        when(managerRepository.findAll()).thenReturn(records);

        List<Manager> value = managerService.findAllManagers();

        assertThat(value).isSameAs(records);
    }

    @Test
    public void findAllManagersShouldReturnAnEmptyListIfNone() {
        List<Manager> records = List.of();

        when(managerRepository.findAll()).thenReturn(records);

        List<Manager> value = managerService.findAllManagers();

        assertThat(value).isSameAs(records);
    }

    @Test
    public void findManagerByIdShouldReturnManagerIfExists() {
        when(managerRepository.findById(managerOne.getId())).thenReturn(Optional.of(managerOne));

        Manager value = managerService.findManagerById(managerOne.getId());

        assertThat(value).isSameAs(managerOne);
    }

    @Test
    public void findManagerByIdShouldThrowExceptionIfNotExists() {
        when(managerRepository.findById(managerOne.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ManagerDoesNotExistException.class, () -> managerService.findManagerById(managerOne.getId()));

        String expectedMessage = "Manager not found with that id";
        String actualMessage = exception.getMessage();

        assertThat(expectedMessage).isEqualTo(actualMessage);
    }

    @Test
    public void findManagersByNameShouldReturnAListOfManagersIfExists() {
        List<Manager> records = List.of(managerOne);

        when(managerRepository.findAllByUserDetailsNameContainingIgnoreCase("test")).thenReturn(records);

        List<Manager> value = managerService.findManagersByName("test");

        assertThat(value).isSameAs(records);
    }

    @Test
    public void findManagersByNameShouldReturnAnEmptyListIfNone() {
        List<Manager> records = List.of();

        when(managerRepository.findAllByUserDetailsNameContainingIgnoreCase("test")).thenReturn(records);

        List<Manager> value = managerService.findManagersByName("test");

        assertThat(value).isSameAs(records);
    }

    @Test
    public void createManagerShouldReturnTheCreatedManager() {
        when(managerRepository.save(managerOne)).thenReturn(managerOne);

        Manager value = managerService.createManager(managerOne);

        verify(managerRepository, times(1)).save(any());
        assertThat(value).isSameAs(managerOne);
    }

    @Test
    public void findManagerByEmailShouldReturnManagerIfExists() {
        when(managerRepository.findByUserDetailsEmail(managerOne.getUserDetails().getEmail())).thenReturn(Optional.of(managerOne));

        Manager value = managerService.findManagerByEmail(managerOne.getUserDetails().getEmail());

        assertThat(value).isSameAs(managerOne);
    }

    @Test
    public void findManagerByEmailShouldThrowExceptionIfNotExists() {
        when(managerRepository.findByUserDetailsEmail(managerOne.getUserDetails().getEmail())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ManagerDoesNotExistException.class, () -> managerService.findManagerByEmail(managerOne.getUserDetails().getEmail()));

        String expectedMessage = "Manager not found with that email";
        String actualMessage = exception.getMessage();

        assertThat(expectedMessage).isEqualTo(actualMessage);
    }

}
