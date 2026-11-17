package com.enterpriseflow.authentication.dto;

import jakarta.validation.constraints.*;

public record RegisterRequest(@NotBlank @Size(max = 120) String organisationName,
                              @NotBlank @Email @Size(max = 160) String email,
                              @NotBlank @Size(min = 12, max = 72) String password,
                              @NotBlank @Size(max = 80) String displayName) {}
