package com.sense.mos.common;

import lombok.Value;

@Value
public class ErrorResponse {
    private ErrorCode code;
    private String message;

    public static ErrorResponse of(ErrorCode code, String message) {
        return new ErrorResponse(code, message);
    }
}
