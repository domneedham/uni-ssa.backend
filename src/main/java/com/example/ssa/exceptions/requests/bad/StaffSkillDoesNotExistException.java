package com.example.ssa.exceptions.requests.bad;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StaffSkillDoesNotExistException extends RuntimeException {
    public StaffSkillDoesNotExistException(String value) {
        super(value);
    }
}