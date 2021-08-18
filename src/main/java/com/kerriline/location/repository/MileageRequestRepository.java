package com.kerriline.location.repository;

import com.kerriline.location.domain.MileageRequest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MileageRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MileageRequestRepository extends JpaRepository<MileageRequest, Long>, JpaSpecificationExecutor<MileageRequest> {}
