package com.pgdit.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pgdit.domain.AppointSchedule;

import com.pgdit.repository.AppointScheduleRepository;
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
 * REST controller for managing AppointSchedule.
 */
@RestController
@RequestMapping("/api")
public class AppointScheduleResource {

    private final Logger log = LoggerFactory.getLogger(AppointScheduleResource.class);

    private static final String ENTITY_NAME = "appointSchedule";

    private final AppointScheduleRepository appointScheduleRepository;

    public AppointScheduleResource(AppointScheduleRepository appointScheduleRepository) {
        this.appointScheduleRepository = appointScheduleRepository;
    }

    /**
     * POST  /appoint-schedules : Create a new appointSchedule.
     *
     * @param appointSchedule the appointSchedule to create
     * @return the ResponseEntity with status 201 (Created) and with body the new appointSchedule, or with status 400 (Bad Request) if the appointSchedule has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/appoint-schedules")
    @Timed
    public ResponseEntity<AppointSchedule> createAppointSchedule(@RequestBody AppointSchedule appointSchedule) throws URISyntaxException {
        log.debug("REST request to save AppointSchedule : {}", appointSchedule);
        if (appointSchedule.getId() != null) {
            throw new BadRequestAlertException("A new appointSchedule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AppointSchedule result = appointScheduleRepository.save(appointSchedule);
        return ResponseEntity.created(new URI("/api/appoint-schedules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /appoint-schedules : Updates an existing appointSchedule.
     *
     * @param appointSchedule the appointSchedule to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated appointSchedule,
     * or with status 400 (Bad Request) if the appointSchedule is not valid,
     * or with status 500 (Internal Server Error) if the appointSchedule couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/appoint-schedules")
    @Timed
    public ResponseEntity<AppointSchedule> updateAppointSchedule(@RequestBody AppointSchedule appointSchedule) throws URISyntaxException {
        log.debug("REST request to update AppointSchedule : {}", appointSchedule);
        if (appointSchedule.getId() == null) {
            return createAppointSchedule(appointSchedule);
        }
        AppointSchedule result = appointScheduleRepository.save(appointSchedule);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, appointSchedule.getId().toString()))
            .body(result);
    }

    /**
     * GET  /appoint-schedules : get all the appointSchedules.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of appointSchedules in body
     */
    @GetMapping("/appoint-schedules")
    @Timed
    public ResponseEntity<List<AppointSchedule>> getAllAppointSchedules(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of AppointSchedules");
        Page<AppointSchedule> page = appointScheduleRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/appoint-schedules");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /appoint-schedules/:id : get the "id" appointSchedule.
     *
     * @param id the id of the appointSchedule to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the appointSchedule, or with status 404 (Not Found)
     */
    @GetMapping("/appoint-schedules/{id}")
    @Timed
    public ResponseEntity<AppointSchedule> getAppointSchedule(@PathVariable Long id) {
        log.debug("REST request to get AppointSchedule : {}", id);
        AppointSchedule appointSchedule = appointScheduleRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(appointSchedule));
    }

    /**
     * DELETE  /appoint-schedules/:id : delete the "id" appointSchedule.
     *
     * @param id the id of the appointSchedule to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/appoint-schedules/{id}")
    @Timed
    public ResponseEntity<Void> deleteAppointSchedule(@PathVariable Long id) {
        log.debug("REST request to delete AppointSchedule : {}", id);
        appointScheduleRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
