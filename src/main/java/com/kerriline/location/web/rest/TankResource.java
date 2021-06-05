package com.kerriline.location.web.rest;

import com.kerriline.location.domain.Tank;
import com.kerriline.location.repository.TankRepository;
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
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.kerriline.location.domain.Tank}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TankResource {

    private final Logger log = LoggerFactory.getLogger(TankResource.class);

    private static final String ENTITY_NAME = "tank";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TankRepository tankRepository;

    public TankResource(TankRepository tankRepository) {
        this.tankRepository = tankRepository;
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
        Tank result = tankRepository.save(tank);
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

        Tank result = tankRepository.save(tank);
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

        Optional<Tank> result = tankRepository
            .findById(tank.getId())
            .map(
                existingTank -> {
                    if (tank.getTankNumber() != null) {
                        existingTank.setTankNumber(tank.getTankNumber());
                    }
                    if (tank.getOwnerName() != null) {
                        existingTank.setOwnerName(tank.getOwnerName());
                    }
                    if (tank.getClientName() != null) {
                        existingTank.setClientName(tank.getClientName());
                    }

                    return existingTank;
                }
            )
            .map(tankRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tank.getId().toString())
        );
    }

    /**
     * {@code GET  /tanks} : get all the tanks.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tanks in body.
     */
    @GetMapping("/tanks")
    public List<Tank> getAllTanks() {
        log.debug("REST request to get all Tanks");
        return tankRepository.findAll();
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
        Optional<Tank> tank = tankRepository.findById(id);
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
        tankRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
