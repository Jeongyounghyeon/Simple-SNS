package com.study.simple_sns.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SimpleSnsException extends RuntimeException{

    private ErrorCode errorCode;
    private String message;

    public SimpleSnsException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = null;
    }

    @Override
    public String getMessage() {
        return String.format("%s. %s", errorCode.getMessage(), message);
    }
}
