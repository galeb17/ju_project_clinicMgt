package com.pgdit.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pgdit.domain.TestDescription;

import com.pgdit.repository.TestDescriptionRepository;
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
 * REST controller for managing TestDescription.
 */
@RestController
@RequestMapping("/api")
public class TestDescriptionResource {

    private final Logger log = LoggerFactory.getLogger(TestDescriptionResource.class);

    private static final String ENTITY_NAME = "testDescription";

    private final TestDescriptionRepository testDescriptionRepository;

    public TestDescriptionResource(TestDescriptionRepository testDescriptionRepository) {
        this.testDescriptionRepository = testDescriptionRepository;
    }

    /**
     * POST  /test-descriptions : Create a new testDescription.
     *
     * @param testDescription the testDescription to create
     * @return the ResponseEntity with status 201 (Created) and with body the new testDescription, or with status 400 (Bad Request) if the testDescription has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/test-descriptions")
    @Timed
    public ResponseEntity<TestDescription> createTestDescription(@RequestBody TestDescription testDescription) throws URISyntaxException {
        log.debug("REST request to save TestDescription : {}", testDescription);
        if (testDescription.getId() != null) {
            throw new BadRequestAlertException("A new testDescription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestDescription result = testDescriptionRepository.save(testDescription);
        return ResponseEntity.created(new URI("/api/test-descriptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /test-descriptions : Updates an existing testDescription.
     *
     * @param testDescription the testDescription to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated testDescription,
     * or with status 400 (Bad Request) if the testDescription is not valid,
     * or with status 500 (Internal Server Error) if the testDescription couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/test-descriptions")
    @Timed
    public ResponseEntity<TestDescription> updateTestDescription(@RequestBody TestDescription testDescription) throws URISyntaxException {
        log.debug("REST request to update TestDescription : {}", testDescription);
        if (testDescription.getId() == null) {
            return createTestDescription(testDescription);
        }
        TestDescription result = testDescriptionRepository.save(testDescription);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, testDescription.getId().toString()))
            .body(result);
    }

    /**
     * GET  /test-descriptions : get all the testDescriptions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of testDescriptions in body
     */
    @GetMapping("/test-descriptions")
    @Timed
    public ResponseEntity<List<TestDescription>> getAllTestDescriptions(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of TestDescriptions");
        Page<TestDescription> page = testDescriptionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/test-descriptions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /test-descriptions/:id : get the "id" testDescription.
     *
     * @param id the id of the testDescription to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the testDescription, or with status 404 (Not Found)
     */
    @GetMapping("/test-descriptions/{id}")
    @Timed
    public ResponseEntity<TestDescription> getTestDescription(@PathVariable Long id) {
        log.debug("REST request to get TestDescription : {}", id);
        TestDescription testDescription = testDescriptionRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(testDescription));
    }

    /**
     * DELETE  /test-descriptions/:id : delete the "id" testDescription.
     *
     * @param id the id of the testDescription to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/test-descriptions/{id}")
    @Timed
    public ResponseEntity<Void> deleteTestDescription(@PathVariable Long id) {
        log.debug("REST request to delete TestDescription : {}", id);
        testDescriptionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
