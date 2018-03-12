package com.pgdit.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pgdit.domain.TreatBill;

import com.pgdit.repository.TreatBillRepository;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TreatBill.
 */
@RestController
@RequestMapping("/api")
public class TreatBillResource {

    private final Logger log = LoggerFactory.getLogger(TreatBillResource.class);

    private static final String ENTITY_NAME = "treatBill";

    private final TreatBillRepository treatBillRepository;

    public TreatBillResource(TreatBillRepository treatBillRepository) {
        this.treatBillRepository = treatBillRepository;
    }

    /**
     * POST  /treat-bills : Create a new treatBill.
     *
     * @param treatBill the treatBill to create
     * @return the ResponseEntity with status 201 (Created) and with body the new treatBill, or with status 400 (Bad Request) if the treatBill has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/treat-bills")
    @Timed
    public ResponseEntity<TreatBill> createTreatBill(@Valid @RequestBody TreatBill treatBill) throws URISyntaxException {
        log.debug("REST request to save TreatBill : {}", treatBill);
        if (treatBill.getId() != null) {
            throw new BadRequestAlertException("A new treatBill cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TreatBill result = treatBillRepository.save(treatBill);
        return ResponseEntity.created(new URI("/api/treat-bills/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /treat-bills : Updates an existing treatBill.
     *
     * @param treatBill the treatBill to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated treatBill,
     * or with status 400 (Bad Request) if the treatBill is not valid,
     * or with status 500 (Internal Server Error) if the treatBill couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/treat-bills")
    @Timed
    public ResponseEntity<TreatBill> updateTreatBill(@Valid @RequestBody TreatBill treatBill) throws URISyntaxException {
        log.debug("REST request to update TreatBill : {}", treatBill);
        if (treatBill.getId() == null) {
            return createTreatBill(treatBill);
        }
        TreatBill result = treatBillRepository.save(treatBill);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, treatBill.getId().toString()))
            .body(result);
    }

    /**
     * GET  /treat-bills : get all the treatBills.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of treatBills in body
     */
    @GetMapping("/treat-bills")
    @Timed
    public ResponseEntity<List<TreatBill>> getAllTreatBills(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of TreatBills");
        Page<TreatBill> page = treatBillRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/treat-bills");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /treat-bills/:id : get the "id" treatBill.
     *
     * @param id the id of the treatBill to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the treatBill, or with status 404 (Not Found)
     */
    @GetMapping("/treat-bills/{id}")
    @Timed
    public ResponseEntity<TreatBill> getTreatBill(@PathVariable Long id) {
        log.debug("REST request to get TreatBill : {}", id);
        TreatBill treatBill = treatBillRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(treatBill));
    }

    /**
     * DELETE  /treat-bills/:id : delete the "id" treatBill.
     *
     * @param id the id of the treatBill to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/treat-bills/{id}")
    @Timed
    public ResponseEntity<Void> deleteTreatBill(@PathVariable Long id) {
        log.debug("REST request to delete TreatBill : {}", id);
        treatBillRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
