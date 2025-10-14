package com.rubinho.soa_first_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class CoordinatesDto {
    @NotNull
    private Integer x;

    @NotNull
    private Integer y;
}
