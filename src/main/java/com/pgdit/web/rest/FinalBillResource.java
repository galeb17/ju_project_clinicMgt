package com.pgdit.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pgdit.domain.FinalBill;

import com.pgdit.repository.FinalBillRepository;
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
 * REST controller for managing FinalBill.
 */
@RestController
@RequestMapping("/api")
public class FinalBillResource {

    private final Logger log = LoggerFactory.getLogger(FinalBillResource.class);

    private static final String ENTITY_NAME = "finalBill";

    private final FinalBillRepository finalBillRepository;

    public FinalBillResource(FinalBillRepository finalBillRepository) {
        this.finalBillRepository = finalBillRepository;
    }

    /**
     * POST  /final-bills : Create a new finalBill.
     *
     * @param finalBill the finalBill to create
     * @return the ResponseEntity with status 201 (Created) and with body the new finalBill, or with status 400 (Bad Request) if the finalBill has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/final-bills")
    @Timed
    public ResponseEntity<FinalBill> createFinalBill(@RequestBody FinalBill finalBill) throws URISyntaxException {
        log.debug("REST request to save FinalBill : {}", finalBill);
        if (finalBill.getId() != null) {
            throw new BadRequestAlertException("A new finalBill cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FinalBill result = finalBillRepository.save(finalBill);
        return ResponseEntity.created(new URI("/api/final-bills/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /final-bills : Updates an existing finalBill.
     *
     * @param finalBill the finalBill to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated finalBill,
     * or with status 400 (Bad Request) if the finalBill is not valid,
     * or with status 500 (Internal Server Error) if the finalBill couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/final-bills")
    @Timed
    public ResponseEntity<FinalBill> updateFinalBill(@RequestBody FinalBill finalBill) throws URISyntaxException {
        log.debug("REST request to update FinalBill : {}", finalBill);
        if (finalBill.getId() == null) {
            return createFinalBill(finalBill);
        }
        FinalBill result = finalBillRepository.save(finalBill);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, finalBill.getId().toString()))
            .body(result);
    }

    /**
     * GET  /final-bills : get all the finalBills.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of finalBills in body
     */
    @GetMapping("/final-bills")
    @Timed
    public ResponseEntity<List<FinalBill>> getAllFinalBills(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of FinalBills");
        Page<FinalBill> page = finalBillRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/final-bills");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /final-bills/:id : get the "id" finalBill.
     *
     * @param id the id of the finalBill to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the finalBill, or with status 404 (Not Found)
     */
    @GetMapping("/final-bills/{id}")
    @Timed
    public ResponseEntity<FinalBill> getFinalBill(@PathVariable Long id) {
        log.debug("REST request to get FinalBill : {}", id);
        FinalBill finalBill = finalBillRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(finalBill));
    }

    /**
     * DELETE  /final-bills/:id : delete the "id" finalBill.
     *
     * @param id the id of the finalBill to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/final-bills/{id}")
    @Timed
    public ResponseEntity<Void> deleteFinalBill(@PathVariable Long id) {
        log.debug("REST request to delete FinalBill : {}", id);
        finalBillRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
