package com.prima.factory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ZES_ApiResponse<T>(
    @JsonProperty("success") boolean ZES_success,
    @JsonProperty("data") T ZES_data,
    @JsonProperty("message") String ZES_message)
{
    public static <T> ZES_ApiResponse<T> ZES_ok(T ZES_data)
    {
        return new ZES_ApiResponse<>(true, ZES_data, null);
    }

    public static ZES_ApiResponse<Void> ZES_error(String ZES_message)
    {
        return new ZES_ApiResponse<>(false, null, ZES_message);
    }
}
