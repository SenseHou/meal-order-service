package com.sense.mos.infrastructure.exceptions.base;

import com.sense.mos.common.ErrorCode;
import lombok.Getter;

import static java.lang.String.format;

@Getter
public abstract class BaseException extends RuntimeException {

    private final ErrorCode errorCode;

    public BaseException(ErrorCode errorCode, String message, Object... args) {
        super(format(message, args));
        this.errorCode = errorCode;
    }
}
