package com.example.ssa.web.api;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.ssa.entity.user.AppUser;
import com.example.ssa.security.JWTConfig;
import com.example.ssa.service.AppUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.example.ssa.constants.HttpMapping.AUTH_MAPPING;

@Slf4j
@RequestMapping(AUTH_MAPPING)
@RestController
public class AuthController {
    private final AppUserService appUserService;

    public AuthController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorisationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (JWTConfig.isAuthHeaderValid(authorisationHeader)) {
            try {
                log.info("Refreshing token");

                // decode token
                String token = JWTConfig.getTokenFromHeader(authorisationHeader);
                DecodedJWT decodedJWT = JWTConfig.decodeJWT(token);

                // get user from username
                String username = decodedJWT.getSubject();
                AppUser user = appUserService.findByEmail(username);

                // encode new access token
                List<String> roles = List.of(user.getUserRole().toString());
                String accessToken = JWTConfig.encodeAccessJWT(user.getEmail(), request.getRequestURL().toString(), roles);

                Map<String, String> tokens = JWTConfig.responseTokens(accessToken, token);

                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception e) {
                log.error("Error refreshing token: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                Map<String, String> error = new HashMap<>();
                error.put("error", e.getMessage());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}
