package com.pgdit.web.rest;

import com.pgdit.ClinicApp;

import com.pgdit.domain.FinalBill;
import com.pgdit.repository.FinalBillRepository;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.pgdit.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FinalBillResource REST controller.
 *
 * @see FinalBillResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClinicApp.class)
public class FinalBillResourceIntTest {

    private static final String DEFAULT_BILL_NO = "AAAAAAAAAA";
    private static final String UPDATED_BILL_NO = "BBBBBBBBBB";

    private static final Double DEFAULT_TREATMENT_CHARGE = 1D;
    private static final Double UPDATED_TREATMENT_CHARGE = 2D;

    private static final String DEFAULT_TREATMENT_RECEIVED = "AAAAAAAAAA";
    private static final String UPDATED_TREATMENT_RECEIVED = "BBBBBBBBBB";

    private static final Double DEFAULT_TREATMENT_BALANCE = 1D;
    private static final Double UPDATED_TREATMENT_BALANCE = 2D;

    private static final Double DEFAULT_BED_CHARGE = 1D;
    private static final Double UPDATED_BED_CHARGE = 2D;

    private static final Double DEFAULT_NURSING_CHARGE = 1D;
    private static final Double UPDATED_NURSING_CHARGE = 2D;

    private static final Double DEFAULT_MEDICINE_CHARGE = 1D;
    private static final Double UPDATED_MEDICINE_CHARGE = 2D;

    private static final String DEFAULT_DOCTOR_VISIT = "AAAAAAAAAA";
    private static final String UPDATED_DOCTOR_VISIT = "BBBBBBBBBB";

    private static final String DEFAULT_OPERATION = "AAAAAAAAAA";
    private static final String UPDATED_OPERATION = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private FinalBillRepository finalBillRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFinalBillMockMvc;

    private FinalBill finalBill;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FinalBillResource finalBillResource = new FinalBillResource(finalBillRepository);
        this.restFinalBillMockMvc = MockMvcBuilders.standaloneSetup(finalBillResource)
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
    public static FinalBill createEntity(EntityManager em) {
        FinalBill finalBill = new FinalBill()
            .billNo(DEFAULT_BILL_NO)
            .treatmentCharge(DEFAULT_TREATMENT_CHARGE)
            .treatmentReceived(DEFAULT_TREATMENT_RECEIVED)
            .treatmentBalance(DEFAULT_TREATMENT_BALANCE)
            .bedCharge(DEFAULT_BED_CHARGE)
            .nursingCharge(DEFAULT_NURSING_CHARGE)
            .medicineCharge(DEFAULT_MEDICINE_CHARGE)
            .doctorVisit(DEFAULT_DOCTOR_VISIT)
            .operation(DEFAULT_OPERATION)
            .Date(DEFAULT_DATE);
        return finalBill;
    }

    @Before
    public void initTest() {
        finalBill = createEntity(em);
    }

