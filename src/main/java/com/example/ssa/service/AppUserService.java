package com.example.ssa.service;

import com.example.ssa.entity.user.AppUser;

import java.util.List;

public interface AppUserService {
    List<AppUser> findAllAppUsers();
    AppUser findAppUserById(Long id);
    AppUser createAppUser(AppUser appUser);
    AppUser findByEmail(String email);
}
