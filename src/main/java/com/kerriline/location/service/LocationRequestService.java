package com.kerriline.location.service;

import com.kerriline.location.domain.LocationRequest;
import com.kerriline.location.repository.LocationRequestRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LocationRequest}.
 */
@Service
@Transactional
public class LocationRequestService {

    private final Logger log = LoggerFactory.getLogger(LocationRequestService.class);

    private final LocationRequestRepository locationRequestRepository;

    public LocationRequestService(LocationRequestRepository locationRequestRepository) {
        this.locationRequestRepository = locationRequestRepository;
    }

    /**
     * Save a locationRequest.
     *
     * @param locationRequest the entity to save.
     * @return the persisted entity.
     */
    public LocationRequest save(LocationRequest locationRequest) {
        log.debug("Request to save LocationRequest : {}", locationRequest);
        return locationRequestRepository.save(locationRequest);
    }

    /**
     * Partially update a locationRequest.
     *
     * @param locationRequest the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LocationRequest> partialUpdate(LocationRequest locationRequest) {
        log.debug("Request to partially update LocationRequest : {}", locationRequest);

        return locationRequestRepository
            .findById(locationRequest.getId())
            .map(
                existingLocationRequest -> {
                    if (locationRequest.getRequestDatetime() != null) {
                        existingLocationRequest.setRequestDatetime(locationRequest.getRequestDatetime());
                    }
                    if (locationRequest.getTankNumbers() != null) {
                        existingLocationRequest.setTankNumbers(locationRequest.getTankNumbers());
                    }

                    return existingLocationRequest;
                }
            )
            .map(locationRequestRepository::save);
    }

    /**
     * Get all the locationRequests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LocationRequest> findAll(Pageable pageable) {
        log.debug("Request to get all LocationRequests");
        return locationRequestRepository.findAll(pageable);
    }

    /**
     * Get one locationRequest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LocationRequest> findOne(Long id) {
        log.debug("Request to get LocationRequest : {}", id);
        return locationRequestRepository.findById(id);
    }

    /**
     * Delete the locationRequest by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LocationRequest : {}", id);
        locationRequestRepository.deleteById(id);
    }
}
