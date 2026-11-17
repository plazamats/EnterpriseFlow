package com.enterpriseflow.authentication;

import com.enterpriseflow.authentication.dto.*;
import com.enterpriseflow.organisation.*;
import com.enterpriseflow.security.JwtService;
import com.enterpriseflow.user.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthenticationService {
    private final UserRepository users; private final OrganisationRepository organisations; private final RefreshTokenRepository refreshTokens;
    private final PasswordEncoder encoder; private final JwtService jwt; private final Duration refreshTtl;
    public AuthenticationService(UserRepository users, OrganisationRepository organisations, RefreshTokenRepository refreshTokens,
                                 PasswordEncoder encoder, JwtService jwt, @Value("${security.jwt.refresh-ttl}") Duration refreshTtl) {
        this.users = users; this.organisations = organisations; this.refreshTokens = refreshTokens; this.encoder = encoder; this.jwt = jwt; this.refreshTtl = refreshTtl;
    }
    @Transactional public AuthResponse register(RegisterRequest request) {
        var organisation = new Organisation(UUID.randomUUID(), request.organisationName().trim());
        organisations.save(organisation);
        var user = users.save(new User(UUID.randomUUID(), organisation, request.email().trim(), encoder.encode(request.password()), request.displayName().trim(), Role.ADMIN));
        return tokens(user);
    }
    @Transactional public AuthResponse login(LoginRequest request) {
        var organisation = organisations.findByNameIgnoreCase(request.organisationName().trim())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
        var user = users.findByOrganisationIdAndEmail(organisation.getId(), request.email().toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
        if (!user.isEnabled()) throw new ResponseStatusException(HttpStatus.LOCKED, "Account is locked or disabled");
        if (!encoder.matches(request.password(), user.getPasswordHash())) {
            user.recordFailedLogin(); users.save(user);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        user.recordSuccessfulLogin(); return tokens(user);
    }
    @Transactional public AuthResponse refresh(RefreshRequest request) {
        var existing = refreshTokens.findByTokenHash(hash(request.refreshToken())).filter(RefreshToken::usable)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token"));
        existing.revoke(); return tokens(existing.getUser());
    }
    private AuthResponse tokens(User user) {
        String refresh = UUID.randomUUID().toString() + UUID.randomUUID();
        refreshTokens.save(new RefreshToken(UUID.randomUUID(), user, hash(refresh), Instant.now().plus(refreshTtl)));
        return new AuthResponse(jwt.issue(user), refresh, "Bearer", 900);
    }
    private String hash(String token) {
        try { return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(token.getBytes(StandardCharsets.UTF_8))); }
        catch (Exception e) { throw new IllegalStateException("Unable to hash token", e); }
    }
}
