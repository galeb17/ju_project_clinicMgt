package com.pgdit.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pgdit.domain.TransferIntimation;

import com.pgdit.repository.TransferIntimationRepository;
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
 * REST controller for managing TransferIntimation.
 */
@RestController
@RequestMapping("/api")
public class TransferIntimationResource {

    private final Logger log = LoggerFactory.getLogger(TransferIntimationResource.class);

    private static final String ENTITY_NAME = "transferIntimation";

    private final TransferIntimationRepository transferIntimationRepository;

    public TransferIntimationResource(TransferIntimationRepository transferIntimationRepository) {
        this.transferIntimationRepository = transferIntimationRepository;
    }

    /**
     * POST  /transfer-intimations : Create a new transferIntimation.
     *
     * @param transferIntimation the transferIntimation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transferIntimation, or with status 400 (Bad Request) if the transferIntimation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/transfer-intimations")
    @Timed
    public ResponseEntity<TransferIntimation> createTransferIntimation(@RequestBody TransferIntimation transferIntimation) throws URISyntaxException {
        log.debug("REST request to save TransferIntimation : {}", transferIntimation);
        if (transferIntimation.getId() != null) {
            throw new BadRequestAlertException("A new transferIntimation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransferIntimation result = transferIntimationRepository.save(transferIntimation);
        return ResponseEntity.created(new URI("/api/transfer-intimations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /transfer-intimations : Updates an existing transferIntimation.
     *
     * @param transferIntimation the transferIntimation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transferIntimation,
     * or with status 400 (Bad Request) if the transferIntimation is not valid,
     * or with status 500 (Internal Server Error) if the transferIntimation couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/transfer-intimations")
    @Timed
    public ResponseEntity<TransferIntimation> updateTransferIntimation(@RequestBody TransferIntimation transferIntimation) throws URISyntaxException {
        log.debug("REST request to update TransferIntimation : {}", transferIntimation);
        if (transferIntimation.getId() == null) {
            return createTransferIntimation(transferIntimation);
        }
        TransferIntimation result = transferIntimationRepository.save(transferIntimation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transferIntimation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /transfer-intimations : get all the transferIntimations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of transferIntimations in body
     */
    @GetMapping("/transfer-intimations")
    @Timed
    public ResponseEntity<List<TransferIntimation>> getAllTransferIntimations(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of TransferIntimations");
        Page<TransferIntimation> page = transferIntimationRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/transfer-intimations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /transfer-intimations/:id : get the "id" transferIntimation.
     *
     * @param id the id of the transferIntimation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transferIntimation, or with status 404 (Not Found)
     */
    @GetMapping("/transfer-intimations/{id}")
    @Timed
    public ResponseEntity<TransferIntimation> getTransferIntimation(@PathVariable Long id) {
        log.debug("REST request to get TransferIntimation : {}", id);
        TransferIntimation transferIntimation = transferIntimationRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(transferIntimation));
    }

    /**
     * DELETE  /transfer-intimations/:id : delete the "id" transferIntimation.
     *
     * @param id the id of the transferIntimation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/transfer-intimations/{id}")
    @Timed
    public ResponseEntity<Void> deleteTransferIntimation(@PathVariable Long id) {
        log.debug("REST request to delete TransferIntimation : {}", id);
        transferIntimationRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
