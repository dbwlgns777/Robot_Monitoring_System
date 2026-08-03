package com.prima.factory.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

class GlobalExceptionHandlerTest {

    @Test
    void preservesResponseStatusInsteadOfReturningInternalServerError() {
        var response = new GlobalExceptionHandler().responseStatus(
            new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 실패"));

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("로그인 실패", response.getBody().message());
    }
}
