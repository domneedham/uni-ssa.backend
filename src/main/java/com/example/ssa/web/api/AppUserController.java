package com.example.ssa.web.api;

import com.example.ssa.entity.user.AppUser;
import com.example.ssa.service.AppUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.example.ssa.constants.HttpMapping.APP_USER_MAPPING;

@RequestMapping(APP_USER_MAPPING)
@RestController
public class AppUserController {
    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/")
    public List<AppUser> findAll() {
        return appUserService.findAllAppUsers();
    }

    @GetMapping("/{id}")
    public Optional<AppUser> findById(@PathVariable(value = "id") Long id) {
        return appUserService.findAppUserById(id);
    }

    @GetMapping("/email/{email}")
    public AppUser findByEmail(@PathVariable(value = "email") String email) {
        return appUserService.findByEmail(email);
    }
}
