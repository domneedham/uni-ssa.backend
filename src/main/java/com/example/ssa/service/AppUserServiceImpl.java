package com.example.ssa.service;

import com.example.ssa.entity.user.AppUser;
import com.example.ssa.exceptions.requests.bad.AppUserDoesNotExistException;
import com.example.ssa.repository.AppUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * A range of methods to handle AppUser CRUD operations.
 */
@Slf4j
@Service
public class AppUserServiceImpl implements AppUserService, UserDetailsService {

    /**
     * The app user repository created by Spring.
     */
    private final AppUserRepository appUserRepository;

    /**
     * The password encoder used for hashing passwords.
     */
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AppUserServiceImpl(AppUserRepository appUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.appUserRepository = appUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * Finds all app users that exist.
     * @return A list of app users.
     */
    @Override
    public List<AppUser> findAllAppUsers() {
        return appUserRepository.findAll();
    }

    /**
     * Finds the app user with the given id.
     * @param id The id of the app user.
     * @return The app user found.
     * @throws AppUserDoesNotExistException If the app user does not exist.
     */
    @Override
    public AppUser findAppUserById(Long id) throws AppUserDoesNotExistException {
        Optional<AppUser> appUser = appUserRepository.findById(id);

        if (appUser.isEmpty()) {
            log.error(String.format("User not found with id of %d", id));
            throw new AppUserDoesNotExistException("User not found with that id");
        }

        return appUser.get();
    }

    /**
     * Creates an app user. Hashes the password before saving the password encoder.
     * @param appUser The app user to create.
     * @return The created app user.
     */
    @Override
    public AppUser createAppUser(AppUser appUser) {
        appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));
        return appUserRepository.save(appUser);
    }

    /**
     * Finds an app user by their email address.
     * @param email The email of the app user.
     * @return The app user who has the email address provided.
     */
    @Override
    public AppUser findByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }

    /**
     * Loads a user, including their roles by their username (email).
     * @param username The email address of the user.
     * @return The user details Spring requires for authentication and authorisation.
     * @throws UsernameNotFoundException If the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByEmail(username);

        if (user == null) {
            log.error(String.format("User not found with username of %s", username));
            throw new UsernameNotFoundException("User not found");
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getUserRole().name()));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
