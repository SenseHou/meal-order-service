package com.sense.mos.configuration;

import com.sense.mos.common.ErrorCode;
import com.sense.mos.common.ErrorResponse;
import com.sense.mos.infrastructure.exceptions.base.ConflictBaseException;
import com.sense.mos.infrastructure.exceptions.base.ObjectNoFundBaseException;
import com.sense.mos.infrastructure.exceptions.base.ServiceUnavailableBaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConflictBaseException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleOrderCreationException(ConflictBaseException e) {
        return logAndResponse(e.getErrorCode(), e);
    }

    @ExceptionHandler(ServiceUnavailableBaseException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorResponse handleOrderCreationException(ServiceUnavailableBaseException e) {
        return logAndResponse(e.getErrorCode(), e);
    }

    @ExceptionHandler(ObjectNoFundBaseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleOrderCreationException(ObjectNoFundBaseException e) {
        return logAndResponse(e.getErrorCode(), e);
    }

    private ErrorResponse logAndResponse(ErrorCode code, Exception ex) {
        return ErrorResponse.of(code, ex.getMessage());
    }
}
