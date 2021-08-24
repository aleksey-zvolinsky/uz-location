package com.kerriline.location.repository;

import java.time.LocalDate;
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
    @Query("select r from LocationResponse r where r.responseDatetime > :responseDatetime order by responseDatetime desc")
    List<LocationResponse> findAllWithResposeDateTimeAfter(
      @Param("responseDatetime") LocalDate cutDate);
}
