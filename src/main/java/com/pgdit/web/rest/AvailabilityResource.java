package com.pgdit.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pgdit.domain.Availability;

import com.pgdit.repository.AvailabilityRepository;
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
 * REST controller for managing Availability.
 */
@RestController
@RequestMapping("/api")
public class AvailabilityResource {

    private final Logger log = LoggerFactory.getLogger(AvailabilityResource.class);

    private static final String ENTITY_NAME = "availability";

    private final AvailabilityRepository availabilityRepository;

    public AvailabilityResource(AvailabilityRepository availabilityRepository) {
        this.availabilityRepository = availabilityRepository;
    }

    /**
     * POST  /availabilities : Create a new availability.
     *
     * @param availability the availability to create
     * @return the ResponseEntity with status 201 (Created) and with body the new availability, or with status 400 (Bad Request) if the availability has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/availabilities")
    @Timed
    public ResponseEntity<Availability> createAvailability(@RequestBody Availability availability) throws URISyntaxException {
        log.debug("REST request to save Availability : {}", availability);
        if (availability.getId() != null) {
            throw new BadRequestAlertException("A new availability cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Availability result = availabilityRepository.save(availability);
        return ResponseEntity.created(new URI("/api/availabilities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /availabilities : Updates an existing availability.
     *
     * @param availability the availability to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated availability,
     * or with status 400 (Bad Request) if the availability is not valid,
     * or with status 500 (Internal Server Error) if the availability couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/availabilities")
    @Timed
    public ResponseEntity<Availability> updateAvailability(@RequestBody Availability availability) throws URISyntaxException {
        log.debug("REST request to update Availability : {}", availability);
        if (availability.getId() == null) {
            return createAvailability(availability);
        }
        Availability result = availabilityRepository.save(availability);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, availability.getId().toString()))
            .body(result);
    }

    /**
     * GET  /availabilities : get all the availabilities.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of availabilities in body
     */
    @GetMapping("/availabilities")
    @Timed
    public ResponseEntity<List<Availability>> getAllAvailabilities(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Availabilities");
        Page<Availability> page = availabilityRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/availabilities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /availabilities/:id : get the "id" availability.
     *
     * @param id the id of the availability to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the availability, or with status 404 (Not Found)
     */
    @GetMapping("/availabilities/{id}")
    @Timed
    public ResponseEntity<Availability> getAvailability(@PathVariable Long id) {
        log.debug("REST request to get Availability : {}", id);
        Availability availability = availabilityRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(availability));
    }

    /**
     * DELETE  /availabilities/:id : delete the "id" availability.
     *
     * @param id the id of the availability to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/availabilities/{id}")
    @Timed
    public ResponseEntity<Void> deleteAvailability(@PathVariable Long id) {
        log.debug("REST request to delete Availability : {}", id);
        availabilityRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
