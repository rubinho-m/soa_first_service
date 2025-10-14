package com.rubinho.soa_first_service.utils;

import lombok.Getter;

@Getter
public class FilterCriteria {
    private final String field;
    private final String operation;
    private final String value;

    private FilterCriteria(String field, String operation, String value) {
        this.field = field;
        this.operation = operation;
        this.value = value;
    }

    public static FilterCriteria fromString(String filter) {
        String[] parts = filter.split("(==|!=|>=|<=|>|<|~)");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid filter format: " + filter);
        }

        String field = parts[0];
        String value = parts[1];
        String operation = filter.substring(field.length(), filter.length() - value.length());

        return new FilterCriteria(field, operation, value);
    }
}