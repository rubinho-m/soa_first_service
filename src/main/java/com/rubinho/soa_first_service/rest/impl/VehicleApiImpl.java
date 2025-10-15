package com.rubinho.soa_first_service.rest.impl;

import com.rubinho.soa_first_service.dto.request.VehicleRequestDto;
import com.rubinho.soa_first_service.dto.response.AllVehiclePageableResponseDto;
import com.rubinho.soa_first_service.dto.response.AverageEnginePowerResponseDto;
import com.rubinho.soa_first_service.dto.response.VehicleResponseDto;
import com.rubinho.soa_first_service.exceptions.NotFoundException;
import com.rubinho.soa_first_service.mappers.VehicleMapper;
import com.rubinho.soa_first_service.model.Vehicle;
import com.rubinho.soa_first_service.rest.VehicleApi;
import com.rubinho.soa_first_service.services.VehicleService;
import com.rubinho.soa_first_service.utils.PageableUtils;
import com.rubinho.soa_first_service.utils.SpecificationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VehicleApiImpl implements VehicleApi {
    private final VehicleService vehicleService;
    private final VehicleMapper vehicleMapper;
    private final PageableUtils pageableUtils;
    private final SpecificationUtils specificationUtils;

    @Autowired
    public VehicleApiImpl(VehicleService vehicleService,
                          VehicleMapper vehicleMapper,
                          PageableUtils pageableUtils,
                          SpecificationUtils specificationUtils) {
        this.vehicleService = vehicleService;
        this.vehicleMapper = vehicleMapper;
        this.pageableUtils = pageableUtils;
        this.specificationUtils = specificationUtils;
    }

    @Override
    public ResponseEntity<AllVehiclePageableResponseDto> getAllVehicles(int page, int pageSize, String sort, String filter) {
        Pageable pageable = pageableUtils.getPageable(page, pageSize, sort);
        final VehicleService.AllVehicles vehicles = vehicleService.getAll(
                pageable,
                specificationUtils.buildSpecification(filter)
        );
        final List<Vehicle> vehicleList = vehicles.vehicles();
        return ResponseEntity.ok(
                AllVehiclePageableResponseDto.builder()
                        .vehicles(vehicleList.stream()
                                .map(vehicleMapper::toResponseDto)
                                .toList())
                        .page(Math.max(page, 0))
                        .totalPages(vehicles.totalPages())
                        .pageSize(pageable.isUnpaged() ? vehicleList.size() : pageable.getPageSize())
                        .build()
        );
    }

    @Override
    public ResponseEntity<AverageEnginePowerResponseDto> getAverageEnginePower() {
        final float average = (float) vehicleService.getAverageEnginePower().orElseThrow(
                () -> new NotFoundException("No vehicles found")
        );
        return ResponseEntity.ok(
                AverageEnginePowerResponseDto.builder()
                        .average(average)
                        .build()
        );
    }

    @Override
    public ResponseEntity<VehicleResponseDto> getRandomWithMaxCoordinates() {
        return ResponseEntity.ok(
                vehicleMapper.toResponseDto(
                        vehicleService.findWithMaxCoordinates()
                                .orElseThrow(() -> new NotFoundException("No vehicles found"))
                )
        );
    }

    @Override
    public ResponseEntity<VehicleResponseDto> getVehicle(Long id) {
        return ResponseEntity.ok(
                vehicleMapper.toResponseDto(
                        vehicleService.findById(id)
                                .orElseThrow(
                                        () -> new NotFoundException("Vehicle with id %d not found".formatted(id))
                                )
                )
        );
    }

    @Override
    public ResponseEntity<VehicleResponseDto> createVehicle(VehicleRequestDto vehicleRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        vehicleMapper.toResponseDto(
                                vehicleService.create(
                                        vehicleMapper.toEntity(vehicleRequestDto)
                                )
                        )
                );
    }

    @Override
    public ResponseEntity<VehicleResponseDto> updateVehicle(Long id, VehicleRequestDto vehicleRequestDto) {
        return ResponseEntity.ok(
                vehicleMapper.toResponseDto(
                        vehicleService.update(id, vehicleMapper.toEntity(vehicleRequestDto))
                                .orElseThrow(() -> new NotFoundException("Vehicle with id %d not found".formatted(id)))
                )
        );
    }

    @Override
    public ResponseEntity<Void> deleteVehicle(Long id) {
        if (!vehicleService.delete(id)) {
            throw new NotFoundException("Vehicle with id %d not found".formatted(id));
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
