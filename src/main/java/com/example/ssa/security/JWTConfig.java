package com.example.ssa.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JWTConfig {
    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";

    public static final Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());

    public static Date getAccessTokenExpiry() {
        // 15 minutes
        return new Date(System.currentTimeMillis() + 15 * 60 * 1000);
    }

    public static Date getRefreshTokenExpiry() {
        // 4 weeks
        return new Date(System.currentTimeMillis() + 40320L * 60 * 1000);
    }

    public static String encodeAccessJWT(String username, String URL, List<String> roles) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(getAccessTokenExpiry())
                .withIssuer(URL)
                .withClaim("roles", roles)
                .sign(algorithm);
    }

    public static String encodeRefreshJWT(String username, String URL) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(getRefreshTokenExpiry())
                .withIssuer(URL)
                .sign(algorithm);
    }

    public static DecodedJWT decodeJWT(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    public static Map<String, String> responseTokens(String accessToken, String refreshToken) {
        Map<String, String> tokens = new HashMap<>();
        tokens.put(ACCESS_TOKEN, accessToken);
        tokens.put(REFRESH_TOKEN, refreshToken);
        return tokens;
    }
}
