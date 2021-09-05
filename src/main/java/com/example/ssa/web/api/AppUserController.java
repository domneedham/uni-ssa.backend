package com.example.ssa.web.api;

import com.example.ssa.entity.user.AppUser;
import com.example.ssa.service.AppUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.ssa.constants.HttpMapping.APP_USER_MAPPING;

@Slf4j
@RequestMapping(APP_USER_MAPPING)
@RestController
public class AppUserController {
    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/")
    public List<AppUser> findAll() {
        log.info(String.format("%s /", getClass().getName()));
        return appUserService.findAllAppUsers();
    }

    @GetMapping("/{id}")
    public AppUser findById(@PathVariable(value = "id") Long id) {
        log.info(String.format("%s /{id}", getClass().getName()));
        return appUserService.findAppUserById(id);
    }

    @GetMapping("/email/{email}")
    public AppUser findByEmail(@PathVariable(value = "email") String email) {
        log.info(String.format("%s /email/{email}", getClass().getName()));
        return appUserService.findByEmail(email);
    }
}