    @Test
    @Transactional
    public void createFinalBill() throws Exception {
        int databaseSizeBeforeCreate = finalBillRepository.findAll().size();

        // Create the FinalBill
        restFinalBillMockMvc.perform(post("/api/final-bills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(finalBill)))
            .andExpect(status().isCreated());

        // Validate the FinalBill in the database
        List<FinalBill> finalBillList = finalBillRepository.findAll();
        assertThat(finalBillList).hasSize(databaseSizeBeforeCreate + 1);
        FinalBill testFinalBill = finalBillList.get(finalBillList.size() - 1);
        assertThat(testFinalBill.getBillNo()).isEqualTo(DEFAULT_BILL_NO);
        assertThat(testFinalBill.getTreatmentCharge()).isEqualTo(DEFAULT_TREATMENT_CHARGE);
        assertThat(testFinalBill.getTreatmentReceived()).isEqualTo(DEFAULT_TREATMENT_RECEIVED);
        assertThat(testFinalBill.getTreatmentBalance()).isEqualTo(DEFAULT_TREATMENT_BALANCE);
        assertThat(testFinalBill.getBedCharge()).isEqualTo(DEFAULT_BED_CHARGE);
        assertThat(testFinalBill.getNursingCharge()).isEqualTo(DEFAULT_NURSING_CHARGE);
        assertThat(testFinalBill.getMedicineCharge()).isEqualTo(DEFAULT_MEDICINE_CHARGE);
        assertThat(testFinalBill.getDoctorVisit()).isEqualTo(DEFAULT_DOCTOR_VISIT);
        assertThat(testFinalBill.getOperation()).isEqualTo(DEFAULT_OPERATION);
        assertThat(testFinalBill.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createFinalBillWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = finalBillRepository.findAll().size();

        // Create the FinalBill with an existing ID
        finalBill.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFinalBillMockMvc.perform(post("/api/final-bills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(finalBill)))
            .andExpect(status().isBadRequest());

        // Validate the FinalBill in the database
        List<FinalBill> finalBillList = finalBillRepository.findAll();
        assertThat(finalBillList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFinalBills() throws Exception {
        // Initialize the database
        finalBillRepository.saveAndFlush(finalBill);

        // Get all the finalBillList
        restFinalBillMockMvc.perform(get("/api/final-bills?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(finalBill.getId().intValue())))
            .andExpect(jsonPath("$.[*].billNo").value(hasItem(DEFAULT_BILL_NO.toString())))
            .andExpect(jsonPath("$.[*].treatmentCharge").value(hasItem(DEFAULT_TREATMENT_CHARGE.doubleValue())))
            .andExpect(jsonPath("$.[*].treatmentReceived").value(hasItem(DEFAULT_TREATMENT_RECEIVED.toString())))
            .andExpect(jsonPath("$.[*].treatmentBalance").value(hasItem(DEFAULT_TREATMENT_BALANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].bedCharge").value(hasItem(DEFAULT_BED_CHARGE.doubleValue())))
            .andExpect(jsonPath("$.[*].nursingCharge").value(hasItem(DEFAULT_NURSING_CHARGE.doubleValue())))
            .andExpect(jsonPath("$.[*].medicineCharge").value(hasItem(DEFAULT_MEDICINE_CHARGE.doubleValue())))
            .andExpect(jsonPath("$.[*].doctorVisit").value(hasItem(DEFAULT_DOCTOR_VISIT.toString())))
            .andExpect(jsonPath("$.[*].operation").value(hasItem(DEFAULT_OPERATION.toString())))
            .andExpect(jsonPath("$.[*].Date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    public void getFinalBill() throws Exception {
        // Initialize the database
        finalBillRepository.saveAndFlush(finalBill);

        // Get the finalBill
        restFinalBillMockMvc.perform(get("/api/final-bills/{id}", finalBill.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(finalBill.getId().intValue()))
            .andExpect(jsonPath("$.billNo").value(DEFAULT_BILL_NO.toString()))
            .andExpect(jsonPath("$.treatmentCharge").value(DEFAULT_TREATMENT_CHARGE.doubleValue()))
            .andExpect(jsonPath("$.treatmentReceived").value(DEFAULT_TREATMENT_RECEIVED.toString()))
            .andExpect(jsonPath("$.treatmentBalance").value(DEFAULT_TREATMENT_BALANCE.doubleValue()))
            .andExpect(jsonPath("$.bedCharge").value(DEFAULT_BED_CHARGE.doubleValue()))
            .andExpect(jsonPath("$.nursingCharge").value(DEFAULT_NURSING_CHARGE.doubleValue()))
            .andExpect(jsonPath("$.medicineCharge").value(DEFAULT_MEDICINE_CHARGE.doubleValue()))
            .andExpect(jsonPath("$.doctorVisit").value(DEFAULT_DOCTOR_VISIT.toString()))
            .andExpect(jsonPath("$.operation").value(DEFAULT_OPERATION.toString()))
            .andExpect(jsonPath("$.Date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFinalBill() throws Exception {
        // Get the finalBill
        restFinalBillMockMvc.perform(get("/api/final-bills/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFinalBill() throws Exception {
        // Initialize the database
        finalBillRepository.saveAndFlush(finalBill);
        int databaseSizeBeforeUpdate = finalBillRepository.findAll().size();

        // Update the finalBill
        FinalBill updatedFinalBill = finalBillRepository.findOne(finalBill.getId());
        updatedFinalBill
            .billNo(UPDATED_BILL_NO)
            .treatmentCharge(UPDATED_TREATMENT_CHARGE)
            .treatmentReceived(UPDATED_TREATMENT_RECEIVED)
            .treatmentBalance(UPDATED_TREATMENT_BALANCE)
            .bedCharge(UPDATED_BED_CHARGE)
            .nursingCharge(UPDATED_NURSING_CHARGE)
            .medicineCharge(UPDATED_MEDICINE_CHARGE)
            .doctorVisit(UPDATED_DOCTOR_VISIT)
            .operation(UPDATED_OPERATION)
            .Date(UPDATED_DATE);

        restFinalBillMockMvc.perform(put("/api/final-bills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFinalBill)))
            .andExpect(status().isOk());

        // Validate the FinalBill in the database
        List<FinalBill> finalBillList = finalBillRepository.findAll();
        assertThat(finalBillList).hasSize(databaseSizeBeforeUpdate);
        FinalBill testFinalBill = finalBillList.get(finalBillList.size() - 1);
        assertThat(testFinalBill.getBillNo()).isEqualTo(UPDATED_BILL_NO);
        assertThat(testFinalBill.getTreatmentCharge()).isEqualTo(UPDATED_TREATMENT_CHARGE);
        assertThat(testFinalBill.getTreatmentReceived()).isEqualTo(UPDATED_TREATMENT_RECEIVED);
        assertThat(testFinalBill.getTreatmentBalance()).isEqualTo(UPDATED_TREATMENT_BALANCE);
        assertThat(testFinalBill.getBedCharge()).isEqualTo(UPDATED_BED_CHARGE);
        assertThat(testFinalBill.getNursingCharge()).isEqualTo(UPDATED_NURSING_CHARGE);
        assertThat(testFinalBill.getMedicineCharge()).isEqualTo(UPDATED_MEDICINE_CHARGE);
        assertThat(testFinalBill.getDoctorVisit()).isEqualTo(UPDATED_DOCTOR_VISIT);
        assertThat(testFinalBill.getOperation()).isEqualTo(UPDATED_OPERATION);
        assertThat(testFinalBill.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingFinalBill() throws Exception {
        int databaseSizeBeforeUpdate = finalBillRepository.findAll().size();

        // Create the FinalBill

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFinalBillMockMvc.perform(put("/api/final-bills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(finalBill)))
            .andExpect(status().isCreated());

        // Validate the FinalBill in the database
        List<FinalBill> finalBillList = finalBillRepository.findAll();
        assertThat(finalBillList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFinalBill() throws Exception {
        // Initialize the database
        finalBillRepository.saveAndFlush(finalBill);
        int databaseSizeBeforeDelete = finalBillRepository.findAll().size();

        // Get the finalBill
        restFinalBillMockMvc.perform(delete("/api/final-bills/{id}", finalBill.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FinalBill> finalBillList = finalBillRepository.findAll();
        assertThat(finalBillList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FinalBill.class);
        FinalBill finalBill1 = new FinalBill();
        finalBill1.setId(1L);
        FinalBill finalBill2 = new FinalBill();
        finalBill2.setId(finalBill1.getId());
        assertThat(finalBill1).isEqualTo(finalBill2);
        finalBill2.setId(2L);
        assertThat(finalBill1).isNotEqualTo(finalBill2);
        finalBill1.setId(null);
        assertThat(finalBill1).isNotEqualTo(finalBill2);
    }
}
