package location.uz.repository;

import location.uz.domain.LocationRequest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LocationRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationRequestRepository extends JpaRepository<LocationRequest, Long> {}
