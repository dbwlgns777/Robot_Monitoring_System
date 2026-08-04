package com.prima.factory.exception;

import java.util.Map;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.prima.factory.dto.ZES_ApiResponse;

@RestControllerAdvice
public class ZES_GlobalExceptionHandler
{
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<?> ZES_validation(MethodArgumentNotValidException ZES_exception)
    {
        var ZES_errors = ZES_exception.getFieldErrors().stream()
            .map(ZES_error -> Map.of(
                "field", ZES_error.getField(),
                "message", Objects.toString(ZES_error.getDefaultMessage(), "invalid")))
            .toList();
        return ResponseEntity.badRequest()
            .body(new ZES_ApiResponse<>(false, ZES_errors, "Validation failed"));
    }

    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<ZES_ApiResponse<Void>> ZES_responseStatus(ResponseStatusException ZES_exception)
    {
        return ResponseEntity.status(ZES_exception.getStatusCode())
            .body(ZES_ApiResponse.ZES_error(
                Objects.toString(ZES_exception.getReason(), "요청을 처리하지 못했습니다.")));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ZES_ApiResponse<Void>> ZES_error(Exception ZES_exception)
    {
        return ResponseEntity.internalServerError()
            .body(ZES_ApiResponse.ZES_error("요청을 처리하지 못했습니다."));
    }
}
