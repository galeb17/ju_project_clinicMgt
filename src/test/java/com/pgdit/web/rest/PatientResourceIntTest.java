package com.pgdit.web.rest;

import com.pgdit.ClinicApp;

import com.pgdit.domain.Patient;
import com.pgdit.repository.PatientRepository;
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

import com.pgdit.domain.enumeration.Gender;
/**
 * Test class for the PatientResource REST controller.
 *
 * @see PatientResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClinicApp.class)
public class PatientResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BED_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BED_NO = "AAAAAAAAAA";
    private static final String UPDATED_BED_NO = "BBBBBBBBBB";

    private static final String DEFAULT_ENTRY_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ENTRY_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DISCHARGE_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_DISCHARGE_STATUS = "BBBBBBBBBB";

    private static final Instant DEFAULT_DISCHARGE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DISCHARGE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ADMISSION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ADMISSION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final Gender DEFAULT_GENDER = Gender.male;
    private static final Gender UPDATED_GENDER = Gender.female;

    private static final Double DEFAULT_AGE = 1D;
    private static final Double UPDATED_AGE = 2D;

    private static final String DEFAULT_CONTACT_NO = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NO = "BBBBBBBBBB";

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPatientMockMvc;

    private Patient patient;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PatientResource patientResource = new PatientResource(patientRepository);
        this.restPatientMockMvc = MockMvcBuilders.standaloneSetup(patientResource)
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
    public static Patient createEntity(EntityManager em) {
        Patient patient = new Patient()
            .name(DEFAULT_NAME)
            .bed_Code(DEFAULT_BED_CODE)
            .bed_No(DEFAULT_BED_NO)
            .entry_Type(DEFAULT_ENTRY_TYPE)
            .discharge_Status(DEFAULT_DISCHARGE_STATUS)
            .discharge_Date(DEFAULT_DISCHARGE_DATE)
            .admission_Date(DEFAULT_ADMISSION_DATE)
            .address(DEFAULT_ADDRESS)
            .gender(DEFAULT_GENDER)
            .age(DEFAULT_AGE)
            .contactNo(DEFAULT_CONTACT_NO);
        return patient;
    }

    @Before
    public void initTest() {
        patient = createEntity(em);
    }

    @Test
    @Transactional
    public void createPatient() throws Exception {
        int databaseSizeBeforeCreate = patientRepository.findAll().size();

        // Create the Patient
        restPatientMockMvc.perform(post("/api/patients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isCreated());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeCreate + 1);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPatient.getBed_Code()).isEqualTo(DEFAULT_BED_CODE);
        assertThat(testPatient.getBed_No()).isEqualTo(DEFAULT_BED_NO);
        assertThat(testPatient.getEntry_Type()).isEqualTo(DEFAULT_ENTRY_TYPE);
        assertThat(testPatient.getDischarge_Status()).isEqualTo(DEFAULT_DISCHARGE_STATUS);
        assertThat(testPatient.getDischarge_Date()).isEqualTo(DEFAULT_DISCHARGE_DATE);
        assertThat(testPatient.getAdmission_Date()).isEqualTo(DEFAULT_ADMISSION_DATE);
        assertThat(testPatient.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testPatient.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testPatient.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testPatient.getContactNo()).isEqualTo(DEFAULT_CONTACT_NO);
    }

    @Test
    @Transactional
    public void createPatientWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = patientRepository.findAll().size();

        // Create the Patient with an existing ID
        patient.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPatientMockMvc.perform(post("/api/patients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPatients() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList
        restPatientMockMvc.perform(get("/api/patients?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patient.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].bed_Code").value(hasItem(DEFAULT_BED_CODE.toString())))
            .andExpect(jsonPath("$.[*].bed_No").value(hasItem(DEFAULT_BED_NO.toString())))
            .andExpect(jsonPath("$.[*].entry_Type").value(hasItem(DEFAULT_ENTRY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].discharge_Status").value(hasItem(DEFAULT_DISCHARGE_STATUS.toString())))
            .andExpect(jsonPath("$.[*].discharge_Date").value(hasItem(DEFAULT_DISCHARGE_DATE.toString())))
            .andExpect(jsonPath("$.[*].admission_Date").value(hasItem(DEFAULT_ADMISSION_DATE.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE.doubleValue())))
            .andExpect(jsonPath("$.[*].contactNo").value(hasItem(DEFAULT_CONTACT_NO.toString())));
    }

    @Test
    @Transactional
    public void getPatient() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get the patient
        restPatientMockMvc.perform(get("/api/patients/{id}", patient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(patient.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.bed_Code").value(DEFAULT_BED_CODE.toString()))
            .andExpect(jsonPath("$.bed_No").value(DEFAULT_BED_NO.toString()))
            .andExpect(jsonPath("$.entry_Type").value(DEFAULT_ENTRY_TYPE.toString()))
            .andExpect(jsonPath("$.discharge_Status").value(DEFAULT_DISCHARGE_STATUS.toString()))
            .andExpect(jsonPath("$.discharge_Date").value(DEFAULT_DISCHARGE_DATE.toString()))
            .andExpect(jsonPath("$.admission_Date").value(DEFAULT_ADMISSION_DATE.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE.doubleValue()))
            .andExpect(jsonPath("$.contactNo").value(DEFAULT_CONTACT_NO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPatient() throws Exception {
        // Get the patient
        restPatientMockMvc.perform(get("/api/patients/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePatient() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();

        // Update the patient
        Patient updatedPatient = patientRepository.findOne(patient.getId());
        updatedPatient
            .name(UPDATED_NAME)
            .bed_Code(UPDATED_BED_CODE)
            .bed_No(UPDATED_BED_NO)
            .entry_Type(UPDATED_ENTRY_TYPE)
            .discharge_Status(UPDATED_DISCHARGE_STATUS)
            .discharge_Date(UPDATED_DISCHARGE_DATE)
            .admission_Date(UPDATED_ADMISSION_DATE)
            .address(UPDATED_ADDRESS)
            .gender(UPDATED_GENDER)
            .age(UPDATED_AGE)
            .contactNo(UPDATED_CONTACT_NO);

        restPatientMockMvc.perform(put("/api/patients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPatient)))
            .andExpect(status().isOk());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPatient.getBed_Code()).isEqualTo(UPDATED_BED_CODE);
        assertThat(testPatient.getBed_No()).isEqualTo(UPDATED_BED_NO);
        assertThat(testPatient.getEntry_Type()).isEqualTo(UPDATED_ENTRY_TYPE);
        assertThat(testPatient.getDischarge_Status()).isEqualTo(UPDATED_DISCHARGE_STATUS);
        assertThat(testPatient.getDischarge_Date()).isEqualTo(UPDATED_DISCHARGE_DATE);
        assertThat(testPatient.getAdmission_Date()).isEqualTo(UPDATED_ADMISSION_DATE);
        assertThat(testPatient.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testPatient.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testPatient.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testPatient.getContactNo()).isEqualTo(UPDATED_CONTACT_NO);
    }

    @Test
    @Transactional
    public void updateNonExistingPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();

        // Create the Patient

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPatientMockMvc.perform(put("/api/patients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isCreated());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePatient() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);
        int databaseSizeBeforeDelete = patientRepository.findAll().size();

        // Get the patient
        restPatientMockMvc.perform(delete("/api/patients/{id}", patient.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Patient.class);
        Patient patient1 = new Patient();
        patient1.setId(1L);
        Patient patient2 = new Patient();
        patient2.setId(patient1.getId());
        assertThat(patient1).isEqualTo(patient2);
        patient2.setId(2L);
        assertThat(patient1).isNotEqualTo(patient2);
        patient1.setId(null);
        assertThat(patient1).isNotEqualTo(patient2);
    }
}
