package com.rubinho.soa_first_service.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends CustomResponseStatusException {
    public NotFoundException(String reason) {
        super(HttpStatus.NOT_FOUND, reason);
    }
}
