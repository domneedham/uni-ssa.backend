package com.example.ssa.repository;

import com.example.ssa.entity.user.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    List<Staff> findAllByUserDetailsNameContainingIgnoreCase(String name);
    Optional<Staff> findByUserDetailsEmail(String email);
}
