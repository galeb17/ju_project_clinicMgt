package com.pgdit.repository;

import com.pgdit.domain.TransferIntimation;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TransferIntimation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransferIntimationRepository extends JpaRepository<TransferIntimation, Long> {

}
