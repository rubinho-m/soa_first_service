package com.rubinho.soa_first_service.mappers.impl;

import com.rubinho.soa_first_service.dto.CoordinatesDto;
import com.rubinho.soa_first_service.dto.request.VehicleRequestDto;
import com.rubinho.soa_first_service.dto.response.VehicleResponseDto;
import com.rubinho.soa_first_service.exceptions.UnprocessableEntityException;
import com.rubinho.soa_first_service.mappers.VehicleMapper;
import com.rubinho.soa_first_service.model.FuelType;
import com.rubinho.soa_first_service.model.Vehicle;
import com.rubinho.soa_first_service.model.VehicleType;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class VehicleMapperImpl implements VehicleMapper {
    @Override
    public Vehicle toEntity(VehicleRequestDto requestDto) {
        return Vehicle.builder()
                .name(requestDto.getName())
                .x(requestDto.getCoordinates().getX())
                .y(requestDto.getCoordinates().getY())
                .enginePower(requestDto.getEnginePower())
                .type(mapToEnum(requestDto.getType(), VehicleType.class, "vehicle type"))
                .fuelType(mapToEnum(requestDto.getFuelType(), FuelType.class, "fuel type"))
                .build();
    }

    @Override
    public VehicleResponseDto toResponseDto(Vehicle vehicle) {
        return VehicleResponseDto.builder()
                .id(vehicle.getId().intValue())
                .name(vehicle.getName())
                .coordinates(
                        CoordinatesDto.builder()
                                .x(vehicle.getX())
                                .y(vehicle.getY())
                                .build()
                )
                .creationDate(vehicle.getCreationDate())
                .enginePower(vehicle.getEnginePower())
                .type(vehicle.getType())
                .fuelType(vehicle.getFuelType())
                .build();
    }

    private <T extends Enum<T>> T mapToEnum(String value, Class<T> enumClass, String typeName) {
        try {
            return Enum.valueOf(enumClass, value.toUpperCase());
        } catch (Exception e) {
            throw new UnprocessableEntityException("Invalid %s. Allowed values: %s"
                    .formatted(
                            typeName,
                            Arrays.stream(enumClass.getEnumConstants())
                                    .map(Enum::toString)
                                    .collect(Collectors.toSet())
                    )
            );
        }
    }
}
