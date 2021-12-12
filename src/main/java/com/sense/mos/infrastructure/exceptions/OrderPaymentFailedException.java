package com.sense.mos.infrastructure.exceptions;

import com.sense.mos.common.ErrorCode;
import com.sense.mos.infrastructure.exceptions.base.ConflictBaseException;

public class OrderPaymentFailedException extends ConflictBaseException {

    public OrderPaymentFailedException(ErrorCode code, String message, Object... args) {
        super(code, message, args);
    }
}
