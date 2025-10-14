package com.rubinho.soa_first_service.dto.response;

import com.rubinho.soa_first_service.dto.CoordinatesDto;
import com.rubinho.soa_first_service.model.FuelType;
import com.rubinho.soa_first_service.model.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
public class VehicleResponseDto {
    private Integer id;

    private String name;

    private CoordinatesDto coordinates;

    private LocalDate creationDate;

    private Float enginePower;

    private VehicleType type;

    private FuelType fuelType;
}
