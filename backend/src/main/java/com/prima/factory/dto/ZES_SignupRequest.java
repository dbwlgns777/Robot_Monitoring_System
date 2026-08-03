package com.prima.factory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ZES_SignupRequest(
    @JsonProperty("username") @NotBlank String ZES_username,
    @JsonProperty("name") @NotBlank String ZES_name,
    @JsonProperty("password") @Size(min = 8) String ZES_password,
    @JsonProperty("passwordConfirm") @NotBlank String ZES_passwordConfirm,
    @JsonProperty("factory") @NotBlank String ZES_factory,
    @JsonProperty("department") String ZES_department,
    @JsonProperty("position") String ZES_position,
    @JsonProperty("phone") String ZES_phone,
    @JsonProperty("email") @NotBlank @Email String ZES_email,
    @JsonProperty("requestedRole") @NotBlank String ZES_requestedRole)
{
}
