package com.ae.community.exception;

import lombok.Getter;

@Getter
public class chaebbiException extends RuntimeException{
    private final String code;
    private final String message;

    public chaebbiException(CodeAndMessage errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
}
