package com.kerriline.location.repository;

import com.kerriline.location.domain.MileageResponse;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MileageResponse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MileageResponseRepository extends JpaRepository<MileageResponse, Long> {}
