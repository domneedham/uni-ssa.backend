package com.example.ssa.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.ssa.entity.user.AppUser;
import com.example.ssa.security.JWTConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A range of methods to handle Auth operations.
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    /**
     * The app user service created by Spring.
     */
    private final AppUserService appUserService;

    public AuthServiceImpl(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    /**
     *
     * @param request The request variable created for every request.
     * @param response The response variable created for every request.
     * @throws IOException If the refresh token is missing.
     */
    @Override
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
