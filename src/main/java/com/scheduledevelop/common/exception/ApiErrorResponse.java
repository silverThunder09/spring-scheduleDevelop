package com.scheduledevelop.common.exception;

import lombok.Getter;

@Getter
public class ApiErrorResponse {

    private final int status;
    private final String message;

    public ApiErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ApiErrorResponse of(int status, String message) {
        return new ApiErrorResponse(status, message);
    }
}
