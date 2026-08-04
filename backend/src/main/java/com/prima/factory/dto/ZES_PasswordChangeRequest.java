package com.prima.factory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ZES_PasswordChangeRequest(
    @JsonProperty("currentPassword") @NotBlank String ZES_currentPassword,
    @JsonProperty("newPassword") @NotBlank @Size(min = 8) String ZES_newPassword,
    @JsonProperty("newPasswordConfirm") @NotBlank String ZES_newPasswordConfirm)
{
}
