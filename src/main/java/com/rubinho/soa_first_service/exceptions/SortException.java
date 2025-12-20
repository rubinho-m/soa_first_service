package com.rubinho.soa_first_service.exceptions;

import org.springframework.http.HttpStatus;

public class SortException extends CustomResponseStatusException {
    public SortException(String fieldName) {
        super(HttpStatus.BAD_REQUEST, "Invalid sort field: " + fieldName);
    }
}
