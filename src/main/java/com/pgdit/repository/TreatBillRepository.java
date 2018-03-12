package com.pgdit.repository;

import com.pgdit.domain.TreatBill;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TreatBill entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TreatBillRepository extends JpaRepository<TreatBill, Long> {

}
