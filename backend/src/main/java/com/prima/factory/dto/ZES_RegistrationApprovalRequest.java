package com.prima.factory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record ZES_RegistrationApprovalRequest(
    @JsonProperty("roleCode") @NotBlank String ZES_roleCode)
{
}
