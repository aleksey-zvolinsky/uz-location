package com.kerriline.location.service;

import com.kerriline.location.domain.LocationResponse;
import com.kerriline.location.repository.LocationResponseRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LocationResponse}.
 */
@Service
@Transactional
public class LocationResponseService {

    private final Logger log = LoggerFactory.getLogger(LocationResponseService.class);

    private final LocationResponseRepository locationResponseRepository;

    public LocationResponseService(LocationResponseRepository locationResponseRepository) {
        this.locationResponseRepository = locationResponseRepository;
    }

    /**
     * Save a locationResponse.
     *
     * @param locationResponse the entity to save.
     * @return the persisted entity.
     */
    public LocationResponse save(LocationResponse locationResponse) {
        log.debug("Request to save LocationResponse : {}", locationResponse);
        return locationResponseRepository.save(locationResponse);
    }

    /**
     * Partially update a locationResponse.
     *
     * @param locationResponse the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LocationResponse> partialUpdate(LocationResponse locationResponse) {
        log.debug("Request to partially update LocationResponse : {}", locationResponse);

        return locationResponseRepository
            .findById(locationResponse.getId())
            .map(
                existingLocationResponse -> {
                    if (locationResponse.getResponseDatetime() != null) {
                        existingLocationResponse.setResponseDatetime(locationResponse.getResponseDatetime());
                    }
                    if (locationResponse.getTankNumber() != null) {
                        existingLocationResponse.setTankNumber(locationResponse.getTankNumber());
                    }
                    if (locationResponse.getTankType() != null) {
                        existingLocationResponse.setTankType(locationResponse.getTankType());
                    }
                    if (locationResponse.getCargoId() != null) {
                        existingLocationResponse.setCargoId(locationResponse.getCargoId());
                    }
                    if (locationResponse.getCargoName() != null) {
                        existingLocationResponse.setCargoName(locationResponse.getCargoName());
                    }
                    if (locationResponse.getWeight() != null) {
                        existingLocationResponse.setWeight(locationResponse.getWeight());
                    }
                    if (locationResponse.getReceiverId() != null) {
                        existingLocationResponse.setReceiverId(locationResponse.getReceiverId());
                    }
                    if (locationResponse.getTankIndex() != null) {
                        existingLocationResponse.setTankIndex(locationResponse.getTankIndex());
                    }
                    if (locationResponse.getLocationStationId() != null) {
                        existingLocationResponse.setLocationStationId(locationResponse.getLocationStationId());
                    }
                    if (locationResponse.getLocationStationName() != null) {
                        existingLocationResponse.setLocationStationName(locationResponse.getLocationStationName());
                    }
                    if (locationResponse.getLocationDatetime() != null) {
                        existingLocationResponse.setLocationDatetime(locationResponse.getLocationDatetime());
                    }
                    if (locationResponse.getLocationOperation() != null) {
                        existingLocationResponse.setLocationOperation(locationResponse.getLocationOperation());
                    }
                    if (locationResponse.getStateFromStationId() != null) {
                        existingLocationResponse.setStateFromStationId(locationResponse.getStateFromStationId());
                    }
                    if (locationResponse.getStateFromStationName() != null) {
                        existingLocationResponse.setStateFromStationName(locationResponse.getStateFromStationName());
                    }
                    if (locationResponse.getStateToStationId() != null) {
                        existingLocationResponse.setStateToStationId(locationResponse.getStateToStationId());
                    }
                    if (locationResponse.getStateToStationName() != null) {
                        existingLocationResponse.setStateToStationName(locationResponse.getStateToStationName());
                    }
                    if (locationResponse.getStateSendDatetime() != null) {
                        existingLocationResponse.setStateSendDatetime(locationResponse.getStateSendDatetime());
                    }
                    if (locationResponse.getStateSenderId() != null) {
                        existingLocationResponse.setStateSenderId(locationResponse.getStateSenderId());
                    }
                    if (locationResponse.getPlanedServiceDatetime() != null) {
                        existingLocationResponse.setPlanedServiceDatetime(locationResponse.getPlanedServiceDatetime());
                    }
                    if (locationResponse.getTankOwner() != null) {
                        existingLocationResponse.setTankOwner(locationResponse.getTankOwner());
                    }
                    if (locationResponse.getTankModel() != null) {
                        existingLocationResponse.setTankModel(locationResponse.getTankModel());
                    }
                    if (locationResponse.getDefectRegion() != null) {
                        existingLocationResponse.setDefectRegion(locationResponse.getDefectRegion());
                    }
                    if (locationResponse.getDefectStation() != null) {
                        existingLocationResponse.setDefectStation(locationResponse.getDefectStation());
                    }
                    if (locationResponse.getDefectDatetime() != null) {
                        existingLocationResponse.setDefectDatetime(locationResponse.getDefectDatetime());
                    }
                    if (locationResponse.getDefectDetails() != null) {
                        existingLocationResponse.setDefectDetails(locationResponse.getDefectDetails());
                    }
                    if (locationResponse.getRepairRegion() != null) {
                        existingLocationResponse.setRepairRegion(locationResponse.getRepairRegion());
                    }
                    if (locationResponse.getRepairStation() != null) {
                        existingLocationResponse.setRepairStation(locationResponse.getRepairStation());
                    }
                    if (locationResponse.getRepairDatetime() != null) {
                        existingLocationResponse.setRepairDatetime(locationResponse.getRepairDatetime());
                    }
                    if (locationResponse.getUpdateDatetime() != null) {
                        existingLocationResponse.setUpdateDatetime(locationResponse.getUpdateDatetime());
                    }

                    return existingLocationResponse;
                }
            )
            .map(locationResponseRepository::save);
    }

    /**
     * Get all the locationResponses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LocationResponse> findAll(Pageable pageable) {
        log.debug("Request to get all LocationResponses");
        return locationResponseRepository.findAll(pageable);
    }

    /**
     *  Get all the locationResponses where LocationRequest is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<LocationResponse> findAllWhereLocationRequestIsNull() {
        log.debug("Request to get all locationResponses where LocationRequest is null");
        return StreamSupport
            .stream(locationResponseRepository.findAll().spliterator(), false)
            .filter(locationResponse -> locationResponse.getLocationRequest() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one locationResponse by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LocationResponse> findOne(Long id) {
        log.debug("Request to get LocationResponse : {}", id);
        return locationResponseRepository.findById(id);
    }

    /**
     * Delete the locationResponse by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LocationResponse : {}", id);
        locationResponseRepository.deleteById(id);
    }
}
