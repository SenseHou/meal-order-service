package com.sense.mos.infrastructure.exceptions.base;

import com.sense.mos.common.ErrorCode;

public class ServiceUnavailableBaseException extends BaseException {

    public ServiceUnavailableBaseException(ErrorCode errorCode, String message, Object... args) {
        super(errorCode, message, args);
    }
}
