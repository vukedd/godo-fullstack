package com.app.godo.utils;

import com.app.godo.services.auth.JwtService;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Utils {

    private final JwtDecoder jwtDecoder;

    public String extractToken(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header");
        }

        return header.substring(7);
    }

    public String extractSubject(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        return jwt.getSubject();
    }

    public String extractRole(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        return jwt.getClaim("role").toString();
    }

}
