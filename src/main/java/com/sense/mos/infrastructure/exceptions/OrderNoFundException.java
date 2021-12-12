package com.sense.mos.infrastructure.exceptions;

import com.sense.mos.common.ErrorCode;
import com.sense.mos.infrastructure.exceptions.base.ObjectNoFundBaseException;

public class OrderNoFundException extends ObjectNoFundBaseException {

    public OrderNoFundException(ErrorCode code, String message, Object... args) {
        super(code, message, args);
    }
}
