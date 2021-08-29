package com.example.ssa.service;

import com.example.ssa.entity.user.Staff;

import java.util.List;

public interface StaffService {
    List<Staff> findAllStaff();
    Staff findStaffById(Long id);
    List<Staff> findStaffByName(String name);
    Staff findStaffByEmail(String email);
    Staff createStaff(Staff staff);
    Staff updateStaff(Staff staff);
}
