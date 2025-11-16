package com.rubinho.soa_first_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FilterException extends ResponseStatusException {
    public FilterException(String message) {
        super(HttpStatus.BAD_REQUEST, "Error in filtering: " + message);
    }
}
