package com.example.ssa.service;

import com.example.ssa.entity.user.AppUser;

import java.util.List;
import java.util.Optional;

public interface AppUserService {
    List<AppUser> findAllAppUsers();
    Optional<AppUser> findAppUserById(Long id);

}
