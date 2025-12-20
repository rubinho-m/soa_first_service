package com.rubinho.soa_first_service.exceptions;

import org.springframework.http.HttpStatus;

public class UnprocessableEntityException extends CustomResponseStatusException {
    public UnprocessableEntityException(String message) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, message);
    }
}
