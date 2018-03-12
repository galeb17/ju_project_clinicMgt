package com.pgdit.repository;

import com.pgdit.domain.TestTransaction;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TestTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestTransactionRepository extends JpaRepository<TestTransaction, Long> {

}
