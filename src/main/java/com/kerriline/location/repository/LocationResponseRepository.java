package com.kerriline.location.repository;

import com.kerriline.location.domain.LocationResponse;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LocationResponse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationResponseRepository extends JpaRepository<LocationResponse, Long>, JpaSpecificationExecutor<LocationResponse> {
    @Query("select r from LocationResponse r where r.responseDatetime > :responseDatetime order by responseDatetime desc")
    List<LocationResponse> findAllWithResposeDateTimeAfter(
      @Param("responseDatetime") LocalDate cutDate);
}
