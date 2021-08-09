package com.example.ssa.exceptions.requests.bad;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ManagerDoesNotExistException extends RuntimeException {
    public ManagerDoesNotExistException(String value) {
        super(value);
    }
}
