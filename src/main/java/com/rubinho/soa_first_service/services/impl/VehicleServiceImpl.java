package com.rubinho.soa_first_service.services.impl;

import com.rubinho.soa_first_service.model.Vehicle;
import com.rubinho.soa_first_service.repository.VehicleRepository;
import com.rubinho.soa_first_service.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.OptionalDouble;

@Service
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public Vehicle create(Vehicle vehicle) {
        return create(vehicle, LocalDate.now());
    }

    @Override
    public AllVehicles getAll(Pageable pageable, Specification<Vehicle> specification) {
        Page<Vehicle> vehicles = vehicleRepository.findAll(specification, pageable);
        return new AllVehicles(vehicles.getContent(), vehicles.getTotalPages());
    }

    @Override
    public Optional<Vehicle> findById(Long id) {
        return vehicleRepository.findById(id);
    }

    @Override
    public Optional<Vehicle> update(Long id, Vehicle vehicle) {
        vehicle.setId(id);
        return vehicleRepository.findById(id)
                .map(value -> create(vehicle, value.getCreationDate()));
    }

    @Override
    public boolean delete(Long id) {
        if (!vehicleRepository.existsById(id)) {
            return false;
        }
        vehicleRepository.deleteById(id);
        return true;
    }

    @Override
    public OptionalDouble getAverageEnginePower() {
        return vehicleRepository.findAll()
                .stream()
                .mapToDouble(Vehicle::getEnginePower)
                .average();
    }

    @Override
    public Optional<Vehicle> findWithMaxCoordinates() {
        return vehicleRepository.findAll().stream()
                .max(
                        (v1, v2) -> Double.compare(distance(v1.getX(), v1.getY()), distance(v2.getX(), v2.getY()))
                );
    }

    private Vehicle create(Vehicle vehicle, LocalDate creationDate) {
        if (creationDate != null) {
            vehicle.setCreationDate(creationDate);
        }
        return vehicleRepository.save(vehicle);
    }

    private double distance(int x, int y) {
        return Math.sqrt(x * x + y * y);
    }
}
