package com.kerriline.location.service;

import com.kerriline.location.domain.*; // for static metamodels
import com.kerriline.location.domain.Tank;
import com.kerriline.location.repository.TankRepository;
import com.kerriline.location.service.criteria.TankCriteria;
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
 * Service for executing complex queries for {@link Tank} entities in the database.
 * The main input is a {@link TankCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Tank} or a {@link Page} of {@link Tank} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TankQueryService extends QueryService<Tank> {

    private final Logger log = LoggerFactory.getLogger(TankQueryService.class);

    private final TankRepository tankRepository;

    public TankQueryService(TankRepository tankRepository) {
        this.tankRepository = tankRepository;
    }

    /**
     * Return a {@link List} of {@link Tank} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Tank> findByCriteria(TankCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Tank> specification = createSpecification(criteria);
        return tankRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Tank} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Tank> findByCriteria(TankCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Tank> specification = createSpecification(criteria);
        return tankRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TankCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Tank> specification = createSpecification(criteria);
        return tankRepository.count(specification);
    }

    /**
     * Function to convert {@link TankCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Tank> createSpecification(TankCriteria criteria) {
        Specification<Tank> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Tank_.id));
            }
            if (criteria.getTankNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTankNumber(), Tank_.tankNumber));
            }
            if (criteria.getOwnerName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOwnerName(), Tank_.ownerName));
            }
            if (criteria.getClientName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getClientName(), Tank_.clientName));
            }
            if (criteria.getLocationRequestId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLocationRequestId(),
                            root -> root.join(Tank_.locationRequests, JoinType.LEFT).get(LocationRequest_.id)
                        )
                    );
            }
            if (criteria.getMileageRequestId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMileageRequestId(),
                            root -> root.join(Tank_.mileageRequests, JoinType.LEFT).get(MileageRequest_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
