package location.uz.repository;

import location.uz.domain.LocationResponse;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LocationResponse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationResponseRepository extends JpaRepository<LocationResponse, Long> {}
