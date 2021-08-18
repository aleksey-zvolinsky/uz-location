package com.kerriline.location.service;

import com.kerriline.location.domain.MileageRequest;
import com.kerriline.location.repository.MileageRequestRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MileageRequest}.
 */
@Service
@Transactional
public class MileageRequestService {

    private final Logger log = LoggerFactory.getLogger(MileageRequestService.class);

    private final MileageRequestRepository mileageRequestRepository;

    public MileageRequestService(MileageRequestRepository mileageRequestRepository) {
        this.mileageRequestRepository = mileageRequestRepository;
    }

    /**
     * Save a mileageRequest.
     *
     * @param mileageRequest the entity to save.
     * @return the persisted entity.
     */
    public MileageRequest save(MileageRequest mileageRequest) {
        log.debug("Request to save MileageRequest : {}", mileageRequest);
        return mileageRequestRepository.save(mileageRequest);
    }

    /**
     * Partially update a mileageRequest.
     *
     * @param mileageRequest the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MileageRequest> partialUpdate(MileageRequest mileageRequest) {
        log.debug("Request to partially update MileageRequest : {}", mileageRequest);

        return mileageRequestRepository
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
    }

    /**
     * Get all the mileageRequests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MileageRequest> findAll(Pageable pageable) {
        log.debug("Request to get all MileageRequests");
        return mileageRequestRepository.findAll(pageable);
    }

    /**
     * Get one mileageRequest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MileageRequest> findOne(Long id) {
        log.debug("Request to get MileageRequest : {}", id);
        return mileageRequestRepository.findById(id);
    }

    /**
     * Delete the mileageRequest by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MileageRequest : {}", id);
        mileageRequestRepository.deleteById(id);
    }
}
