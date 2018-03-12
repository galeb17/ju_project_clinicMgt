package com.pgdit.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pgdit.domain.TestTransaction;

import com.pgdit.repository.TestTransactionRepository;
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
 * REST controller for managing TestTransaction.
 */
@RestController
@RequestMapping("/api")
public class TestTransactionResource {

    private final Logger log = LoggerFactory.getLogger(TestTransactionResource.class);

    private static final String ENTITY_NAME = "testTransaction";

    private final TestTransactionRepository testTransactionRepository;

    public TestTransactionResource(TestTransactionRepository testTransactionRepository) {
        this.testTransactionRepository = testTransactionRepository;
    }

    /**
     * POST  /test-transactions : Create a new testTransaction.
     *
     * @param testTransaction the testTransaction to create
     * @return the ResponseEntity with status 201 (Created) and with body the new testTransaction, or with status 400 (Bad Request) if the testTransaction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/test-transactions")
    @Timed
    public ResponseEntity<TestTransaction> createTestTransaction(@RequestBody TestTransaction testTransaction) throws URISyntaxException {
        log.debug("REST request to save TestTransaction : {}", testTransaction);
        if (testTransaction.getId() != null) {
            throw new BadRequestAlertException("A new testTransaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestTransaction result = testTransactionRepository.save(testTransaction);
        return ResponseEntity.created(new URI("/api/test-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /test-transactions : Updates an existing testTransaction.
     *
     * @param testTransaction the testTransaction to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated testTransaction,
     * or with status 400 (Bad Request) if the testTransaction is not valid,
     * or with status 500 (Internal Server Error) if the testTransaction couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/test-transactions")
    @Timed
    public ResponseEntity<TestTransaction> updateTestTransaction(@RequestBody TestTransaction testTransaction) throws URISyntaxException {
        log.debug("REST request to update TestTransaction : {}", testTransaction);
        if (testTransaction.getId() == null) {
            return createTestTransaction(testTransaction);
        }
        TestTransaction result = testTransactionRepository.save(testTransaction);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, testTransaction.getId().toString()))
            .body(result);
    }

    /**
     * GET  /test-transactions : get all the testTransactions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of testTransactions in body
     */
    @GetMapping("/test-transactions")
    @Timed
    public ResponseEntity<List<TestTransaction>> getAllTestTransactions(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of TestTransactions");
        Page<TestTransaction> page = testTransactionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/test-transactions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /test-transactions/:id : get the "id" testTransaction.
     *
     * @param id the id of the testTransaction to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the testTransaction, or with status 404 (Not Found)
     */
    @GetMapping("/test-transactions/{id}")
    @Timed
    public ResponseEntity<TestTransaction> getTestTransaction(@PathVariable Long id) {
        log.debug("REST request to get TestTransaction : {}", id);
        TestTransaction testTransaction = testTransactionRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(testTransaction));
    }

    /**
     * DELETE  /test-transactions/:id : delete the "id" testTransaction.
     *
     * @param id the id of the testTransaction to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/test-transactions/{id}")
    @Timed
    public ResponseEntity<Void> deleteTestTransaction(@PathVariable Long id) {
        log.debug("REST request to delete TestTransaction : {}", id);
        testTransactionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
