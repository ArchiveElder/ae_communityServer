package com.ae.community.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class ExceptionController {
    // 커스텀 에러
    @ExceptionHandler(chaebbiException.class)
    public ResponseEntity<ErrorResponse> handleException(
            chaebbiException e, HttpServletRequest request
    ) {
        log.error("code : {}, url : {}, message : {}",
                e.getCode(), request.getRequestURI(), e.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(e.getCode())
                .message(e.getMessage())
                .build();
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
