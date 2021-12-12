package com.sense.mos.infrastructure.exceptions;

import com.sense.mos.common.ErrorCode;
import com.sense.mos.infrastructure.exceptions.base.ServiceUnavailableBaseException;

public class PaymentServiceUnavailableException extends ServiceUnavailableBaseException {

    public PaymentServiceUnavailableException(ErrorCode code, String message, Object... args) {
        super(code, message, args);
    }
}
