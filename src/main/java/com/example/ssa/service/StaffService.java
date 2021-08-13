package com.example.ssa.service;

import com.example.ssa.entity.user.Staff;

import java.util.List;
import java.util.Optional;

public interface StaffService {
    List<Staff> findAllStaff();
    Optional<Staff> findStaffById(Long id);
    List<Staff> findStaffByName(String name);
    Staff createStaff(Staff staff);
    Staff updateStaff(Staff staff);
}
