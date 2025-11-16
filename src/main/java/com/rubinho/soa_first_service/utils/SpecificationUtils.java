package com.rubinho.soa_first_service.utils;

import com.rubinho.soa_first_service.exceptions.FilterException;
import com.rubinho.soa_first_service.model.Vehicle;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class SpecificationUtils {
    public Specification<Vehicle> buildSpecification(String filterStr) {
        final List<String> filters = Arrays.stream(filterStr.split(","))
                .map(String::trim)
                .filter(StringUtils::isNotBlank)
                .toList();
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            for (FilterCriteria filter : buildFilters(filters)) {
                Predicate predicate = buildPredicate(root, criteriaBuilder, filter);
                if (predicate != null) {
                    predicates.add(predicate);
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Predicate buildPredicate(Root<Vehicle> root, CriteriaBuilder cb, FilterCriteria filter) {
        String field = filter.getField();
        String operation = filter.getOperation();
        String value = filter.getValue();

        return switch (field) {
            case "id" -> buildNumberPredicate(root.get("id"), cb, operation, Long.valueOf(value));
            case "name" -> buildStringPredicate(root.get("name"), cb, operation, value);
            case "coordinates.x" ->
                    buildNumberPredicate(root.get("x"), cb, operation, Double.valueOf(value));
            case "coordinates.y" ->
                    buildNumberPredicate(root.get("y"), cb, operation, Double.valueOf(value));
            case "creationDate" -> buildDatePredicate(root.get("creationDate"), cb, operation, LocalDate.parse(value));
            case "enginePower" -> buildNumberPredicate(root.get("enginePower"), cb, operation, Double.valueOf(value));
            case "type" -> buildEnumPredicate(root.get("type"), cb, operation, value);
            case "fuelType" -> buildEnumPredicate(root.get("fuelType"), cb, operation, value);
            default -> throw new FilterException("Unknown field: " + field);
        };
    }

    private Predicate buildNumberPredicate(Path<Number> path, CriteriaBuilder cb, String operation, Number value) {
        return switch (operation) {
            case "==" -> cb.equal(path, value);
            case "!=" -> cb.notEqual(path, value);
            case ">" -> cb.gt(path, value);
            case ">=" -> cb.ge(path, value);
            case "<" -> cb.lt(path, value);
            case "<=" -> cb.le(path, value);
            default -> throw new FilterException("Unsupported operation: " + operation);
        };
    }

    private Predicate buildStringPredicate(Path<String> path, CriteriaBuilder cb, String operation, String value) {
        return switch (operation) {
            case "==" -> cb.equal(path, value);
            case "!=" -> cb.notEqual(path, value);
            case "~" -> cb.like(cb.lower(path), "%" + value.toLowerCase() + "%");
            default -> throw new FilterException("Unsupported operation for string: " + operation);
        };
    }

    private Predicate buildDatePredicate(Path<LocalDate> path, CriteriaBuilder cb, String operation, LocalDate value) {
        return switch (operation) {
            case "==" -> cb.equal(path, value);
            case "!=" -> cb.notEqual(path, value);
            case ">" -> cb.greaterThan(path, value);
            case ">=" -> cb.greaterThanOrEqualTo(path, value);
            case "<" -> cb.lessThan(path, value);
            case "<=" -> cb.lessThanOrEqualTo(path, value);
            default -> throw new FilterException("Unsupported operation for date: " + operation);
        };
    }

    private Predicate buildEnumPredicate(Path<?> path, CriteriaBuilder cb, String operation, String value) {
        return switch (operation) {
            case "==" -> cb.equal(path.as(String.class), value);
            case "!=" -> cb.notEqual(path.as(String.class), value);
            case ">" -> cb.greaterThan(path.as(String.class), value);
            case ">=" -> cb.greaterThanOrEqualTo(path.as(String.class), value);
            case "<" -> cb.lessThan(path.as(String.class), value);
            case "<=" -> cb.lessThanOrEqualTo(path.as(String.class), value);
            case "~" -> cb.like(cb.lower(path.as(String.class)), "%" + value.toLowerCase() + "%");
            default -> throw new FilterException("Unsupported operation for enum: " + operation);
        };
    }

    private List<FilterCriteria> buildFilters(List<String> filterParams) {
        if (filterParams == null || filterParams.isEmpty()) {
            return Collections.emptyList();
        }

        return filterParams.stream()
                .map(FilterCriteria::fromString)
                .toList();
    }
}
