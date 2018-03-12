package com.pgdit.web.rest;

import com.pgdit.ClinicApp;

import com.pgdit.domain.ClinicalTest;
import com.pgdit.repository.ClinicalTestRepository;
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
import java.util.List;

import static com.pgdit.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ClinicalTestResource REST controller.
 *
 * @see ClinicalTestResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClinicApp.class)
public class ClinicalTestResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TEST_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TEST_TYPE = "BBBBBBBBBB";

    private static final Double DEFAULT_RATE = 1D;
    private static final Double UPDATED_RATE = 2D;

    private static final String DEFAULT_PRE_REQUISITE = "AAAAAAAAAA";
    private static final String UPDATED_PRE_REQUISITE = "BBBBBBBBBB";

    private static final String DEFAULT_CAUTION = "AAAAAAAAAA";
    private static final String UPDATED_CAUTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Autowired
    private ClinicalTestRepository clinicalTestRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restClinicalTestMockMvc;

    private ClinicalTest clinicalTest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ClinicalTestResource clinicalTestResource = new ClinicalTestResource(clinicalTestRepository);
        this.restClinicalTestMockMvc = MockMvcBuilders.standaloneSetup(clinicalTestResource)
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
    public static ClinicalTest createEntity(EntityManager em) {
        ClinicalTest clinicalTest = new ClinicalTest()
            .name (DEFAULT_NAME)
            .testType(DEFAULT_TEST_TYPE)
            .rate(DEFAULT_RATE)
            .preRequisite(DEFAULT_PRE_REQUISITE)
            .caution(DEFAULT_CAUTION)
            .isActive(DEFAULT_IS_ACTIVE);
        return clinicalTest;
    }

    @Before
    public void initTest() {
        clinicalTest = createEntity(em);
    }

    @Test
    @Transactional
    public void createClinicalTest() throws Exception {
        int databaseSizeBeforeCreate = clinicalTestRepository.findAll().size();

        // Create the ClinicalTest
        restClinicalTestMockMvc.perform(post("/api/clinical-tests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clinicalTest)))
            .andExpect(status().isCreated());

        // Validate the ClinicalTest in the database
        List<ClinicalTest> clinicalTestList = clinicalTestRepository.findAll();
        assertThat(clinicalTestList).hasSize(databaseSizeBeforeCreate + 1);
        ClinicalTest testClinicalTest = clinicalTestList.get(clinicalTestList.size() - 1);
        assertThat(testClinicalTest.getName ()).isEqualTo(DEFAULT_NAME);
        assertThat(testClinicalTest.getTestType()).isEqualTo(DEFAULT_TEST_TYPE);
        assertThat(testClinicalTest.getRate()).isEqualTo(DEFAULT_RATE);
        assertThat(testClinicalTest.getPreRequisite()).isEqualTo(DEFAULT_PRE_REQUISITE);
        assertThat(testClinicalTest.getCaution()).isEqualTo(DEFAULT_CAUTION);
        assertThat(testClinicalTest.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void createClinicalTestWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = clinicalTestRepository.findAll().size();

        // Create the ClinicalTest with an existing ID
        clinicalTest.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClinicalTestMockMvc.perform(post("/api/clinical-tests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clinicalTest)))
            .andExpect(status().isBadRequest());

        // Validate the ClinicalTest in the database
        List<ClinicalTest> clinicalTestList = clinicalTestRepository.findAll();
        assertThat(clinicalTestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllClinicalTests() throws Exception {
        // Initialize the database
        clinicalTestRepository.saveAndFlush(clinicalTest);

        // Get all the clinicalTestList
        restClinicalTestMockMvc.perform(get("/api/clinical-tests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clinicalTest.getId().intValue())))
            .andExpect(jsonPath("$.[*].name ").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].testType").value(hasItem(DEFAULT_TEST_TYPE.toString())))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].preRequisite").value(hasItem(DEFAULT_PRE_REQUISITE.toString())))
            .andExpect(jsonPath("$.[*].caution").value(hasItem(DEFAULT_CAUTION.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getClinicalTest() throws Exception {
        // Initialize the database
        clinicalTestRepository.saveAndFlush(clinicalTest);

        // Get the clinicalTest
        restClinicalTestMockMvc.perform(get("/api/clinical-tests/{id}", clinicalTest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(clinicalTest.getId().intValue()))
            .andExpect(jsonPath("$.name ").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.testType").value(DEFAULT_TEST_TYPE.toString()))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE.doubleValue()))
            .andExpect(jsonPath("$.preRequisite").value(DEFAULT_PRE_REQUISITE.toString()))
            .andExpect(jsonPath("$.caution").value(DEFAULT_CAUTION.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingClinicalTest() throws Exception {
        // Get the clinicalTest
        restClinicalTestMockMvc.perform(get("/api/clinical-tests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClinicalTest() throws Exception {
        // Initialize the database
        clinicalTestRepository.saveAndFlush(clinicalTest);
        int databaseSizeBeforeUpdate = clinicalTestRepository.findAll().size();

        // Update the clinicalTest
        ClinicalTest updatedClinicalTest = clinicalTestRepository.findOne(clinicalTest.getId());
        updatedClinicalTest
            .name (UPDATED_NAME)
            .testType(UPDATED_TEST_TYPE)
            .rate(UPDATED_RATE)
            .preRequisite(UPDATED_PRE_REQUISITE)
            .caution(UPDATED_CAUTION)
            .isActive(UPDATED_IS_ACTIVE);

        restClinicalTestMockMvc.perform(put("/api/clinical-tests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedClinicalTest)))
            .andExpect(status().isOk());

        // Validate the ClinicalTest in the database
        List<ClinicalTest> clinicalTestList = clinicalTestRepository.findAll();
        assertThat(clinicalTestList).hasSize(databaseSizeBeforeUpdate);
        ClinicalTest testClinicalTest = clinicalTestList.get(clinicalTestList.size() - 1);
        assertThat(testClinicalTest.getName ()).isEqualTo(UPDATED_NAME);
        assertThat(testClinicalTest.getTestType()).isEqualTo(UPDATED_TEST_TYPE);
        assertThat(testClinicalTest.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testClinicalTest.getPreRequisite()).isEqualTo(UPDATED_PRE_REQUISITE);
        assertThat(testClinicalTest.getCaution()).isEqualTo(UPDATED_CAUTION);
        assertThat(testClinicalTest.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingClinicalTest() throws Exception {
        int databaseSizeBeforeUpdate = clinicalTestRepository.findAll().size();

        // Create the ClinicalTest

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restClinicalTestMockMvc.perform(put("/api/clinical-tests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clinicalTest)))
            .andExpect(status().isCreated());

        // Validate the ClinicalTest in the database
        List<ClinicalTest> clinicalTestList = clinicalTestRepository.findAll();
        assertThat(clinicalTestList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteClinicalTest() throws Exception {
        // Initialize the database
        clinicalTestRepository.saveAndFlush(clinicalTest);
        int databaseSizeBeforeDelete = clinicalTestRepository.findAll().size();

        // Get the clinicalTest
        restClinicalTestMockMvc.perform(delete("/api/clinical-tests/{id}", clinicalTest.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ClinicalTest> clinicalTestList = clinicalTestRepository.findAll();
        assertThat(clinicalTestList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClinicalTest.class);
        ClinicalTest clinicalTest1 = new ClinicalTest();
        clinicalTest1.setId(1L);
        ClinicalTest clinicalTest2 = new ClinicalTest();
        clinicalTest2.setId(clinicalTest1.getId());
        assertThat(clinicalTest1).isEqualTo(clinicalTest2);
        clinicalTest2.setId(2L);
        assertThat(clinicalTest1).isNotEqualTo(clinicalTest2);
        clinicalTest1.setId(null);
        assertThat(clinicalTest1).isNotEqualTo(clinicalTest2);
    }
}
