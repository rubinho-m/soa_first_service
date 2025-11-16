package com.rubinho.soa_first_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class SortException extends ResponseStatusException {
    public SortException(String fieldName) {
        super(HttpStatus.BAD_REQUEST, "Invalid sort field: " + fieldName);
    }
}
