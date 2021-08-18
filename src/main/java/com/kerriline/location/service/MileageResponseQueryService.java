package com.kerriline.location.service;

import com.kerriline.location.domain.*; // for static metamodels
import com.kerriline.location.domain.MileageResponse;
import com.kerriline.location.repository.MileageResponseRepository;
import com.kerriline.location.service.criteria.MileageResponseCriteria;
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
 * Service for executing complex queries for {@link MileageResponse} entities in the database.
 * The main input is a {@link MileageResponseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MileageResponse} or a {@link Page} of {@link MileageResponse} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MileageResponseQueryService extends QueryService<MileageResponse> {

    private final Logger log = LoggerFactory.getLogger(MileageResponseQueryService.class);

    private final MileageResponseRepository mileageResponseRepository;

    public MileageResponseQueryService(MileageResponseRepository mileageResponseRepository) {
        this.mileageResponseRepository = mileageResponseRepository;
    }

    /**
     * Return a {@link List} of {@link MileageResponse} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MileageResponse> findByCriteria(MileageResponseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MileageResponse> specification = createSpecification(criteria);
        return mileageResponseRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link MileageResponse} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MileageResponse> findByCriteria(MileageResponseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MileageResponse> specification = createSpecification(criteria);
        return mileageResponseRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MileageResponseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MileageResponse> specification = createSpecification(criteria);
        return mileageResponseRepository.count(specification);
    }

    /**
     * Function to convert {@link MileageResponseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MileageResponse> createSpecification(MileageResponseCriteria criteria) {
        Specification<MileageResponse> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MileageResponse_.id));
            }
            if (criteria.getResponseDatetime() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getResponseDatetime(), MileageResponse_.responseDatetime));
            }
            if (criteria.getTankNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTankNumber(), MileageResponse_.tankNumber));
            }
            if (criteria.getMileageCurrent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMileageCurrent(), MileageResponse_.mileageCurrent));
            }
            if (criteria.getMileageDatetime() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getMileageDatetime(), MileageResponse_.mileageDatetime));
            }
            if (criteria.getMileageRemain() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMileageRemain(), MileageResponse_.mileageRemain));
            }
            if (criteria.getMileageUpdateDatetime() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getMileageUpdateDatetime(), MileageResponse_.mileageUpdateDatetime)
                    );
            }
            if (criteria.getMileageRequestId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMileageRequestId(),
                            root -> root.join(MileageResponse_.mileageRequest, JoinType.LEFT).get(MileageRequest_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
