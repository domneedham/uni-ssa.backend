package com.example.ssa.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class used for working with the JWT library.
 */
public class JWTConfig {
    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";

    public static final Algorithm algorithm = Algorithm.HMAC256("aBg3hyL1TVDge6ys".getBytes());

    /**
     * Get a new expiry for an access JWT. Set to 5 minutes.
     * @return A Date in 5 minutes time.
     */
    public static Date getAccessTokenExpiry() {
        // 5 minutes
        return new Date(System.currentTimeMillis() + 40320L * 60 * 1000);
    }

    /**
     * Get a new expiry for a refresh JWT. Set to 4 weeks.
     * @return A Date in 4 weeks time.
     */
    public static Date getRefreshTokenExpiry() {
        // 4 weeks
        return new Date(System.currentTimeMillis() + 40320L * 60 * 1000);
    }

    /**
     * Check if the authorisation header contains a Bearer phrase.
     * @param header The authorisation header of the request.
     * @return Whether the header is valid or not.
     */
    public static boolean isAuthHeaderValid(String header) {
        return header != null && header.startsWith("Bearer ");
    }

    /**
     * Get the JWT from the authorisation header.
     * @param header The authorisation header of the request.
     * @return The JWT.
     */
    public static String getTokenFromHeader(String header) {
        return header.substring("Bearer ".length());
    }

    /**
     * Encode an access JWT.
     * @param username The username of the user. This is their email.
     * @param URL The URL of the request.
     * @param roles The authority roles of the user.
     * @return The encoded access JWT.
     */
    public static String encodeAccessJWT(String username, String URL, List<String> roles) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(getAccessTokenExpiry())
                .withIssuer(URL)
                .withClaim("roles", roles)
                .sign(algorithm);
    }

    /**
     * Encode a refresh JWT.
     * @param username The username of the user. This is their email.
     * @param URL The URL of the request.
     * @return The encoded refresh JWT.
     */
    public static String encodeRefreshJWT(String username, String URL) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(getRefreshTokenExpiry())
                .withIssuer(URL)
                .sign(algorithm);
    }

    /**
     * Decode a JWT using the JWT library.
     * @param token The token from the request.
     * @return The decoded JWT.
     */
    public static DecodedJWT decodeJWT(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    /**
     * Create a new map of the tokens to send back in the response.
     * @param accessToken The access JWT to send back.
     * @param refreshToken The refresh JWT to send back.
     * @return A map of the two tokens.
     */
    public static Map<String, String> responseTokens(String accessToken, String refreshToken) {
        Map<String, String> tokens = new HashMap<>();
        tokens.put(ACCESS_TOKEN, accessToken);
        tokens.put(REFRESH_TOKEN, refreshToken);
        return tokens;
    }
}
