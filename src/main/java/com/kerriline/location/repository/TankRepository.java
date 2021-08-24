package com.kerriline.location.repository;

import com.kerriline.location.domain.Tank;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Tank entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TankRepository extends JpaRepository<Tank, Long> {}
