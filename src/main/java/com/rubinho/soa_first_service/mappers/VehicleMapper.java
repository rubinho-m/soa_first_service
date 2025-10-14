package com.rubinho.soa_first_service.mappers;

import com.rubinho.soa_first_service.dto.request.VehicleRequestDto;
import com.rubinho.soa_first_service.dto.response.VehicleResponseDto;
import com.rubinho.soa_first_service.model.Vehicle;

public interface VehicleMapper {
    Vehicle toEntity(VehicleRequestDto requestDto);

    VehicleResponseDto toResponseDto(Vehicle vehicle);
}
