package com.sense.mos.infrastructure.exceptions.base;

import com.sense.mos.common.ErrorCode;

public class ConflictBaseException extends BaseException {

    public ConflictBaseException(ErrorCode errorCode, String message, Object... args) {
        super(errorCode, message, args);
    }
}
