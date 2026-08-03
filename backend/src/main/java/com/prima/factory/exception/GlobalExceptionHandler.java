package com.prima.factory.exception;

import java.util.Map;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.prima.factory.dto.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<?> validation(MethodArgumentNotValidException exception) {
        var errors = exception.getFieldErrors().stream()
            .map(error -> Map.of(
                "field", error.getField(),
                "message", Objects.toString(error.getDefaultMessage(), "invalid")))
            .toList();
        return ResponseEntity.badRequest()
            .body(new ApiResponse<>(false, errors, "Validation failed"));
    }

    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<ApiResponse<Void>> responseStatus(ResponseStatusException exception) {
        return ResponseEntity.status(exception.getStatusCode())
            .body(ApiResponse.error(Objects.toString(exception.getReason(), "요청을 처리하지 못했습니다.")));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiResponse<Void>> error(Exception exception) {
        return ResponseEntity.internalServerError()
            .body(ApiResponse.error("요청을 처리하지 못했습니다."));
    }
}
