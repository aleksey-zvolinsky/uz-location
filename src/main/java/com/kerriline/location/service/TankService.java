package com.kerriline.location.service;

import com.kerriline.location.domain.Tank;
import com.kerriline.location.repository.TankRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Tank}.
 */
@Service
@Transactional
public class TankService {

    private final Logger log = LoggerFactory.getLogger(TankService.class);

    private final TankRepository tankRepository;

    public TankService(TankRepository tankRepository) {
        this.tankRepository = tankRepository;
    }

    /**
     * Save a tank.
     *
     * @param tank the entity to save.
     * @return the persisted entity.
     */
    public Tank save(Tank tank) {
        log.debug("Request to save Tank : {}", tank);
        return tankRepository.save(tank);
    }

    /**
     * Partially update a tank.
     *
     * @param tank the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Tank> partialUpdate(Tank tank) {
        log.debug("Request to partially update Tank : {}", tank);

        return tankRepository
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
    }

    /**
     * Get all the tanks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Tank> findAll(Pageable pageable) {
        log.debug("Request to get all Tanks");
        return tankRepository.findAll(pageable);
    }

    /**
     * Get one tank by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Tank> findOne(Long id) {
        log.debug("Request to get Tank : {}", id);
        return tankRepository.findById(id);
    }

    /**
     * Delete the tank by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Tank : {}", id);
        tankRepository.deleteById(id);
    }
}
