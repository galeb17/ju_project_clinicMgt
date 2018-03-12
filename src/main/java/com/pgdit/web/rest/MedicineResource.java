package com.pgdit.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pgdit.domain.Medicine;

import com.pgdit.repository.MedicineRepository;
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
 * REST controller for managing Medicine.
 */
@RestController
@RequestMapping("/api")
public class MedicineResource {

    private final Logger log = LoggerFactory.getLogger(MedicineResource.class);

    private static final String ENTITY_NAME = "medicine";

    private final MedicineRepository medicineRepository;

    public MedicineResource(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    /**
     * POST  /medicines : Create a new medicine.
     *
     * @param medicine the medicine to create
     * @return the ResponseEntity with status 201 (Created) and with body the new medicine, or with status 400 (Bad Request) if the medicine has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/medicines")
    @Timed
    public ResponseEntity<Medicine> createMedicine(@RequestBody Medicine medicine) throws URISyntaxException {
        log.debug("REST request to save Medicine : {}", medicine);
        if (medicine.getId() != null) {
            throw new BadRequestAlertException("A new medicine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Medicine result = medicineRepository.save(medicine);
        return ResponseEntity.created(new URI("/api/medicines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /medicines : Updates an existing medicine.
     *
     * @param medicine the medicine to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated medicine,
     * or with status 400 (Bad Request) if the medicine is not valid,
     * or with status 500 (Internal Server Error) if the medicine couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/medicines")
    @Timed
    public ResponseEntity<Medicine> updateMedicine(@RequestBody Medicine medicine) throws URISyntaxException {
        log.debug("REST request to update Medicine : {}", medicine);
        if (medicine.getId() == null) {
            return createMedicine(medicine);
        }
        Medicine result = medicineRepository.save(medicine);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, medicine.getId().toString()))
            .body(result);
    }

    /**
     * GET  /medicines : get all the medicines.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of medicines in body
     */
    @GetMapping("/medicines")
    @Timed
    public ResponseEntity<List<Medicine>> getAllMedicines(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Medicines");
        Page<Medicine> page = medicineRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/medicines");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /medicines/:id : get the "id" medicine.
     *
     * @param id the id of the medicine to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the medicine, or with status 404 (Not Found)
     */
    @GetMapping("/medicines/{id}")
    @Timed
    public ResponseEntity<Medicine> getMedicine(@PathVariable Long id) {
        log.debug("REST request to get Medicine : {}", id);
        Medicine medicine = medicineRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(medicine));
    }

    /**
     * DELETE  /medicines/:id : delete the "id" medicine.
     *
     * @param id the id of the medicine to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/medicines/{id}")
    @Timed
    public ResponseEntity<Void> deleteMedicine(@PathVariable Long id) {
        log.debug("REST request to delete Medicine : {}", id);
        medicineRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
