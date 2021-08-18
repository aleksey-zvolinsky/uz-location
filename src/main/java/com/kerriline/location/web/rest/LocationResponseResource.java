package com.kerriline.location.web.rest;

import com.kerriline.location.domain.LocationResponse;
import com.kerriline.location.repository.LocationResponseRepository;
import com.kerriline.location.service.LocationResponseQueryService;
import com.kerriline.location.service.LocationResponseService;
import com.kerriline.location.service.criteria.LocationResponseCriteria;
import com.kerriline.location.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.kerriline.location.domain.LocationResponse}.
 */
@RestController
@RequestMapping("/api")
public class LocationResponseResource {

    private final Logger log = LoggerFactory.getLogger(LocationResponseResource.class);

    private static final String ENTITY_NAME = "locationResponse";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocationResponseService locationResponseService;

    private final LocationResponseRepository locationResponseRepository;

    private final LocationResponseQueryService locationResponseQueryService;

    public LocationResponseResource(
        LocationResponseService locationResponseService,
        LocationResponseRepository locationResponseRepository,
        LocationResponseQueryService locationResponseQueryService
    ) {
        this.locationResponseService = locationResponseService;
        this.locationResponseRepository = locationResponseRepository;
        this.locationResponseQueryService = locationResponseQueryService;
    }

    /**
     * {@code POST  /location-responses} : Create a new locationResponse.
     *
     * @param locationResponse the locationResponse to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new locationResponse, or with status {@code 400 (Bad Request)} if the locationResponse has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/location-responses")
    public ResponseEntity<LocationResponse> createLocationResponse(@RequestBody LocationResponse locationResponse)
        throws URISyntaxException {
        log.debug("REST request to save LocationResponse : {}", locationResponse);
        if (locationResponse.getId() != null) {
            throw new BadRequestAlertException("A new locationResponse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LocationResponse result = locationResponseService.save(locationResponse);
        return ResponseEntity
            .created(new URI("/api/location-responses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /location-responses/:id} : Updates an existing locationResponse.
     *
     * @param id the id of the locationResponse to save.
     * @param locationResponse the locationResponse to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locationResponse,
     * or with status {@code 400 (Bad Request)} if the locationResponse is not valid,
     * or with status {@code 500 (Internal Server Error)} if the locationResponse couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/location-responses/{id}")
    public ResponseEntity<LocationResponse> updateLocationResponse(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LocationResponse locationResponse
    ) throws URISyntaxException {
        log.debug("REST request to update LocationResponse : {}, {}", id, locationResponse);
        if (locationResponse.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, locationResponse.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!locationResponseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LocationResponse result = locationResponseService.save(locationResponse);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, locationResponse.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /location-responses/:id} : Partial updates given fields of an existing locationResponse, field will ignore if it is null
     *
     * @param id the id of the locationResponse to save.
     * @param locationResponse the locationResponse to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locationResponse,
     * or with status {@code 400 (Bad Request)} if the locationResponse is not valid,
     * or with status {@code 404 (Not Found)} if the locationResponse is not found,
     * or with status {@code 500 (Internal Server Error)} if the locationResponse couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/location-responses/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LocationResponse> partialUpdateLocationResponse(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LocationResponse locationResponse
    ) throws URISyntaxException {
        log.debug("REST request to partial update LocationResponse partially : {}, {}", id, locationResponse);
        if (locationResponse.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, locationResponse.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!locationResponseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LocationResponse> result = locationResponseService.partialUpdate(locationResponse);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, locationResponse.getId().toString())
        );
    }

    /**
     * {@code GET  /location-responses} : get all the locationResponses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of locationResponses in body.
     */
    @GetMapping("/location-responses")
    public ResponseEntity<List<LocationResponse>> getAllLocationResponses(LocationResponseCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LocationResponses by criteria: {}", criteria);
        Page<LocationResponse> page = locationResponseQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /location-responses/count} : count all the locationResponses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/location-responses/count")
    public ResponseEntity<Long> countLocationResponses(LocationResponseCriteria criteria) {
        log.debug("REST request to count LocationResponses by criteria: {}", criteria);
        return ResponseEntity.ok().body(locationResponseQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /location-responses/:id} : get the "id" locationResponse.
     *
     * @param id the id of the locationResponse to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the locationResponse, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/location-responses/{id}")
    public ResponseEntity<LocationResponse> getLocationResponse(@PathVariable Long id) {
        log.debug("REST request to get LocationResponse : {}", id);
        Optional<LocationResponse> locationResponse = locationResponseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(locationResponse);
    }

    /**
     * {@code DELETE  /location-responses/:id} : delete the "id" locationResponse.
     *
     * @param id the id of the locationResponse to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/location-responses/{id}")
    public ResponseEntity<Void> deleteLocationResponse(@PathVariable Long id) {
        log.debug("REST request to delete LocationResponse : {}", id);
        locationResponseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
