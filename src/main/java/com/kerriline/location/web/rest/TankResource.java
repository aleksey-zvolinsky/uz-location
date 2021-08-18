package com.kerriline.location.web.rest;

import com.kerriline.location.domain.Tank;
import com.kerriline.location.repository.TankRepository;
import com.kerriline.location.service.TankQueryService;
import com.kerriline.location.service.TankService;
import com.kerriline.location.service.criteria.TankCriteria;
import com.kerriline.location.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.kerriline.location.domain.Tank}.
 */
@RestController
@RequestMapping("/api")
public class TankResource {

    private final Logger log = LoggerFactory.getLogger(TankResource.class);

    private static final String ENTITY_NAME = "tank";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TankService tankService;

    private final TankRepository tankRepository;

    private final TankQueryService tankQueryService;

    public TankResource(TankService tankService, TankRepository tankRepository, TankQueryService tankQueryService) {
        this.tankService = tankService;
        this.tankRepository = tankRepository;
        this.tankQueryService = tankQueryService;
    }

    /**
     * {@code POST  /tanks} : Create a new tank.
     *
     * @param tank the tank to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tank, or with status {@code 400 (Bad Request)} if the tank has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tanks")
    public ResponseEntity<Tank> createTank(@Valid @RequestBody Tank tank) throws URISyntaxException {
        log.debug("REST request to save Tank : {}", tank);
        if (tank.getId() != null) {
            throw new BadRequestAlertException("A new tank cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tank result = tankService.save(tank);
        return ResponseEntity
            .created(new URI("/api/tanks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tanks/:id} : Updates an existing tank.
     *
     * @param id the id of the tank to save.
     * @param tank the tank to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tank,
     * or with status {@code 400 (Bad Request)} if the tank is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tank couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tanks/{id}")
    public ResponseEntity<Tank> updateTank(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Tank tank)
        throws URISyntaxException {
        log.debug("REST request to update Tank : {}, {}", id, tank);
        if (tank.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tank.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tankRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Tank result = tankService.save(tank);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tank.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tanks/:id} : Partial updates given fields of an existing tank, field will ignore if it is null
     *
     * @param id the id of the tank to save.
     * @param tank the tank to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tank,
     * or with status {@code 400 (Bad Request)} if the tank is not valid,
     * or with status {@code 404 (Not Found)} if the tank is not found,
     * or with status {@code 500 (Internal Server Error)} if the tank couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tanks/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Tank> partialUpdateTank(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Tank tank
    ) throws URISyntaxException {
        log.debug("REST request to partial update Tank partially : {}, {}", id, tank);
        if (tank.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tank.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tankRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Tank> result = tankService.partialUpdate(tank);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tank.getId().toString())
        );
    }

    /**
     * {@code GET  /tanks} : get all the tanks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tanks in body.
     */
    @GetMapping("/tanks")
    public ResponseEntity<List<Tank>> getAllTanks(TankCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Tanks by criteria: {}", criteria);
        Page<Tank> page = tankQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tanks/count} : count all the tanks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tanks/count")
    public ResponseEntity<Long> countTanks(TankCriteria criteria) {
        log.debug("REST request to count Tanks by criteria: {}", criteria);
        return ResponseEntity.ok().body(tankQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tanks/:id} : get the "id" tank.
     *
     * @param id the id of the tank to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tank, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tanks/{id}")
    public ResponseEntity<Tank> getTank(@PathVariable Long id) {
        log.debug("REST request to get Tank : {}", id);
        Optional<Tank> tank = tankService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tank);
    }

    /**
     * {@code DELETE  /tanks/:id} : delete the "id" tank.
     *
     * @param id the id of the tank to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tanks/{id}")
    public ResponseEntity<Void> deleteTank(@PathVariable Long id) {
        log.debug("REST request to delete Tank : {}", id);
        tankService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
