package com.rubinho.soa_first_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class AllVehiclePageableResponseDto {
    private List<VehicleResponseDto> vehicles;

    private int totalPages;

    private int page;

    private int pageSize;
}
