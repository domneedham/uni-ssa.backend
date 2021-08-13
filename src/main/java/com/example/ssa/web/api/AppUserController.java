package com.example.ssa.web.api;

import com.example.ssa.entity.user.AppUser;
import com.example.ssa.service.AppUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("api/user")
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
}
