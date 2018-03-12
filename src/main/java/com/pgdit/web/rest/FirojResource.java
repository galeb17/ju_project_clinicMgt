package com.pgdit.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pgdit.domain.Firoj;

import com.pgdit.repository.FirojRepository;
import com.pgdit.web.rest.errors.BadRequestAlertException;
import com.pgdit.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Firoj.
 */
@RestController
@RequestMapping("/api")
public class FirojResource {

    private final Logger log = LoggerFactory.getLogger(FirojResource.class);

    private static final String ENTITY_NAME = "firoj";

    private final FirojRepository firojRepository;

    public FirojResource(FirojRepository firojRepository) {
        this.firojRepository = firojRepository;
    }

    /**
     * POST  /firojs : Create a new firoj.
     *
     * @param firoj the firoj to create
     * @return the ResponseEntity with status 201 (Created) and with body the new firoj, or with status 400 (Bad Request) if the firoj has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/firojs")
    @Timed
    public ResponseEntity<Firoj> createFiroj(@RequestBody Firoj firoj) throws URISyntaxException {
        log.debug("REST request to save Firoj : {}", firoj);
        if (firoj.getId() != null) {
            throw new BadRequestAlertException("A new firoj cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Firoj result = firojRepository.save(firoj);
        return ResponseEntity.created(new URI("/api/firojs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /firojs : Updates an existing firoj.
     *
     * @param firoj the firoj to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated firoj,
     * or with status 400 (Bad Request) if the firoj is not valid,
     * or with status 500 (Internal Server Error) if the firoj couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/firojs")
    @Timed
    public ResponseEntity<Firoj> updateFiroj(@RequestBody Firoj firoj) throws URISyntaxException {
        log.debug("REST request to update Firoj : {}", firoj);
        if (firoj.getId() == null) {
            return createFiroj(firoj);
        }
        Firoj result = firojRepository.save(firoj);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, firoj.getId().toString()))
            .body(result);
    }

    /**
     * GET  /firojs : get all the firojs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of firojs in body
     */
    @GetMapping("/firojs")
    @Timed
    public List<Firoj> getAllFirojs() {
        log.debug("REST request to get all Firojs");
        return firojRepository.findAll();
        }

    /**
     * GET  /firojs/:id : get the "id" firoj.
     *
     * @param id the id of the firoj to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the firoj, or with status 404 (Not Found)
     */
    @GetMapping("/firojs/{id}")
    @Timed
    public ResponseEntity<Firoj> getFiroj(@PathVariable Long id) {
        log.debug("REST request to get Firoj : {}", id);
        Firoj firoj = firojRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(firoj));
    }

    /**
     * DELETE  /firojs/:id : delete the "id" firoj.
     *
     * @param id the id of the firoj to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/firojs/{id}")
    @Timed
    public ResponseEntity<Void> deleteFiroj(@PathVariable Long id) {
        log.debug("REST request to delete Firoj : {}", id);
        firojRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
