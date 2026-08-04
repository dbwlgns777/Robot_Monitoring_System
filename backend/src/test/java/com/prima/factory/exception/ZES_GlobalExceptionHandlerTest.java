package com.prima.factory.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

class ZES_GlobalExceptionHandlerTest
{
    @Test
    void ZES_preservesResponseStatusInsteadOfReturningInternalServerError()
    {
        var ZES_response = new ZES_GlobalExceptionHandler().ZES_responseStatus(
            new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 실패"));

        assertEquals(HttpStatus.UNAUTHORIZED, ZES_response.getStatusCode());
        assertEquals("로그인 실패", ZES_response.getBody().ZES_message());
    }
}
