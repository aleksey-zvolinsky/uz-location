package com.kerriline.location.web.rest;

import com.kerriline.location.domain.LocationRequest;
import com.kerriline.location.repository.LocationRequestRepository;
import com.kerriline.location.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.kerriline.location.domain.LocationRequest}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LocationRequestResource {

    private final Logger log = LoggerFactory.getLogger(LocationRequestResource.class);

    private static final String ENTITY_NAME = "locationRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocationRequestRepository locationRequestRepository;

    public LocationRequestResource(LocationRequestRepository locationRequestRepository) {
        this.locationRequestRepository = locationRequestRepository;
    }

    /**
     * {@code POST  /location-requests} : Create a new locationRequest.
     *
     * @param locationRequest the locationRequest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new locationRequest, or with status {@code 400 (Bad Request)} if the locationRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/location-requests")
    public ResponseEntity<LocationRequest> createLocationRequest(@RequestBody LocationRequest locationRequest) throws URISyntaxException {
        log.debug("REST request to save LocationRequest : {}", locationRequest);
        if (locationRequest.getId() != null) {
            throw new BadRequestAlertException("A new locationRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LocationRequest result = locationRequestRepository.save(locationRequest);
        return ResponseEntity
            .created(new URI("/api/location-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /location-requests/:id} : Updates an existing locationRequest.
     *
     * @param id the id of the locationRequest to save.
     * @param locationRequest the locationRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locationRequest,
     * or with status {@code 400 (Bad Request)} if the locationRequest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the locationRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/location-requests/{id}")
    public ResponseEntity<LocationRequest> updateLocationRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LocationRequest locationRequest
    ) throws URISyntaxException {
        log.debug("REST request to update LocationRequest : {}, {}", id, locationRequest);
        if (locationRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, locationRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!locationRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LocationRequest result = locationRequestRepository.save(locationRequest);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, locationRequest.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /location-requests/:id} : Partial updates given fields of an existing locationRequest, field will ignore if it is null
     *
     * @param id the id of the locationRequest to save.
     * @param locationRequest the locationRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locationRequest,
     * or with status {@code 400 (Bad Request)} if the locationRequest is not valid,
     * or with status {@code 404 (Not Found)} if the locationRequest is not found,
     * or with status {@code 500 (Internal Server Error)} if the locationRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/location-requests/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LocationRequest> partialUpdateLocationRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LocationRequest locationRequest
    ) throws URISyntaxException {
        log.debug("REST request to partial update LocationRequest partially : {}, {}", id, locationRequest);
        if (locationRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, locationRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!locationRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LocationRequest> result = locationRequestRepository
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

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, locationRequest.getId().toString())
        );
    }

    /**
     * {@code GET  /location-requests} : get all the locationRequests.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of locationRequests in body.
     */
    @GetMapping("/location-requests")
    public List<LocationRequest> getAllLocationRequests() {
        log.debug("REST request to get all LocationRequests");
        return locationRequestRepository.findAll();
    }

    /**
     * {@code GET  /location-requests/:id} : get the "id" locationRequest.
     *
     * @param id the id of the locationRequest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the locationRequest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/location-requests/{id}")
    public ResponseEntity<LocationRequest> getLocationRequest(@PathVariable Long id) {
        log.debug("REST request to get LocationRequest : {}", id);
        Optional<LocationRequest> locationRequest = locationRequestRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(locationRequest);
    }

    /**
     * {@code DELETE  /location-requests/:id} : delete the "id" locationRequest.
     *
     * @param id the id of the locationRequest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/location-requests/{id}")
    public ResponseEntity<Void> deleteLocationRequest(@PathVariable Long id) {
        log.debug("REST request to delete LocationRequest : {}", id);
        locationRequestRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
