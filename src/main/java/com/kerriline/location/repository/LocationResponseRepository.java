package com.kerriline.location.repository;

import com.kerriline.location.domain.LocationResponse;

import java.util.Collection;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LocationResponse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationResponseRepository extends JpaRepository<LocationResponse, Long> {
	
	@Query(value = "SELECT * FROM LocationResponse u",
			nativeQuery = true)
	Collection<LocationResponse> findAllActiveTanksNative();
}
