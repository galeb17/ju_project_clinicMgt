package com.pgdit.repository;

import com.pgdit.domain.TestDescription;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TestDescription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestDescriptionRepository extends JpaRepository<TestDescription, Long> {

}
