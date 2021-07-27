package com.example.ssa.repository;

import com.example.ssa.entity.user.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
}
