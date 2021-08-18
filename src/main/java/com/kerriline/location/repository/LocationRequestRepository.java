package com.kerriline.location.repository;

import com.kerriline.location.domain.LocationRequest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LocationRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationRequestRepository extends JpaRepository<LocationRequest, Long>, JpaSpecificationExecutor<LocationRequest> {}
