package com.rubinho.soa_first_service.mappers;

import com.rubinho.soa_first_service.generated.vehicles.VehicleRequest;
import com.rubinho.soa_first_service.generated.vehicles.VehicleResponse;
import com.rubinho.soa_first_service.model.Vehicle;

public interface VehicleMapper {
    Vehicle toEntity(VehicleRequest vehicleRequest);

    VehicleResponse toResponse(Vehicle vehicle);
}
