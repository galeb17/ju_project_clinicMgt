package com.pgdit.repository;

import com.pgdit.domain.AppointSchedule;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the AppointSchedule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppointScheduleRepository extends JpaRepository<AppointSchedule, Long> {

}
