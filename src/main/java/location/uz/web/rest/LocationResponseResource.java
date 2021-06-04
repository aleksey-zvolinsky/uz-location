package location.uz.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import location.uz.domain.LocationResponse;
import location.uz.repository.LocationResponseRepository;
import location.uz.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link location.uz.domain.LocationResponse}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LocationResponseResource {

    private final Logger log = LoggerFactory.getLogger(LocationResponseResource.class);

    private static final String ENTITY_NAME = "locationResponse";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocationResponseRepository locationResponseRepository;

    public LocationResponseResource(LocationResponseRepository locationResponseRepository) {
        this.locationResponseRepository = locationResponseRepository;
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
        LocationResponse result = locationResponseRepository.save(locationResponse);
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

        LocationResponse result = locationResponseRepository.save(locationResponse);
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

        Optional<LocationResponse> result = locationResponseRepository
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
                    if (locationResponse.getMileageCurrent() != null) {
                        existingLocationResponse.setMileageCurrent(locationResponse.getMileageCurrent());
                    }
                    if (locationResponse.getMileageDatetime() != null) {
                        existingLocationResponse.setMileageDatetime(locationResponse.getMileageDatetime());
                    }
                    if (locationResponse.getMileageRemain() != null) {
                        existingLocationResponse.setMileageRemain(locationResponse.getMileageRemain());
                    }
                    if (locationResponse.getMileageUpdateDatetime() != null) {
                        existingLocationResponse.setMileageUpdateDatetime(locationResponse.getMileageUpdateDatetime());
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
                    if (locationResponse.getRepairDatatime() != null) {
                        existingLocationResponse.setRepairDatatime(locationResponse.getRepairDatatime());
                    }

                    return existingLocationResponse;
                }
            )
            .map(locationResponseRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, locationResponse.getId().toString())
        );
    }

    /**
     * {@code GET  /location-responses} : get all the locationResponses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of locationResponses in body.
     */
    @GetMapping("/location-responses")
    public List<LocationResponse> getAllLocationResponses() {
        log.debug("REST request to get all LocationResponses");
        return locationResponseRepository.findAll();
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
        Optional<LocationResponse> locationResponse = locationResponseRepository.findById(id);
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
        locationResponseRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
