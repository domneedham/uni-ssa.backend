package com.example.ssa.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface AuthService {
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
