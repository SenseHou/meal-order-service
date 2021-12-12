package com.sense.mos.infrastructure.exceptions;

import com.sense.mos.common.ErrorCode;
import com.sense.mos.infrastructure.exceptions.base.ConflictBaseException;

public class OrderCancelFailedException extends ConflictBaseException {
    public OrderCancelFailedException(ErrorCode errorCode, String message, Object... args) {
        super(errorCode, message, args);
    }
}
