package com.pgdit.web.rest;

import com.pgdit.ClinicApp;

import com.pgdit.domain.TransferIntimation;
import com.pgdit.repository.TransferIntimationRepository;
import com.pgdit.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.pgdit.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TransferIntimationResource REST controller.
 *
 * @see TransferIntimationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClinicApp.class)
public class TransferIntimationResourceIntTest {

    private static final String DEFAULT_REF_DOCTOR = "AAAAAAAAAA";
    private static final String UPDATED_REF_DOCTOR = "BBBBBBBBBB";

    private static final Integer DEFAULT_FROM_BED_CODE = 1;
    private static final Integer UPDATED_FROM_BED_CODE = 2;

    private static final Integer DEFAULT_FROM_BED_NO = 1;
    private static final Integer UPDATED_FROM_BED_NO = 2;

    private static final Integer DEFAULT_TO_BED_CODE = 1;
    private static final Integer UPDATED_TO_BED_CODE = 2;

    private static final Integer DEFAULT_TO_BED_NO = 1;
    private static final Integer UPDATED_TO_BED_NO = 2;

    private static final Double DEFAULT_CHARGE = 1D;
    private static final Double UPDATED_CHARGE = 2D;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private TransferIntimationRepository transferIntimationRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTransferIntimationMockMvc;

