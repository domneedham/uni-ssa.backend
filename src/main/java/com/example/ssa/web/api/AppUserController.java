package com.example.ssa.web.api;

import com.example.ssa.entity.user.AppUser;
import com.example.ssa.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("api/user")
@RestController
public class AppUserController {
    @Autowired
    private AppUserRepository appUserRepository;

    @GetMapping("/")
    public List<AppUser> findAll() {
        return appUserRepository.findAll();
    }

    @GetMapping("/{id}")
    public AppUser findById(@PathVariable(value = "id") Long id) {
        return appUserRepository.getById(id);
    }
}
