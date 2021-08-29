package com.example.ssa.exceptions.requests.bad;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AppUserDoesNotExistException extends RuntimeException {
    public AppUserDoesNotExistException(String value) {
        super(value);
    }
}
