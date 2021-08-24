package com.kerriline.location.repository;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kerriline.location.domain.LocationResponse;

/**
 * Spring Data SQL repository for the LocationResponse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KerrilineLocationResponseRepository extends LocationResponseRepository {

    List<LocationResponse> findByResponseDatetimeAfter(ZonedDateTime after);
    List<LocationResponse> findByTankTypeInAndResponseDatetimeAfter(Collection<String> tankType, ZonedDateTime after);
    List<LocationResponse> findByTankTypeNotInAndResponseDatetimeAfter(Collection<String> tankType, ZonedDateTime after);
}
