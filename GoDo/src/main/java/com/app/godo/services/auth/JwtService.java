package com.app.godo.services.auth;

import com.app.godo.models.Administrator;
import com.app.godo.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Duration;
import java.time.Instant;

@RequiredArgsConstructor
public class JwtService {

    private final String issuer;
    private final Duration ttl;
    private final JwtEncoder jwtEncoder;

    public String generateToken(final User user) {
        String role = user.getRole();
        final var claimsSet = JwtClaimsSet.builder()
                .subject(user.getUsername())
                .issuer(issuer)
                .expiresAt(Instant.now().plus(ttl))
                .claim("role", role)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet))
                .getTokenValue();
    }

}