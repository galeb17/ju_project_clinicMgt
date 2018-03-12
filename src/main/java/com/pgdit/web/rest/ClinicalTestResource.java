package com.pgdit.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pgdit.domain.ClinicalTest;

import com.pgdit.repository.ClinicalTestRepository;
import com.pgdit.web.rest.errors.BadRequestAlertException;
import com.pgdit.web.rest.util.HeaderUtil;
import com.pgdit.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ClinicalTest.
 */
@RestController
@RequestMapping("/api")
public class ClinicalTestResource {

    private final Logger log = LoggerFactory.getLogger(ClinicalTestResource.class);

    private static final String ENTITY_NAME = "clinicalTest";

    private final ClinicalTestRepository clinicalTestRepository;

    public ClinicalTestResource(ClinicalTestRepository clinicalTestRepository) {
        this.clinicalTestRepository = clinicalTestRepository;
    }

    /**
     * POST  /clinical-tests : Create a new clinicalTest.
     *
     * @param clinicalTest the clinicalTest to create
     * @return the ResponseEntity with status 201 (Created) and with body the new clinicalTest, or with status 400 (Bad Request) if the clinicalTest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/clinical-tests")
    @Timed
    public ResponseEntity<ClinicalTest> createClinicalTest(@RequestBody ClinicalTest clinicalTest) throws URISyntaxException {
        log.debug("REST request to save ClinicalTest : {}", clinicalTest);
        if (clinicalTest.getId() != null) {
            throw new BadRequestAlertException("A new clinicalTest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClinicalTest result = clinicalTestRepository.save(clinicalTest);
        return ResponseEntity.created(new URI("/api/clinical-tests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /clinical-tests : Updates an existing clinicalTest.
     *
     * @param clinicalTest the clinicalTest to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated clinicalTest,
     * or with status 400 (Bad Request) if the clinicalTest is not valid,
     * or with status 500 (Internal Server Error) if the clinicalTest couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/clinical-tests")
    @Timed
    public ResponseEntity<ClinicalTest> updateClinicalTest(@RequestBody ClinicalTest clinicalTest) throws URISyntaxException {
        log.debug("REST request to update ClinicalTest : {}", clinicalTest);
        if (clinicalTest.getId() == null) {
            return createClinicalTest(clinicalTest);
        }
        ClinicalTest result = clinicalTestRepository.save(clinicalTest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, clinicalTest.getId().toString()))
            .body(result);
    }

    /**
     * GET  /clinical-tests : get all the clinicalTests.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of clinicalTests in body
     */
    @GetMapping("/clinical-tests")
    @Timed
    public ResponseEntity<List<ClinicalTest>> getAllClinicalTests(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ClinicalTests");
        Page<ClinicalTest> page = clinicalTestRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/clinical-tests");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /clinical-tests/:id : get the "id" clinicalTest.
     *
     * @param id the id of the clinicalTest to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the clinicalTest, or with status 404 (Not Found)
     */
    @GetMapping("/clinical-tests/{id}")
    @Timed
    public ResponseEntity<ClinicalTest> getClinicalTest(@PathVariable Long id) {
        log.debug("REST request to get ClinicalTest : {}", id);
        ClinicalTest clinicalTest = clinicalTestRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(clinicalTest));
    }

    /**
     * DELETE  /clinical-tests/:id : delete the "id" clinicalTest.
     *
     * @param id the id of the clinicalTest to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/clinical-tests/{id}")
    @Timed
    public ResponseEntity<Void> deleteClinicalTest(@PathVariable Long id) {
        log.debug("REST request to delete ClinicalTest : {}", id);
        clinicalTestRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
