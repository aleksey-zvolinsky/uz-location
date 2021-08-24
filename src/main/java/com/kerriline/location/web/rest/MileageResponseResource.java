package com.kerriline.location.web.rest;

import com.kerriline.location.domain.MileageResponse;
import com.kerriline.location.repository.MileageResponseRepository;
import com.kerriline.location.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.kerriline.location.domain.MileageResponse}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MileageResponseResource {

    private final Logger log = LoggerFactory.getLogger(MileageResponseResource.class);

    private static final String ENTITY_NAME = "mileageResponse";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MileageResponseRepository mileageResponseRepository;

    public MileageResponseResource(MileageResponseRepository mileageResponseRepository) {
        this.mileageResponseRepository = mileageResponseRepository;
    }

    /**
     * {@code POST  /mileage-responses} : Create a new mileageResponse.
     *
     * @param mileageResponse the mileageResponse to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mileageResponse, or with status {@code 400 (Bad Request)} if the mileageResponse has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mileage-responses")
    public ResponseEntity<MileageResponse> createMileageResponse(@RequestBody MileageResponse mileageResponse) throws URISyntaxException {
        log.debug("REST request to save MileageResponse : {}", mileageResponse);
        if (mileageResponse.getId() != null) {
            throw new BadRequestAlertException("A new mileageResponse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MileageResponse result = mileageResponseRepository.save(mileageResponse);
        return ResponseEntity
            .created(new URI("/api/mileage-responses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mileage-responses/:id} : Updates an existing mileageResponse.
     *
     * @param id the id of the mileageResponse to save.
     * @param mileageResponse the mileageResponse to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mileageResponse,
     * or with status {@code 400 (Bad Request)} if the mileageResponse is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mileageResponse couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mileage-responses/{id}")
    public ResponseEntity<MileageResponse> updateMileageResponse(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MileageResponse mileageResponse
    ) throws URISyntaxException {
        log.debug("REST request to update MileageResponse : {}, {}", id, mileageResponse);
        if (mileageResponse.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mileageResponse.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mileageResponseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MileageResponse result = mileageResponseRepository.save(mileageResponse);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mileageResponse.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mileage-responses/:id} : Partial updates given fields of an existing mileageResponse, field will ignore if it is null
     *
     * @param id the id of the mileageResponse to save.
     * @param mileageResponse the mileageResponse to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mileageResponse,
     * or with status {@code 400 (Bad Request)} if the mileageResponse is not valid,
     * or with status {@code 404 (Not Found)} if the mileageResponse is not found,
     * or with status {@code 500 (Internal Server Error)} if the mileageResponse couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mileage-responses/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<MileageResponse> partialUpdateMileageResponse(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MileageResponse mileageResponse
    ) throws URISyntaxException {
        log.debug("REST request to partial update MileageResponse partially : {}, {}", id, mileageResponse);
        if (mileageResponse.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mileageResponse.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mileageResponseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MileageResponse> result = mileageResponseRepository
            .findById(mileageResponse.getId())
            .map(
                existingMileageResponse -> {
                    if (mileageResponse.getResponseDatetime() != null) {
                        existingMileageResponse.setResponseDatetime(mileageResponse.getResponseDatetime());
                    }
                    if (mileageResponse.getTankNumber() != null) {
                        existingMileageResponse.setTankNumber(mileageResponse.getTankNumber());
                    }
                    if (mileageResponse.getMileageCurrent() != null) {
                        existingMileageResponse.setMileageCurrent(mileageResponse.getMileageCurrent());
                    }
                    if (mileageResponse.getMileageDatetime() != null) {
                        existingMileageResponse.setMileageDatetime(mileageResponse.getMileageDatetime());
                    }
                    if (mileageResponse.getMileageRemain() != null) {
                        existingMileageResponse.setMileageRemain(mileageResponse.getMileageRemain());
                    }
                    if (mileageResponse.getMileageUpdateDatetime() != null) {
                        existingMileageResponse.setMileageUpdateDatetime(mileageResponse.getMileageUpdateDatetime());
                    }

                    return existingMileageResponse;
                }
            )
            .map(mileageResponseRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mileageResponse.getId().toString())
        );
    }

    /**
     * {@code GET  /mileage-responses} : get all the mileageResponses.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mileageResponses in body.
     */
    @GetMapping("/mileage-responses")
    public ResponseEntity<List<MileageResponse>> getAllMileageResponses(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("mileagerequest-is-null".equals(filter)) {
            log.debug("REST request to get all MileageResponses where mileageRequest is null");
            return new ResponseEntity<>(
                StreamSupport
                    .stream(mileageResponseRepository.findAll().spliterator(), false)
                    .filter(mileageResponse -> mileageResponse.getMileageRequest() == null)
                    .collect(Collectors.toList()),
                HttpStatus.OK
            );
        }
        log.debug("REST request to get a page of MileageResponses");
        Page<MileageResponse> page = mileageResponseRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mileage-responses/:id} : get the "id" mileageResponse.
     *
     * @param id the id of the mileageResponse to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mileageResponse, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mileage-responses/{id}")
    public ResponseEntity<MileageResponse> getMileageResponse(@PathVariable Long id) {
        log.debug("REST request to get MileageResponse : {}", id);
        Optional<MileageResponse> mileageResponse = mileageResponseRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(mileageResponse);
    }

    /**
     * {@code DELETE  /mileage-responses/:id} : delete the "id" mileageResponse.
     *
     * @param id the id of the mileageResponse to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mileage-responses/{id}")
    public ResponseEntity<Void> deleteMileageResponse(@PathVariable Long id) {
        log.debug("REST request to delete MileageResponse : {}", id);
        mileageResponseRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
