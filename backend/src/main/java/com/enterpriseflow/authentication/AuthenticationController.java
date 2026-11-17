package com.enterpriseflow.authentication;

import com.enterpriseflow.authentication.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService service;
    public AuthenticationController(AuthenticationService service) { this.service = service; }
    @PostMapping("/register") @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) { return service.register(request); }
    @PostMapping("/login") public AuthResponse login(@Valid @RequestBody LoginRequest request) { return service.login(request); }
    @PostMapping("/refresh") public AuthResponse refresh(@Valid @RequestBody RefreshRequest request) { return service.refresh(request); }
}
