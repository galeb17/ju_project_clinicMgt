package com.pgdit.repository;

import com.pgdit.domain.FinalBill;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the FinalBill entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FinalBillRepository extends JpaRepository<FinalBill, Long> {

}
