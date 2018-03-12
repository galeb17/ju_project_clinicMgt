package com.pgdit.repository;

import com.pgdit.domain.ClinicalTest;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ClinicalTest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClinicalTestRepository extends JpaRepository<ClinicalTest, Long> {

}
