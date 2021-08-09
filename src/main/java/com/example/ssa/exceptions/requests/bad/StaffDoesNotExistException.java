package com.example.ssa.exceptions.requests.bad;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StaffDoesNotExistException extends RuntimeException {
    public StaffDoesNotExistException(String value) { super(value); }
}
