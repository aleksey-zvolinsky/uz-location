package com.kerriline.location.web.rest;

import com.kerriline.location.domain.LocationRequest;
import com.kerriline.location.repository.LocationRequestRepository;
import com.kerriline.location.service.LocationRequestQueryService;
import com.kerriline.location.service.LocationRequestService;
import com.kerriline.location.service.criteria.LocationRequestCriteria;
import com.kerriline.location.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.kerriline.location.domain.LocationRequest}.
 */
@RestController
@RequestMapping("/api")
public class LocationRequestResource {

    private final Logger log = LoggerFactory.getLogger(LocationRequestResource.class);

    private static final String ENTITY_NAME = "locationRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocationRequestService locationRequestService;

    private final LocationRequestRepository locationRequestRepository;

    private final LocationRequestQueryService locationRequestQueryService;

    public LocationRequestResource(
        LocationRequestService locationRequestService,
        LocationRequestRepository locationRequestRepository,
        LocationRequestQueryService locationRequestQueryService
    ) {
        this.locationRequestService = locationRequestService;
        this.locationRequestRepository = locationRequestRepository;
        this.locationRequestQueryService = locationRequestQueryService;
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
        LocationRequest result = locationRequestService.save(locationRequest);
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

        LocationRequest result = locationRequestService.save(locationRequest);
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

        Optional<LocationRequest> result = locationRequestService.partialUpdate(locationRequest);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, locationRequest.getId().toString())
        );
    }

    /**
     * {@code GET  /location-requests} : get all the locationRequests.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of locationRequests in body.
     */
    @GetMapping("/location-requests")
    public ResponseEntity<List<LocationRequest>> getAllLocationRequests(LocationRequestCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LocationRequests by criteria: {}", criteria);
        Page<LocationRequest> page = locationRequestQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /location-requests/count} : count all the locationRequests.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/location-requests/count")
    public ResponseEntity<Long> countLocationRequests(LocationRequestCriteria criteria) {
        log.debug("REST request to count LocationRequests by criteria: {}", criteria);
        return ResponseEntity.ok().body(locationRequestQueryService.countByCriteria(criteria));
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
        Optional<LocationRequest> locationRequest = locationRequestService.findOne(id);
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
        locationRequestService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
