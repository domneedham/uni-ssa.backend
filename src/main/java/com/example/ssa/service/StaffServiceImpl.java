package com.example.ssa.service;

import com.example.ssa.entity.user.AppUser;
import com.example.ssa.entity.user.Staff;
import com.example.ssa.entity.user.UserRole;
import com.example.ssa.exceptions.requests.bad.ManagerDoesNotExistException;
import com.example.ssa.exceptions.requests.bad.StaffDoesNotExistException;
import com.example.ssa.repository.AppUserRepository;
import com.example.ssa.repository.StaffRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * A range of methods to handle Staff CRUD operations.
 */
@Slf4j
@Service
public class StaffServiceImpl implements StaffService {
    /**
     * The staff repository created by Spring.
     */
    private final StaffRepository staffRepository;

    /**
     * The app user repository created by Spring.
     */
    private final AppUserRepository appUserRepository;

    public StaffServiceImpl(StaffRepository staffRepository, AppUserRepository appUserRepository) {
        this.staffRepository = staffRepository;
        this.appUserRepository = appUserRepository;
    }

    /**
     * Finds all staff that exist.
     * @return A list of staff.
     */
    @Override
    public List<Staff> findAllStaff() {
        return staffRepository.findAll();
    }

    /**
     * Find the staff with the given id.
     * @param id The id of the staff member.
     * @return The found staff member.
     * @throws StaffDoesNotExistException If the staff member does not exist.
     */
    @Override
    public Staff findStaffById(Long id) throws StaffDoesNotExistException {
        Optional<Staff> staff = staffRepository.findById(id);

        if (staff.isEmpty()) {
            log.error(String.format("Staff not found with id of %d", id));
            throw new StaffDoesNotExistException("Staff not found with that id");
        }

        return staff.get();
    }

    /**
     * Find the staff by the name given. Does not have to be a full exact match, a partial match will suffice.
     * @param name The name (or partial name) of the staff.
     * @return A list of staff who match the name.
     */
    @Override
    public List<Staff> findStaffByName(String name) {
        return staffRepository.findAllByUserDetailsNameContainingIgnoreCase(name);
    }

    /**
     * Finds a staff by their email (username).
     * @param email The email of the staff.
     * @return The found staff.
     * @throws StaffDoesNotExistException If a staff is not found.
     */
    @Override
    public Staff findStaffByEmail(String email) throws StaffDoesNotExistException {
        Optional<Staff> staff = staffRepository.findByUserDetailsEmail(email);

        if (staff.isEmpty()) {
            log.error(String.format("Staff not found with id of %s", email));
            throw new StaffDoesNotExistException("Staff not found with that email");
        }

        return staff.get();
    }

    /**
     * Creates a new staff member.
     * @param staff The staff member to create.
     * @return The created staff member.
     * @throws ManagerDoesNotExistException If the manager of the staff member does not exist.
     */
    @Override
    public Staff createStaff(Staff staff) throws ManagerDoesNotExistException {
        // check manager id is manager
        Optional<AppUser> manager = appUserRepository.findById(staff.getManagerDetails().getId());
        if (manager.isEmpty() || manager.get().getUserRole() == UserRole.STAFF) {
            log.error(String.format("Manager not found with id of %s", manager.isPresent() ? manager.get().getId() : "null"));
            throw new ManagerDoesNotExistException("Manager not found with that id");
        }
        return staffRepository.save(staff);
    }

    /**
     * Updates a staff members details.
     * @param staff The updated staff details.
     * @return The updated staff member.
     * @throws StaffDoesNotExistException If the staff member does not exist.
     */
    @Override
    public Staff updateStaff(Staff staff) throws StaffDoesNotExistException {
        Optional<Staff> staffToUpdate = staffRepository.findById(staff.getId());

        if (staffToUpdate.isEmpty()) {
            log.error(String.format("Staff not found with id of %d", staff.getId()));
            throw new StaffDoesNotExistException("Staff not found with that id");
        }

        staffToUpdate.get().getUserDetails().setFirstname(staff.getUserDetails().getFirstname());
        staffToUpdate.get().getUserDetails().setSurname(staff.getUserDetails().getSurname());

        return staffRepository.save(staffToUpdate.get());
    }
}
