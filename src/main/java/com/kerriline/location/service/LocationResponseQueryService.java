package com.kerriline.location.service;

import com.kerriline.location.domain.*; // for static metamodels
import com.kerriline.location.domain.LocationResponse;
import com.kerriline.location.repository.LocationResponseRepository;
import com.kerriline.location.service.criteria.LocationResponseCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link LocationResponse} entities in the database.
 * The main input is a {@link LocationResponseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LocationResponse} or a {@link Page} of {@link LocationResponse} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LocationResponseQueryService extends QueryService<LocationResponse> {

    private final Logger log = LoggerFactory.getLogger(LocationResponseQueryService.class);

    private final LocationResponseRepository locationResponseRepository;

    public LocationResponseQueryService(LocationResponseRepository locationResponseRepository) {
        this.locationResponseRepository = locationResponseRepository;
    }

    /**
     * Return a {@link List} of {@link LocationResponse} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LocationResponse> findByCriteria(LocationResponseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LocationResponse> specification = createSpecification(criteria);
        return locationResponseRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link LocationResponse} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LocationResponse> findByCriteria(LocationResponseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LocationResponse> specification = createSpecification(criteria);
        return locationResponseRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LocationResponseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LocationResponse> specification = createSpecification(criteria);
        return locationResponseRepository.count(specification);
    }

    /**
     * Function to convert {@link LocationResponseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LocationResponse> createSpecification(LocationResponseCriteria criteria) {
        Specification<LocationResponse> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LocationResponse_.id));
            }
            if (criteria.getResponseDatetime() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getResponseDatetime(), LocationResponse_.responseDatetime));
            }
            if (criteria.getTankNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTankNumber(), LocationResponse_.tankNumber));
            }
            if (criteria.getTankType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTankType(), LocationResponse_.tankType));
            }
            if (criteria.getCargoId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCargoId(), LocationResponse_.cargoId));
            }
            if (criteria.getCargoName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCargoName(), LocationResponse_.cargoName));
            }
            if (criteria.getWeight() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWeight(), LocationResponse_.weight));
            }
            if (criteria.getReceiverId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReceiverId(), LocationResponse_.receiverId));
            }
            if (criteria.getTankIndex() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTankIndex(), LocationResponse_.tankIndex));
            }
            if (criteria.getLocationStationId() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getLocationStationId(), LocationResponse_.locationStationId));
            }
            if (criteria.getLocationStationName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getLocationStationName(), LocationResponse_.locationStationName));
            }
            if (criteria.getLocationDatetime() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getLocationDatetime(), LocationResponse_.locationDatetime));
            }
            if (criteria.getLocationOperation() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getLocationOperation(), LocationResponse_.locationOperation));
            }
            if (criteria.getStateFromStationId() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getStateFromStationId(), LocationResponse_.stateFromStationId));
            }
            if (criteria.getStateFromStationName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getStateFromStationName(), LocationResponse_.stateFromStationName));
            }
            if (criteria.getStateToStationId() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getStateToStationId(), LocationResponse_.stateToStationId));
            }
            if (criteria.getStateToStationName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getStateToStationName(), LocationResponse_.stateToStationName));
            }
            if (criteria.getStateSendDatetime() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getStateSendDatetime(), LocationResponse_.stateSendDatetime));
            }
            if (criteria.getStateSenderId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStateSenderId(), LocationResponse_.stateSenderId));
            }
            if (criteria.getPlanedServiceDatetime() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getPlanedServiceDatetime(), LocationResponse_.planedServiceDatetime)
                    );
            }
            if (criteria.getTankOwner() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTankOwner(), LocationResponse_.tankOwner));
            }
            if (criteria.getTankModel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTankModel(), LocationResponse_.tankModel));
            }
            if (criteria.getDefectRegion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDefectRegion(), LocationResponse_.defectRegion));
            }
            if (criteria.getDefectStation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDefectStation(), LocationResponse_.defectStation));
            }
            if (criteria.getDefectDatetime() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDefectDatetime(), LocationResponse_.defectDatetime));
            }
            if (criteria.getDefectDetails() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDefectDetails(), LocationResponse_.defectDetails));
            }
            if (criteria.getRepairRegion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRepairRegion(), LocationResponse_.repairRegion));
            }
            if (criteria.getRepairStation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRepairStation(), LocationResponse_.repairStation));
            }
            if (criteria.getRepairDatetime() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRepairDatetime(), LocationResponse_.repairDatetime));
            }
            if (criteria.getUpdateDatetime() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUpdateDatetime(), LocationResponse_.updateDatetime));
            }
            if (criteria.getLocationRequestId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLocationRequestId(),
                            root -> root.join(LocationResponse_.locationRequest, JoinType.LEFT).get(LocationRequest_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
