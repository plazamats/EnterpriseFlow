package com.enterpriseflow.security;

import com.enterpriseflow.user.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final Key key;
    private final Duration accessTtl;
    public JwtService(@Value("${security.jwt.secret}") String secret,
                      @Value("${security.jwt.access-ttl}") Duration accessTtl) {
        if (secret.getBytes(StandardCharsets.UTF_8).length < 32) throw new IllegalArgumentException("JWT secret must be at least 256 bits");
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); this.accessTtl = accessTtl;
    }
    public String issue(User user) {
        Instant now = Instant.now();
        return Jwts.builder().subject(user.getId().toString()).claim("org", user.getOrganisation().getId().toString())
                .claim("role", user.getRole().name()).issuedAt(Date.from(now)).expiration(Date.from(now.plus(accessTtl)))
                .signWith(key).compact();
    }
    public UUID subject(String token) {
        return UUID.fromString(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject());
    }
}
