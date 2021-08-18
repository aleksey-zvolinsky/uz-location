package com.kerriline.location.service;

import com.kerriline.location.domain.MileageResponse;
import com.kerriline.location.repository.MileageResponseRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MileageResponse}.
 */
@Service
@Transactional
public class MileageResponseService {

    private final Logger log = LoggerFactory.getLogger(MileageResponseService.class);

    private final MileageResponseRepository mileageResponseRepository;

    public MileageResponseService(MileageResponseRepository mileageResponseRepository) {
        this.mileageResponseRepository = mileageResponseRepository;
    }

    /**
     * Save a mileageResponse.
     *
     * @param mileageResponse the entity to save.
     * @return the persisted entity.
     */
    public MileageResponse save(MileageResponse mileageResponse) {
        log.debug("Request to save MileageResponse : {}", mileageResponse);
        return mileageResponseRepository.save(mileageResponse);
    }

    /**
     * Partially update a mileageResponse.
     *
     * @param mileageResponse the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MileageResponse> partialUpdate(MileageResponse mileageResponse) {
        log.debug("Request to partially update MileageResponse : {}", mileageResponse);

        return mileageResponseRepository
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
    }

    /**
     * Get all the mileageResponses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MileageResponse> findAll(Pageable pageable) {
        log.debug("Request to get all MileageResponses");
        return mileageResponseRepository.findAll(pageable);
    }

    /**
     *  Get all the mileageResponses where MileageRequest is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MileageResponse> findAllWhereMileageRequestIsNull() {
        log.debug("Request to get all mileageResponses where MileageRequest is null");
        return StreamSupport
            .stream(mileageResponseRepository.findAll().spliterator(), false)
            .filter(mileageResponse -> mileageResponse.getMileageRequest() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one mileageResponse by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MileageResponse> findOne(Long id) {
        log.debug("Request to get MileageResponse : {}", id);
        return mileageResponseRepository.findById(id);
    }

    /**
     * Delete the mileageResponse by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MileageResponse : {}", id);
        mileageResponseRepository.deleteById(id);
    }
}
