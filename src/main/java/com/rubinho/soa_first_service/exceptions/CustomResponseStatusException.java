package com.rubinho.soa_first_service.exceptions;

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public abstract class CustomResponseStatusException extends ResponseStatusException {
    private final String reason;

    public CustomResponseStatusException(HttpStatus httpStatus, String reason) {
        super(httpStatus, reason);
        this.reason = reason;
    }

    @Override
    @NonNull
    public String getMessage() {
        return reason;
    }
}
