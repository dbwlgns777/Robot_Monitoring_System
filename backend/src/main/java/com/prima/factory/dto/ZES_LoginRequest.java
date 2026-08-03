package com.prima.factory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record ZES_LoginRequest(
    @JsonProperty("username") @NotBlank String ZES_username,
    @JsonProperty("password") @NotBlank String ZES_password)
{
}
