package com.prima.factory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ZES_ProfileUpdateRequest(
    @JsonProperty("name") @NotBlank String ZES_name,
    @JsonProperty("factoryId") Long ZES_factoryId,
    @JsonProperty("department") String ZES_department,
    @JsonProperty("position") String ZES_position,
    @JsonProperty("phone") String ZES_phone,
    @JsonProperty("email") @NotBlank @Email String ZES_email)
{
}
