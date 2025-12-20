package com.rubinho.soa_first_service.mappers.impl;

import com.rubinho.soa_first_service.exceptions.UnprocessableEntityException;
import com.rubinho.soa_first_service.generated.vehicles.Coordinates;
import com.rubinho.soa_first_service.generated.vehicles.VehicleRequest;
import com.rubinho.soa_first_service.generated.vehicles.VehicleResponse;
import com.rubinho.soa_first_service.mappers.VehicleMapper;
import com.rubinho.soa_first_service.model.FuelType;
import com.rubinho.soa_first_service.model.Vehicle;
import com.rubinho.soa_first_service.model.VehicleType;
import org.springframework.stereotype.Component;

import javax.xml.datatype.DatatypeFactory;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class VehicleMapperImpl implements VehicleMapper {
    @Override
    public Vehicle toEntity(VehicleRequest vehicleRequest) {
        return Vehicle.builder()
                .name(vehicleRequest.getName())
                .x(vehicleRequest.getCoordinates().getX())
                .y(vehicleRequest.getCoordinates().getY())
                .enginePower(vehicleRequest.getEnginePower())
                .type(mapToEnum(vehicleRequest.getType(), VehicleType.class, "vehicle type"))
                .fuelType(mapToEnum(vehicleRequest.getFuelType(), FuelType.class, "fuel type"))
                .build();
    }

    @Override
    public VehicleResponse toResponse(Vehicle vehicle) {
        final Coordinates coordinates = new Coordinates();
        coordinates.setX(vehicle.getX());
        coordinates.setY(vehicle.getY());

        final VehicleResponse vehicleResponse = new VehicleResponse();
        vehicleResponse.setId(vehicle.getId());
        vehicleResponse.setName(vehicle.getName());
        vehicleResponse.setCoordinates(coordinates);
        try {
            vehicleResponse.setCreationDate(
                    DatatypeFactory.newInstance()
                            .newXMLGregorianCalendar(vehicle.getCreationDate().toString())
            );
        } catch (Exception e) {
            throw new UnprocessableEntityException("Couldn't detect creation date: %s".formatted(vehicle.getCreationDate()));
        }
        vehicleResponse.setEnginePower(vehicle.getEnginePower());
        vehicleResponse.setType(vehicle.getType().toString());
        vehicleResponse.setFuelType(vehicle.getFuelType().toString());
        return vehicleResponse;
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
