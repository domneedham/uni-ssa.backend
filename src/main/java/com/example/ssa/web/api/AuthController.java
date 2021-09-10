package com.example.ssa.web.api;

import com.example.ssa.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.ssa.constants.HttpMapping.AUTH_MAPPING;

@Slf4j
@RequestMapping(AUTH_MAPPING)
@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info(String.format("%s /token/refresh", getClass().getName()));
        authService.refreshToken(request, response);
    }
}
