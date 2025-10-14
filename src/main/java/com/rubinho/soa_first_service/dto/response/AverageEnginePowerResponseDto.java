package com.rubinho.soa_first_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class AverageEnginePowerResponseDto {
    private float average;
}
