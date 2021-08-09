package com.example.ssa.exceptions.requests.bad;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SkillDoesNotExistException extends RuntimeException {
    public SkillDoesNotExistException(String value) {
        super(value);
    }
}
