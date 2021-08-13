package com.example.ssa.service;

import com.example.ssa.entity.user.AppUser;
import com.example.ssa.entity.user.Staff;
import com.example.ssa.entity.user.UserRole;
import com.example.ssa.exceptions.requests.bad.ManagerDoesNotExistException;
import com.example.ssa.exceptions.requests.bad.StaffDoesNotExistException;
import com.example.ssa.repository.AppUserRepository;
import com.example.ssa.repository.StaffRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffServiceImpl implements StaffService {
    private final StaffRepository staffRepository;
    private final AppUserRepository appUserRepository;

    public StaffServiceImpl(StaffRepository staffRepository, AppUserRepository appUserRepository) {
        this.staffRepository = staffRepository;
        this.appUserRepository = appUserRepository;
    }

    @Override
    public List<Staff> findAllStaff() {
        return staffRepository.findAll();
    }

    @Override
    public Optional<Staff> findStaffById(Long id) {
        Optional<Staff> staff = staffRepository.findById(id);

        if (staff.isEmpty()) {
            throw new StaffDoesNotExistException("Staff not found with that id");
        }

        return staff;
    }

    @Override
    public List<Staff> findStaffByName(String name) {
        return staffRepository.findAllByUserDetailsNameContainingIgnoreCase(name);
    }

    @Override
    public Optional<Staff> findStaffByEmail(String email) {
        Optional<Staff> staff = staffRepository.findByUserDetailsEmail(email);

        if (staff.isEmpty()) {
            throw new StaffDoesNotExistException("Staff not found with that email");
        }

        return staff;
    }

    @Override
    public Staff createStaff(Staff staff) {
        // check manager id is manager
        Optional<AppUser> manager = appUserRepository.findById(staff.getManagerDetails().getId());
        if (manager.isEmpty() || manager.get().getUserRole() == UserRole.STAFF) {
            throw new ManagerDoesNotExistException("Manager not found with that id");
        }
        return staffRepository.save(staff);
    }

    @Override
    public Staff updateStaff(Staff staff) {
        Optional<Staff> staffToUpdate = staffRepository.findById(staff.getId());

        if (staffToUpdate.isEmpty()) {
            throw new StaffDoesNotExistException("Staff not found with that id");
        }

        staffToUpdate.get().getUserDetails().setFirstname(staff.getUserDetails().getFirstname());
        staffToUpdate.get().getUserDetails().setSurname(staff.getUserDetails().getSurname());

        return staffRepository.save(staffToUpdate.get());
    }
}
