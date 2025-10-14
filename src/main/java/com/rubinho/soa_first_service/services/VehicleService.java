package com.rubinho.soa_first_service.services;

import com.rubinho.soa_first_service.model.Vehicle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

public interface VehicleService {
    Vehicle create(Vehicle vehicle);

    AllVehicles getAll(Pageable pageable, Specification<Vehicle> specification);

    Optional<Vehicle> findById(Long id);

    Optional<Vehicle> update(Long id, Vehicle vehicle);

    boolean delete(Long id);

    OptionalDouble getAverageEnginePower();

    Optional<Vehicle> findWithMaxCoordinates();

    record AllVehicles(List<Vehicle> vehicles, int totalPages){}
}
