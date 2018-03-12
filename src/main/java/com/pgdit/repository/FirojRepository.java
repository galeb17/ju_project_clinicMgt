package com.pgdit.repository;

import com.pgdit.domain.Firoj;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Firoj entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FirojRepository extends JpaRepository<Firoj, Long> {

}
