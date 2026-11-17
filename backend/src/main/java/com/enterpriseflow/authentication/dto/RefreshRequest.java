package com.enterpriseflow.authentication.dto;
import jakarta.validation.constraints.NotBlank;
public record RefreshRequest(@NotBlank String refreshToken) {}
