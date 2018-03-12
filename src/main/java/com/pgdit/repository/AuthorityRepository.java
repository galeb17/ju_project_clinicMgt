package com.pgdit.repository;

import com.pgdit.domain.Authority;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
//    @Query("select sdfkjasdfkjlsdlkj")
//    public List<Authority> getAllAutory();
}
