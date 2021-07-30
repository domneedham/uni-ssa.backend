package com.example.ssa.web.api;

import com.example.ssa.entity.user.AppUser;
import com.example.ssa.entity.user.Staff;
import com.example.ssa.entity.user.UserRole;
import com.example.ssa.repository.AppUserRepository;
import com.example.ssa.repository.StaffRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public Optional<Staff> findById(@PathVariable(value = "id") Long id) {
        return staffRepository.findById(id);
    }

    @GetMapping("/search/{name}")
    public List<Staff> findByName(@PathVariable(value = "name") String name) {
        return staffRepository.findAllByUserDetailsNameContainingIgnoreCase(name);
    }

    @PostMapping("/create")
    public Staff createNewStaff(@RequestBody Staff staff) {
        try {
            // check manager id is manager
            Optional<AppUser> manager = appUserRepository.findById(staff.getManagerDetails().getId());
            if (manager.isPresent()) {
                if (manager.get().getUserRole() == UserRole.STAFF) {
                    throw new ManagerDoesNotExistException("Manager does not exist");
                }
            }
            return staffRepository.save(staff);
        } catch (ManagerDoesNotExistException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getLocalizedMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        }
    }

    @PutMapping("/update")
    public Staff update(@RequestBody Staff staff) {
        Staff staffToUpdate = staffRepository.getById(staff.getId());

        staffToUpdate.getUserDetails().setFirstname(staff.getUserDetails().getFirstname());
        staffToUpdate.getUserDetails().setSurname(staff.getUserDetails().getSurname());

        return staffRepository.save(staffToUpdate);
    }
}

class ManagerDoesNotExistException extends Exception {
    public ManagerDoesNotExistException(String value) {
        super(value);
    }
}