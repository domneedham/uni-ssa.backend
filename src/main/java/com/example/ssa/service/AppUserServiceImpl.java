package com.example.ssa.service;

import com.example.ssa.entity.user.AppUser;
import com.example.ssa.repository.AppUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository appUserRepository;

    public AppUserServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public List<AppUser> findAllAppUsers() {
        return appUserRepository.findAll();
    }

    @Override
    public Optional<AppUser> findAppUserById(Long id) {
        return appUserRepository.findById(id);
    }
}