    private TransferIntimation transferIntimation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TransferIntimationResource transferIntimationResource = new TransferIntimationResource(transferIntimationRepository);
        this.restTransferIntimationMockMvc = MockMvcBuilders.standaloneSetup(transferIntimationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransferIntimation createEntity(EntityManager em) {
        TransferIntimation transferIntimation = new TransferIntimation()
            .refDoctor(DEFAULT_REF_DOCTOR)
            .fromBedCode(DEFAULT_FROM_BED_CODE)
            .fromBedNo(DEFAULT_FROM_BED_NO)
            .toBedCode(DEFAULT_TO_BED_CODE)
            .toBedNo(DEFAULT_TO_BED_NO)
            .charge(DEFAULT_CHARGE)
            .date(DEFAULT_DATE);
        return transferIntimation;
    }

    @Before
    public void initTest() {
        transferIntimation = createEntity(em);
    }

    @Test
    @Transactional
    public void createTransferIntimation() throws Exception {
        int databaseSizeBeforeCreate = transferIntimationRepository.findAll().size();

        // Create the TransferIntimation
        restTransferIntimationMockMvc.perform(post("/api/transfer-intimations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferIntimation)))
            .andExpect(status().isCreated());

        // Validate the TransferIntimation in the database
        List<TransferIntimation> transferIntimationList = transferIntimationRepository.findAll();
        assertThat(transferIntimationList).hasSize(databaseSizeBeforeCreate + 1);
        TransferIntimation testTransferIntimation = transferIntimationList.get(transferIntimationList.size() - 1);
        assertThat(testTransferIntimation.getRefDoctor()).isEqualTo(DEFAULT_REF_DOCTOR);
        assertThat(testTransferIntimation.getFromBedCode()).isEqualTo(DEFAULT_FROM_BED_CODE);
        assertThat(testTransferIntimation.getFromBedNo()).isEqualTo(DEFAULT_FROM_BED_NO);
        assertThat(testTransferIntimation.getToBedCode()).isEqualTo(DEFAULT_TO_BED_CODE);
        assertThat(testTransferIntimation.getToBedNo()).isEqualTo(DEFAULT_TO_BED_NO);
        assertThat(testTransferIntimation.getCharge()).isEqualTo(DEFAULT_CHARGE);
        assertThat(testTransferIntimation.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createTransferIntimationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transferIntimationRepository.findAll().size();

        // Create the TransferIntimation with an existing ID
        transferIntimation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransferIntimationMockMvc.perform(post("/api/transfer-intimations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferIntimation)))
            .andExpect(status().isBadRequest());

        // Validate the TransferIntimation in the database
        List<TransferIntimation> transferIntimationList = transferIntimationRepository.findAll();
        assertThat(transferIntimationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTransferIntimations() throws Exception {
        // Initialize the database
        transferIntimationRepository.saveAndFlush(transferIntimation);

        // Get all the transferIntimationList
        restTransferIntimationMockMvc.perform(get("/api/transfer-intimations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transferIntimation.getId().intValue())))
            .andExpect(jsonPath("$.[*].refDoctor").value(hasItem(DEFAULT_REF_DOCTOR.toString())))
            .andExpect(jsonPath("$.[*].fromBedCode").value(hasItem(DEFAULT_FROM_BED_CODE)))
            .andExpect(jsonPath("$.[*].fromBedNo").value(hasItem(DEFAULT_FROM_BED_NO)))
            .andExpect(jsonPath("$.[*].toBedCode").value(hasItem(DEFAULT_TO_BED_CODE)))
            .andExpect(jsonPath("$.[*].toBedNo").value(hasItem(DEFAULT_TO_BED_NO)))
            .andExpect(jsonPath("$.[*].charge").value(hasItem(DEFAULT_CHARGE.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    public void getTransferIntimation() throws Exception {
        // Initialize the database
        transferIntimationRepository.saveAndFlush(transferIntimation);

        // Get the transferIntimation
        restTransferIntimationMockMvc.perform(get("/api/transfer-intimations/{id}", transferIntimation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transferIntimation.getId().intValue()))
            .andExpect(jsonPath("$.refDoctor").value(DEFAULT_REF_DOCTOR.toString()))
            .andExpect(jsonPath("$.fromBedCode").value(DEFAULT_FROM_BED_CODE))
            .andExpect(jsonPath("$.fromBedNo").value(DEFAULT_FROM_BED_NO))
            .andExpect(jsonPath("$.toBedCode").value(DEFAULT_TO_BED_CODE))
            .andExpect(jsonPath("$.toBedNo").value(DEFAULT_TO_BED_NO))
            .andExpect(jsonPath("$.charge").value(DEFAULT_CHARGE.doubleValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTransferIntimation() throws Exception {
        // Get the transferIntimation
        restTransferIntimationMockMvc.perform(get("/api/transfer-intimations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransferIntimation() throws Exception {
        // Initialize the database
        transferIntimationRepository.saveAndFlush(transferIntimation);
        int databaseSizeBeforeUpdate = transferIntimationRepository.findAll().size();

        // Update the transferIntimation
        TransferIntimation updatedTransferIntimation = transferIntimationRepository.findOne(transferIntimation.getId());
        updatedTransferIntimation
            .refDoctor(UPDATED_REF_DOCTOR)
            .fromBedCode(UPDATED_FROM_BED_CODE)
            .fromBedNo(UPDATED_FROM_BED_NO)
            .toBedCode(UPDATED_TO_BED_CODE)
            .toBedNo(UPDATED_TO_BED_NO)
            .charge(UPDATED_CHARGE)
            .date(UPDATED_DATE);

        restTransferIntimationMockMvc.perform(put("/api/transfer-intimations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTransferIntimation)))
            .andExpect(status().isOk());

        // Validate the TransferIntimation in the database
        List<TransferIntimation> transferIntimationList = transferIntimationRepository.findAll();
        assertThat(transferIntimationList).hasSize(databaseSizeBeforeUpdate);
        TransferIntimation testTransferIntimation = transferIntimationList.get(transferIntimationList.size() - 1);
        assertThat(testTransferIntimation.getRefDoctor()).isEqualTo(UPDATED_REF_DOCTOR);
        assertThat(testTransferIntimation.getFromBedCode()).isEqualTo(UPDATED_FROM_BED_CODE);
        assertThat(testTransferIntimation.getFromBedNo()).isEqualTo(UPDATED_FROM_BED_NO);
        assertThat(testTransferIntimation.getToBedCode()).isEqualTo(UPDATED_TO_BED_CODE);
        assertThat(testTransferIntimation.getToBedNo()).isEqualTo(UPDATED_TO_BED_NO);
        assertThat(testTransferIntimation.getCharge()).isEqualTo(UPDATED_CHARGE);
        assertThat(testTransferIntimation.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingTransferIntimation() throws Exception {
        int databaseSizeBeforeUpdate = transferIntimationRepository.findAll().size();

        // Create the TransferIntimation

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTransferIntimationMockMvc.perform(put("/api/transfer-intimations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferIntimation)))
            .andExpect(status().isCreated());

        // Validate the TransferIntimation in the database
        List<TransferIntimation> transferIntimationList = transferIntimationRepository.findAll();
        assertThat(transferIntimationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTransferIntimation() throws Exception {
        // Initialize the database
        transferIntimationRepository.saveAndFlush(transferIntimation);
        int databaseSizeBeforeDelete = transferIntimationRepository.findAll().size();

        // Get the transferIntimation
        restTransferIntimationMockMvc.perform(delete("/api/transfer-intimations/{id}", transferIntimation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TransferIntimation> transferIntimationList = transferIntimationRepository.findAll();
        assertThat(transferIntimationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransferIntimation.class);
        TransferIntimation transferIntimation1 = new TransferIntimation();
        transferIntimation1.setId(1L);
        TransferIntimation transferIntimation2 = new TransferIntimation();
        transferIntimation2.setId(transferIntimation1.getId());
        assertThat(transferIntimation1).isEqualTo(transferIntimation2);
        transferIntimation2.setId(2L);
        assertThat(transferIntimation1).isNotEqualTo(transferIntimation2);
        transferIntimation1.setId(null);
        assertThat(transferIntimation1).isNotEqualTo(transferIntimation2);
    }
}
