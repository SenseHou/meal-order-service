package com.sense.mos.infrastructure.exceptions.base;

import com.sense.mos.common.ErrorCode;

public class ObjectNoFundBaseException extends BaseException {

    public ObjectNoFundBaseException(ErrorCode errorCode, String message, Object... args) {
        super(errorCode, message, args);
    }
}
