package com.example.ssa.web.api;

import com.example.ssa.entity.user.AppUser;
import com.example.ssa.entity.user.Staff;
import com.example.ssa.entity.user.UserRole;
import com.example.ssa.exceptions.requests.bad.ManagerDoesNotExistException;
import com.example.ssa.exceptions.requests.bad.StaffDoesNotExistException;
import com.example.ssa.repository.AppUserRepository;
import com.example.ssa.repository.StaffRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RequestMapping("api/staff")
@RestController
public class StaffController {
    private final StaffRepository staffRepository;
    private final AppUserRepository appUserRepository;

    public StaffController(StaffRepository staffRepository, AppUserRepository appUserRepository) {
        this.staffRepository = staffRepository;
        this.appUserRepository = appUserRepository;
    }

    @GetMapping("/")
    public List<Staff> findAll() {
        return staffRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Staff> findById(@PathVariable(value = "id") Long id) throws RuntimeException {
        Optional<Staff> staff = staffRepository.findById(id);

        if (staff.isEmpty()) {
            throw new StaffDoesNotExistException("Staff not found with that id");
        }

        return staff;
    }

    @GetMapping("/search/{name}")
    public List<Staff> findByName(@PathVariable(value = "name") String name) {
        return staffRepository.findAllByUserDetailsNameContainingIgnoreCase(name);
    }

    @PostMapping("/create")
    public Staff createNewStaff(@RequestBody Staff staff) throws RuntimeException {
        // check manager id is manager
        Optional<AppUser> manager = appUserRepository.findById(staff.getManagerDetails().getId());
        if (manager.isPresent()) {
            if (manager.get().getUserRole() == UserRole.STAFF) {
                throw new ManagerDoesNotExistException("Manager not found with that id");
            }
        } else {
            throw new ManagerDoesNotExistException("Manager not found with that id");
        }
        return staffRepository.save(staff);
    }

    @PutMapping("/update")
    public Staff update(@RequestBody Staff staff) throws RuntimeException {
        Optional<Staff> staffToUpdate = staffRepository.findById(staff.getId());

        if (staffToUpdate.isEmpty()) {
            throw new StaffDoesNotExistException("Staff not found with that id");
        }

        staffToUpdate.get().getUserDetails().setFirstname(staff.getUserDetails().getFirstname());
        staffToUpdate.get().getUserDetails().setSurname(staff.getUserDetails().getSurname());

        return staffRepository.save(staffToUpdate.get());
    }
}

