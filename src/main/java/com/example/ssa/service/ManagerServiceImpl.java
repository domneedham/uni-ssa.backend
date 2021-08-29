package com.example.ssa.service;

import com.example.ssa.entity.user.Manager;
import com.example.ssa.exceptions.requests.bad.ManagerDoesNotExistException;
import com.example.ssa.repository.ManagerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * A range of methods to handle Manager CRUD operations.
 */
@Service
public class ManagerServiceImpl implements ManagerService {
    /**
     * The manager repository created by Spring.
     */
    private final ManagerRepository managerRepository;

    public ManagerServiceImpl(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    /**
     * Finds all managers that exist.
     * @return A list of managers.
     */
    @Override
    public List<Manager> findAllManagers() {
        return managerRepository.findAll();
    }

    /**
     * Find the manager with the given id.
     * @param id The id of the manager (app user).
     * @return The manager if found.
     * @throws ManagerDoesNotExistException If the manager does not exist.
     */
    @Override
    public Manager findManagerById(Long id) throws ManagerDoesNotExistException {
        Optional<Manager> manager = managerRepository.findById(id);

        if (manager.isEmpty()) {
            throw new ManagerDoesNotExistException("Manager not found with that id");
        }

        return manager.get();
    }

    /**
     * Find the manager by the name given. Does not have to be a full exact match, a partial match will suffice.
     * @param name The name (or partial name) of the manager.
     * @return A list of managers who match the name.
     */
    @Override
    public List<Manager> findManagersByName(String name) {
        return managerRepository.findAllByUserDetailsNameContainingIgnoreCase(name);
    }

    /**
     * Creates a new manager.
     * @param manager The manager to create.
     * @return The created manager.
     */
    @Override
    public Manager createManager(Manager manager) {
        return managerRepository.save(manager);
    }

    /**
     * Finds a manager by their email (username).
     * @param email The email of the manager.
     * @return The found manager.
     * @throws ManagerDoesNotExistException If a manager is not found.
     */
    @Override
    public Manager findManagerByEmail(String email) throws ManagerDoesNotExistException {
        Optional<Manager> manager = managerRepository.findByUserDetailsEmail(email);

        if (manager.isEmpty()) {
            throw new ManagerDoesNotExistException("Manager not found with that email");
        }

        return manager.get();
    }
}
