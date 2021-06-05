package com.kerriline.location.repository;

import com.kerriline.location.domain.LocationResponse;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LocationResponse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationResponseRepository extends JpaRepository<LocationResponse, Long> {}
