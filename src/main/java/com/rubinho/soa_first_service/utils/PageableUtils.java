package com.rubinho.soa_first_service.utils;

import com.rubinho.soa_first_service.exceptions.SortException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


@Component
public class PageableUtils {
    List<String> ALLOWED_SORT_FIELDS = Arrays.asList(
            "id", "name", "coordinates.x", "coordinates.y",
            "creationDate", "enginePower", "type", "fuelType"
    );

    public Pageable getPageable(int page, int pageSize, String sort) {
        if (page < 0 || pageSize < 0) {
            if (StringUtils.isBlank(sort)) {
                return Pageable.unpaged();
            }
            return Pageable.unpaged(Sort.by(parseSortParameter(sort)));
        }
        if (StringUtils.isBlank(sort)) {
            return PageRequest.of(page, pageSize);
        }
        return PageRequest.of(page, pageSize, Sort.by(parseSortParameter(sort)));

    }

    private List<Sort.Order> parseSortParameter(String sortParam) {
        return Arrays.stream(sortParam.split(","))
                .map(String::trim)
                .filter(StringUtils::isNotBlank)
                .map(this::parseSortOrder)
                .toList();
    }

    private Sort.Order parseSortOrder(String fieldExpression) {
        String direction = "+";
        String fieldName = fieldExpression;

        if (fieldExpression.startsWith("+") || fieldExpression.startsWith("-")) {
            direction = fieldExpression.substring(0, 1);
            fieldName = fieldExpression.substring(1).trim();
        }

        if (!ALLOWED_SORT_FIELDS.contains(fieldName)) {
            throw new SortException(fieldName);
        }

        final Sort.Direction sortDirection = direction.equals("-") ? Sort.Direction.DESC : Sort.Direction.ASC;

        return new Sort.Order(sortDirection, mapFieldName(fieldName));
    }

    private String mapFieldName(String fieldName) {
        if (fieldName.equals("coordinates.x")) {
            return "x";
        }

        if (fieldName.equals("coordinates.y")) {
            return "y";
        }

        return fieldName;
    }
}
