package com.kerriline.location.service;

import com.kerriline.location.domain.*; // for static metamodels
import com.kerriline.location.domain.MileageRequest;
import com.kerriline.location.repository.MileageRequestRepository;
import com.kerriline.location.service.criteria.MileageRequestCriteria;
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
 * Service for executing complex queries for {@link MileageRequest} entities in the database.
 * The main input is a {@link MileageRequestCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MileageRequest} or a {@link Page} of {@link MileageRequest} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MileageRequestQueryService extends QueryService<MileageRequest> {

    private final Logger log = LoggerFactory.getLogger(MileageRequestQueryService.class);

    private final MileageRequestRepository mileageRequestRepository;

    public MileageRequestQueryService(MileageRequestRepository mileageRequestRepository) {
        this.mileageRequestRepository = mileageRequestRepository;
    }

    /**
     * Return a {@link List} of {@link MileageRequest} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MileageRequest> findByCriteria(MileageRequestCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MileageRequest> specification = createSpecification(criteria);
        return mileageRequestRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link MileageRequest} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MileageRequest> findByCriteria(MileageRequestCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MileageRequest> specification = createSpecification(criteria);
        return mileageRequestRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MileageRequestCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MileageRequest> specification = createSpecification(criteria);
        return mileageRequestRepository.count(specification);
    }

    /**
     * Function to convert {@link MileageRequestCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MileageRequest> createSpecification(MileageRequestCriteria criteria) {
        Specification<MileageRequest> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MileageRequest_.id));
            }
            if (criteria.getRequestDatetime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRequestDatetime(), MileageRequest_.requestDatetime));
            }
            if (criteria.getTankNumbers() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTankNumbers(), MileageRequest_.tankNumbers));
            }
            if (criteria.getMileageResponseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMileageResponseId(),
                            root -> root.join(MileageRequest_.mileageResponse, JoinType.LEFT).get(MileageResponse_.id)
                        )
                    );
            }
            if (criteria.getTankId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTankId(), root -> root.join(MileageRequest_.tank, JoinType.LEFT).get(Tank_.id))
                    );
            }
        }
        return specification;
    }
}
