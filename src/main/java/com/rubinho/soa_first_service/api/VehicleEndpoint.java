package com.rubinho.soa_first_service.api;

import com.rubinho.soa_first_service.exceptions.NotFoundException;
import com.rubinho.soa_first_service.generated.vehicles.CreateVehicleRequest;
import com.rubinho.soa_first_service.generated.vehicles.CreateVehicleResponse;
import com.rubinho.soa_first_service.generated.vehicles.DeleteVehicleRequest;
import com.rubinho.soa_first_service.generated.vehicles.DeleteVehicleResponse;
import com.rubinho.soa_first_service.generated.vehicles.GetAllVehiclesRequest;
import com.rubinho.soa_first_service.generated.vehicles.GetAllVehiclesResponse;
import com.rubinho.soa_first_service.generated.vehicles.GetAverageEnginePowerResponse;
import com.rubinho.soa_first_service.generated.vehicles.GetRandomWithMaxCoordinatesResponse;
import com.rubinho.soa_first_service.generated.vehicles.GetVehicleRequest;
import com.rubinho.soa_first_service.generated.vehicles.GetVehicleResponse;
import com.rubinho.soa_first_service.generated.vehicles.UpdateVehicleRequest;
import com.rubinho.soa_first_service.generated.vehicles.UpdateVehicleResponse;
import com.rubinho.soa_first_service.mappers.VehicleMapper;
import com.rubinho.soa_first_service.model.Vehicle;
import com.rubinho.soa_first_service.services.VehicleService;
import com.rubinho.soa_first_service.utils.PageableUtils;
import com.rubinho.soa_first_service.utils.SpecificationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;
import java.util.Objects;

@Endpoint
@Slf4j
public class VehicleEndpoint {
    private final VehicleService vehicleService;
    private final VehicleMapper vehicleMapper;
    private final PageableUtils pageableUtils;
    private final SpecificationUtils specificationUtils;

    private static final String NAMESPACE_URI = "https://com/rubinho/soa_first_service/generated/vehicles";

    @Autowired
    public VehicleEndpoint(VehicleService vehicleService,
                           VehicleMapper vehicleMapper,
                           PageableUtils pageableUtils,
                           SpecificationUtils specificationUtils) {
        this.vehicleService = vehicleService;
        this.vehicleMapper = vehicleMapper;
        this.pageableUtils = pageableUtils;
        this.specificationUtils = specificationUtils;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllVehiclesRequest")
    @ResponsePayload
    public GetAllVehiclesResponse getAllVehicles(@RequestPayload GetAllVehiclesRequest request) {
        final int page = Objects.requireNonNullElse(request.getPage(), -1);
        final int pageSize = Objects.requireNonNullElse(request.getPageSize(), -1);
        final String sort = Objects.requireNonNullElse(request.getSort(), "");
        final String filter = Objects.requireNonNullElse(request.getFilter(), "");


        final Pageable pageable = pageableUtils.getPageable(page, pageSize, sort);
        final VehicleService.AllVehicles vehicles = vehicleService.getAll(
                pageable,
                specificationUtils.buildSpecification(filter)
        );
        final List<Vehicle> vehicleList = vehicles.vehicles();
        final GetAllVehiclesResponse allVehiclePageableResponse = new GetAllVehiclesResponse();
        allVehiclePageableResponse.getVehicles().addAll(
                vehicleList.stream()
                        .map(vehicleMapper::toResponse)
                        .toList()
        );
        allVehiclePageableResponse.setPage(Math.max(page, 0));
        allVehiclePageableResponse.setTotalPages(vehicles.totalPages());
        allVehiclePageableResponse.setPageSize(pageable.isUnpaged() ? vehicleList.size() : pageable.getPageSize());
        return allVehiclePageableResponse;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAverageEnginePowerRequest")
    @ResponsePayload
    public GetAverageEnginePowerResponse getAverageEnginePower() {
        final float average = (float) vehicleService.getAverageEnginePower().orElseThrow(
                () -> new NotFoundException("No vehicles found")
        );
        final GetAverageEnginePowerResponse averageEnginePowerResponse = new GetAverageEnginePowerResponse();
        averageEnginePowerResponse.setAverageEnginePower(average);
        return averageEnginePowerResponse;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getRandomWithMaxCoordinatesRequest")
    @ResponsePayload
    public GetRandomWithMaxCoordinatesResponse getRandomWithMaxCoordinates() {
        final GetRandomWithMaxCoordinatesResponse response = new GetRandomWithMaxCoordinatesResponse();
        response.setVehicle(vehicleMapper.toResponse(
                vehicleService.findWithMaxCoordinates()
                        .orElseThrow(() -> new NotFoundException("No vehicles found"))
        ));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getVehicleRequest")
    @ResponsePayload
    public GetVehicleResponse getVehicle(@RequestPayload GetVehicleRequest request) {
        final Long id = request.getId();
        final GetVehicleResponse response = new GetVehicleResponse();
        response.setVehicle(vehicleMapper.toResponse(
                vehicleService.findById(id)
                        .orElseThrow(
                                () -> new NotFoundException("Vehicle with id %d not found".formatted(id))
                        )
        ));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createVehicleRequest")
    @ResponsePayload
    public CreateVehicleResponse createVehicle(@RequestPayload CreateVehicleRequest request) {
        log.info("vehicle x: {}", request.getVehicle().getCoordinates().getX());
        final CreateVehicleResponse createVehicleResponse = new CreateVehicleResponse();
        createVehicleResponse.setVehicle(vehicleMapper.toResponse(
                vehicleService.create(
                        vehicleMapper.toEntity(request.getVehicle())
                )
        ));
        return createVehicleResponse;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateVehicleRequest")
    @ResponsePayload
    public UpdateVehicleResponse updateVehicle(@RequestPayload UpdateVehicleRequest request) {
        final Long id = request.getId();
        final UpdateVehicleResponse updateVehicleResponse = new UpdateVehicleResponse();
        updateVehicleResponse.setVehicle(vehicleMapper.toResponse(
                vehicleService.update(id, vehicleMapper.toEntity(request.getVehicle()))
                        .orElseThrow(() -> new NotFoundException("Vehicle with id %d not found".formatted(id)))
        ));
        return updateVehicleResponse;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteVehicleRequest")
    @ResponsePayload
    public DeleteVehicleResponse deleteVehicle(@RequestPayload DeleteVehicleRequest request) {
        final Long id = request.getId();
        if (!vehicleService.delete(id)) {
            throw new NotFoundException("Vehicle with id %d not found".formatted(id));
        }
        return new DeleteVehicleResponse();
    }
}
