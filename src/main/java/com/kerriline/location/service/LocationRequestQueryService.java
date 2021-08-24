package com.kerriline.location.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// for static metamodels
import com.kerriline.location.domain.LocationRequest;
import com.kerriline.location.repository.LocationRequestRepository;
import com.kerriline.location.service.criteria.LocationRequestCriteria;

import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link LocationRequest} entities in the database.
 * The main input is a {@link LocationRequestCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LocationRequest} or a {@link Page} of {@link LocationRequest} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LocationRequestQueryService extends QueryService<LocationRequest> {

    private final Logger log = LoggerFactory.getLogger(LocationRequestQueryService.class);

    private final LocationRequestRepository locationRequestRepository;

    public LocationRequestQueryService(LocationRequestRepository locationRequestRepository) {
        this.locationRequestRepository = locationRequestRepository;
    }

    /**
     * Return a {@link List} of {@link LocationRequest} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LocationRequest> findByCriteria(LocationRequestCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LocationRequest> specification = createSpecification(criteria);
        return locationRequestRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link LocationRequest} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LocationRequest> findByCriteria(LocationRequestCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LocationRequest> specification = createSpecification(criteria);
        return locationRequestRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LocationRequestCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LocationRequest> specification = createSpecification(criteria);
        return locationRequestRepository.count(specification);
    }

    /**
     * Function to convert {@link LocationRequestCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LocationRequest> createSpecification(LocationRequestCriteria criteria) {
        Specification<LocationRequest> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LocationRequest_.id));
            }
            if (criteria.getRequestDatetime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRequestDatetime(), LocationRequest_.requestDatetime));
            }
            if (criteria.getTankNumbers() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTankNumbers(), LocationRequest_.tankNumbers));
            }
            if (criteria.getLocationResponseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLocationResponseId(),
                            root -> root.join(LocationRequest_.locationResponse, JoinType.LEFT).get(LocationResponse_.id)
                        )
                    );
            }
            if (criteria.getTankId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTankId(), root -> root.join(LocationRequest_.tank, JoinType.LEFT).get(Tank_.id))
                    );
            }
        }
        return specification;
    }
}
