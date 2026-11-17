package com.enterpriseflow.authentication.dto;

import jakarta.validation.constraints.*;
public record LoginRequest(@NotBlank @Size(max = 120) String organisationName,
                           @NotBlank @Email String email, @NotBlank String password) {}
