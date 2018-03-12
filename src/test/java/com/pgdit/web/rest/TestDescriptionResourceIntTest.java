package com.pgdit.web.rest;

import com.pgdit.ClinicApp;

import com.pgdit.domain.TestDescription;
import com.pgdit.repository.TestDescriptionRepository;
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
 * Test class for the TestDescriptionResource REST controller.
 *
 * @see TestDescriptionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClinicApp.class)
public class TestDescriptionResourceIntTest {

    private static final String DEFAULT_DEPARTMENT = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT = "BBBBBBBBBB";

    private static final String DEFAULT_TREATMENT = "AAAAAAAAAA";
    private static final String UPDATED_TREATMENT = "BBBBBBBBBB";

    private static final Double DEFAULT_CHARGE = 1D;
    private static final Double UPDATED_CHARGE = 2D;

    @Autowired
    private TestDescriptionRepository testDescriptionRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTestDescriptionMockMvc;

    private TestDescription testDescription;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TestDescriptionResource testDescriptionResource = new TestDescriptionResource(testDescriptionRepository);
        this.restTestDescriptionMockMvc = MockMvcBuilders.standaloneSetup(testDescriptionResource)
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
    public static TestDescription createEntity(EntityManager em) {
        TestDescription testDescription = new TestDescription()
            .department(DEFAULT_DEPARTMENT)
            .treatment(DEFAULT_TREATMENT)
            .charge(DEFAULT_CHARGE);
        return testDescription;
    }

    @Before
    public void initTest() {
        testDescription = createEntity(em);
    }

    @Test
    @Transactional
    public void createTestDescription() throws Exception {
        int databaseSizeBeforeCreate = testDescriptionRepository.findAll().size();

        // Create the TestDescription
        restTestDescriptionMockMvc.perform(post("/api/test-descriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testDescription)))
            .andExpect(status().isCreated());

        // Validate the TestDescription in the database
        List<TestDescription> testDescriptionList = testDescriptionRepository.findAll();
        assertThat(testDescriptionList).hasSize(databaseSizeBeforeCreate + 1);
        TestDescription testTestDescription = testDescriptionList.get(testDescriptionList.size() - 1);
        assertThat(testTestDescription.getDepartment()).isEqualTo(DEFAULT_DEPARTMENT);
        assertThat(testTestDescription.getTreatment()).isEqualTo(DEFAULT_TREATMENT);
        assertThat(testTestDescription.getCharge()).isEqualTo(DEFAULT_CHARGE);
    }

    @Test
    @Transactional
    public void createTestDescriptionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = testDescriptionRepository.findAll().size();

        // Create the TestDescription with an existing ID
        testDescription.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestDescriptionMockMvc.perform(post("/api/test-descriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testDescription)))
            .andExpect(status().isBadRequest());

        // Validate the TestDescription in the database
        List<TestDescription> testDescriptionList = testDescriptionRepository.findAll();
        assertThat(testDescriptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTestDescriptions() throws Exception {
        // Initialize the database
        testDescriptionRepository.saveAndFlush(testDescription);

        // Get all the testDescriptionList
        restTestDescriptionMockMvc.perform(get("/api/test-descriptions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testDescription.getId().intValue())))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT.toString())))
            .andExpect(jsonPath("$.[*].treatment").value(hasItem(DEFAULT_TREATMENT.toString())))
            .andExpect(jsonPath("$.[*].charge").value(hasItem(DEFAULT_CHARGE.doubleValue())));
    }

    @Test
    @Transactional
    public void getTestDescription() throws Exception {
        // Initialize the database
        testDescriptionRepository.saveAndFlush(testDescription);

        // Get the testDescription
        restTestDescriptionMockMvc.perform(get("/api/test-descriptions/{id}", testDescription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(testDescription.getId().intValue()))
            .andExpect(jsonPath("$.department").value(DEFAULT_DEPARTMENT.toString()))
            .andExpect(jsonPath("$.treatment").value(DEFAULT_TREATMENT.toString()))
            .andExpect(jsonPath("$.charge").value(DEFAULT_CHARGE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTestDescription() throws Exception {
        // Get the testDescription
        restTestDescriptionMockMvc.perform(get("/api/test-descriptions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTestDescription() throws Exception {
        // Initialize the database
        testDescriptionRepository.saveAndFlush(testDescription);
        int databaseSizeBeforeUpdate = testDescriptionRepository.findAll().size();

        // Update the testDescription
        TestDescription updatedTestDescription = testDescriptionRepository.findOne(testDescription.getId());
        updatedTestDescription
            .department(UPDATED_DEPARTMENT)
            .treatment(UPDATED_TREATMENT)
            .charge(UPDATED_CHARGE);

        restTestDescriptionMockMvc.perform(put("/api/test-descriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTestDescription)))
            .andExpect(status().isOk());

        // Validate the TestDescription in the database
        List<TestDescription> testDescriptionList = testDescriptionRepository.findAll();
        assertThat(testDescriptionList).hasSize(databaseSizeBeforeUpdate);
        TestDescription testTestDescription = testDescriptionList.get(testDescriptionList.size() - 1);
        assertThat(testTestDescription.getDepartment()).isEqualTo(UPDATED_DEPARTMENT);
        assertThat(testTestDescription.getTreatment()).isEqualTo(UPDATED_TREATMENT);
        assertThat(testTestDescription.getCharge()).isEqualTo(UPDATED_CHARGE);
    }

    @Test
    @Transactional
    public void updateNonExistingTestDescription() throws Exception {
        int databaseSizeBeforeUpdate = testDescriptionRepository.findAll().size();

        // Create the TestDescription

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTestDescriptionMockMvc.perform(put("/api/test-descriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testDescription)))
            .andExpect(status().isCreated());

        // Validate the TestDescription in the database
        List<TestDescription> testDescriptionList = testDescriptionRepository.findAll();
        assertThat(testDescriptionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTestDescription() throws Exception {
        // Initialize the database
        testDescriptionRepository.saveAndFlush(testDescription);
        int databaseSizeBeforeDelete = testDescriptionRepository.findAll().size();

        // Get the testDescription
        restTestDescriptionMockMvc.perform(delete("/api/test-descriptions/{id}", testDescription.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TestDescription> testDescriptionList = testDescriptionRepository.findAll();
        assertThat(testDescriptionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestDescription.class);
        TestDescription testDescription1 = new TestDescription();
        testDescription1.setId(1L);
        TestDescription testDescription2 = new TestDescription();
        testDescription2.setId(testDescription1.getId());
        assertThat(testDescription1).isEqualTo(testDescription2);
        testDescription2.setId(2L);
        assertThat(testDescription1).isNotEqualTo(testDescription2);
        testDescription1.setId(null);
        assertThat(testDescription1).isNotEqualTo(testDescription2);
    }
}
