package com.rubinho.soa_first_service.rest;

import com.rubinho.soa_first_service.dto.request.VehicleRequestDto;
import com.rubinho.soa_first_service.dto.response.AllVehiclePageableResponseDto;
import com.rubinho.soa_first_service.dto.response.AverageEnginePowerResponseDto;
import com.rubinho.soa_first_service.dto.response.VehicleResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api/v1/vehicles")
public interface VehicleApi {
    @GetMapping
    ResponseEntity<AllVehiclePageableResponseDto> getAllVehicles(
            @RequestParam(defaultValue = "-1") int page,
            @RequestParam(defaultValue = "-1") int pageSize,
            @RequestParam(defaultValue = "") String sort,
            @RequestParam(defaultValue = "") String filter
    );

    @GetMapping("/engine-powers/average")
    ResponseEntity<AverageEnginePowerResponseDto> getAverageEnginePower();

    @GetMapping("/coordinates/max")
    ResponseEntity<VehicleResponseDto> getRandomWithMaxCoordinates();

    @GetMapping("/{id}")
    ResponseEntity<VehicleResponseDto> getVehicle(@PathVariable Long id);

    @PostMapping
    ResponseEntity<VehicleResponseDto> createVehicle(@RequestBody @Valid VehicleRequestDto vehicleRequestDto);

    @PutMapping("/{id}")
    ResponseEntity<VehicleResponseDto> updateVehicle(@PathVariable Long id, @RequestBody @Valid VehicleRequestDto vehicleRequestDto);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteVehicle(@PathVariable Long id);
}
