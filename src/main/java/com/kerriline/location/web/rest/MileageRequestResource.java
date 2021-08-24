package com.kerriline.location.web.rest;

import com.kerriline.location.domain.MileageRequest;
import com.kerriline.location.repository.MileageRequestRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.kerriline.location.domain.MileageRequest}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MileageRequestResource {

    private final Logger log = LoggerFactory.getLogger(MileageRequestResource.class);

    private static final String ENTITY_NAME = "mileageRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MileageRequestRepository mileageRequestRepository;

    public MileageRequestResource(MileageRequestRepository mileageRequestRepository) {
        this.mileageRequestRepository = mileageRequestRepository;
    }

    /**
     * {@code POST  /mileage-requests} : Create a new mileageRequest.
     *
     * @param mileageRequest the mileageRequest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mileageRequest, or with status {@code 400 (Bad Request)} if the mileageRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mileage-requests")
    public ResponseEntity<MileageRequest> createMileageRequest(@RequestBody MileageRequest mileageRequest) throws URISyntaxException {
        log.debug("REST request to save MileageRequest : {}", mileageRequest);
        if (mileageRequest.getId() != null) {
            throw new BadRequestAlertException("A new mileageRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MileageRequest result = mileageRequestRepository.save(mileageRequest);
        return ResponseEntity
            .created(new URI("/api/mileage-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mileage-requests/:id} : Updates an existing mileageRequest.
     *
     * @param id the id of the mileageRequest to save.
     * @param mileageRequest the mileageRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mileageRequest,
     * or with status {@code 400 (Bad Request)} if the mileageRequest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mileageRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mileage-requests/{id}")
    public ResponseEntity<MileageRequest> updateMileageRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MileageRequest mileageRequest
    ) throws URISyntaxException {
        log.debug("REST request to update MileageRequest : {}, {}", id, mileageRequest);
        if (mileageRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mileageRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mileageRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MileageRequest result = mileageRequestRepository.save(mileageRequest);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mileageRequest.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mileage-requests/:id} : Partial updates given fields of an existing mileageRequest, field will ignore if it is null
     *
     * @param id the id of the mileageRequest to save.
     * @param mileageRequest the mileageRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mileageRequest,
     * or with status {@code 400 (Bad Request)} if the mileageRequest is not valid,
     * or with status {@code 404 (Not Found)} if the mileageRequest is not found,
     * or with status {@code 500 (Internal Server Error)} if the mileageRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mileage-requests/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<MileageRequest> partialUpdateMileageRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MileageRequest mileageRequest
    ) throws URISyntaxException {
        log.debug("REST request to partial update MileageRequest partially : {}, {}", id, mileageRequest);
        if (mileageRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mileageRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mileageRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MileageRequest> result = mileageRequestRepository
            .findById(mileageRequest.getId())
            .map(
                existingMileageRequest -> {
                    if (mileageRequest.getRequestDatetime() != null) {
                        existingMileageRequest.setRequestDatetime(mileageRequest.getRequestDatetime());
                    }
                    if (mileageRequest.getTankNumbers() != null) {
                        existingMileageRequest.setTankNumbers(mileageRequest.getTankNumbers());
                    }

                    return existingMileageRequest;
                }
            )
            .map(mileageRequestRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mileageRequest.getId().toString())
        );
    }

    /**
     * {@code GET  /mileage-requests} : get all the mileageRequests.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mileageRequests in body.
     */
    @GetMapping("/mileage-requests")
    public ResponseEntity<List<MileageRequest>> getAllMileageRequests(Pageable pageable) {
        log.debug("REST request to get a page of MileageRequests");
        Page<MileageRequest> page = mileageRequestRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mileage-requests/:id} : get the "id" mileageRequest.
     *
     * @param id the id of the mileageRequest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mileageRequest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mileage-requests/{id}")
    public ResponseEntity<MileageRequest> getMileageRequest(@PathVariable Long id) {
        log.debug("REST request to get MileageRequest : {}", id);
        Optional<MileageRequest> mileageRequest = mileageRequestRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(mileageRequest);
    }

    /**
     * {@code DELETE  /mileage-requests/:id} : delete the "id" mileageRequest.
     *
     * @param id the id of the mileageRequest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mileage-requests/{id}")
    public ResponseEntity<Void> deleteMileageRequest(@PathVariable Long id) {
        log.debug("REST request to delete MileageRequest : {}", id);
        mileageRequestRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
