package com.rubinho.soa_first_service.dto.request;

import com.rubinho.soa_first_service.dto.CoordinatesDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class VehicleRequestDto {
    @NotBlank
    private String name;

    @NotNull
    @Valid
    private CoordinatesDto coordinates;

    @PositiveOrZero
    private Float enginePower;

    @NotBlank
    private String type;

    @NotBlank
    private String fuelType;
}
