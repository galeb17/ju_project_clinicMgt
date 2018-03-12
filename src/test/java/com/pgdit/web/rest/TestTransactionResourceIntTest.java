package com.pgdit.web.rest;

import com.pgdit.ClinicApp;

import com.pgdit.domain.TestTransaction;
import com.pgdit.repository.TestTransactionRepository;
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
 * Test class for the TestTransactionResource REST controller.
 *
 * @see TestTransactionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClinicApp.class)
public class TestTransactionResourceIntTest {

    private static final String DEFAULT_TREATMENT = "AAAAAAAAAA";
    private static final String UPDATED_TREATMENT = "BBBBBBBBBB";

    private static final String DEFAULT_CAUSE = "AAAAAAAAAA";
    private static final String UPDATED_CAUSE = "BBBBBBBBBB";

    private static final Double DEFAULT_CHARGE = 1D;
    private static final Double UPDATED_CHARGE = 2D;

    private static final Double DEFAULT_RECEIVED = 1D;
    private static final Double UPDATED_RECEIVED = 2D;

    private static final Double DEFAULT_BALANCE = 1D;
    private static final Double UPDATED_BALANCE = 2D;

    private static final String DEFAULT_REF_DOCTOR = "AAAAAAAAAA";
    private static final String UPDATED_REF_DOCTOR = "BBBBBBBBBB";

    private static final Integer DEFAULT_TREAT_NO = 1;
    private static final Integer UPDATED_TREAT_NO = 2;

    private static final String DEFAULT_TEST_REPORT = "AAAAAAAAAA";
    private static final String UPDATED_TEST_REPORT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private TestTransactionRepository testTransactionRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTestTransactionMockMvc;

    private TestTransaction testTransaction;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TestTransactionResource testTransactionResource = new TestTransactionResource(testTransactionRepository);
        this.restTestTransactionMockMvc = MockMvcBuilders.standaloneSetup(testTransactionResource)
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
    public static TestTransaction createEntity(EntityManager em) {
        TestTransaction testTransaction = new TestTransaction()
            .treatment (DEFAULT_TREATMENT)
            .cause(DEFAULT_CAUSE)
            .charge(DEFAULT_CHARGE)
            .received(DEFAULT_RECEIVED)
            .balance(DEFAULT_BALANCE)
            .refDoctor(DEFAULT_REF_DOCTOR)
            .treatNo(DEFAULT_TREAT_NO)
            .testReport(DEFAULT_TEST_REPORT)
            .date(DEFAULT_DATE);
        return testTransaction;
    }

    @Before
    public void initTest() {
        testTransaction = createEntity(em);
    }

    @Test
    @Transactional
    public void createTestTransaction() throws Exception {
        int databaseSizeBeforeCreate = testTransactionRepository.findAll().size();

        // Create the TestTransaction
        restTestTransactionMockMvc.perform(post("/api/test-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testTransaction)))
            .andExpect(status().isCreated());

        // Validate the TestTransaction in the database
        List<TestTransaction> testTransactionList = testTransactionRepository.findAll();
        assertThat(testTransactionList).hasSize(databaseSizeBeforeCreate + 1);
        TestTransaction testTestTransaction = testTransactionList.get(testTransactionList.size() - 1);
        assertThat(testTestTransaction.getTreatment ()).isEqualTo(DEFAULT_TREATMENT);
        assertThat(testTestTransaction.getCause()).isEqualTo(DEFAULT_CAUSE);
        assertThat(testTestTransaction.getCharge()).isEqualTo(DEFAULT_CHARGE);
        assertThat(testTestTransaction.getReceived()).isEqualTo(DEFAULT_RECEIVED);
        assertThat(testTestTransaction.getBalance()).isEqualTo(DEFAULT_BALANCE);
        assertThat(testTestTransaction.getRefDoctor()).isEqualTo(DEFAULT_REF_DOCTOR);
        assertThat(testTestTransaction.getTreatNo()).isEqualTo(DEFAULT_TREAT_NO);
        assertThat(testTestTransaction.getTestReport()).isEqualTo(DEFAULT_TEST_REPORT);
        assertThat(testTestTransaction.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createTestTransactionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = testTransactionRepository.findAll().size();

        // Create the TestTransaction with an existing ID
        testTransaction.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestTransactionMockMvc.perform(post("/api/test-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testTransaction)))
            .andExpect(status().isBadRequest());

        // Validate the TestTransaction in the database
        List<TestTransaction> testTransactionList = testTransactionRepository.findAll();
        assertThat(testTransactionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTestTransactions() throws Exception {
        // Initialize the database
        testTransactionRepository.saveAndFlush(testTransaction);

        // Get all the testTransactionList
        restTestTransactionMockMvc.perform(get("/api/test-transactions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].treatment ").value(hasItem(DEFAULT_TREATMENT.toString())))
            .andExpect(jsonPath("$.[*].cause").value(hasItem(DEFAULT_CAUSE.toString())))
            .andExpect(jsonPath("$.[*].charge").value(hasItem(DEFAULT_CHARGE.doubleValue())))
            .andExpect(jsonPath("$.[*].received").value(hasItem(DEFAULT_RECEIVED.doubleValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].refDoctor").value(hasItem(DEFAULT_REF_DOCTOR.toString())))
            .andExpect(jsonPath("$.[*].treatNo").value(hasItem(DEFAULT_TREAT_NO)))
            .andExpect(jsonPath("$.[*].testReport").value(hasItem(DEFAULT_TEST_REPORT.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    public void getTestTransaction() throws Exception {
        // Initialize the database
        testTransactionRepository.saveAndFlush(testTransaction);

        // Get the testTransaction
        restTestTransactionMockMvc.perform(get("/api/test-transactions/{id}", testTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(testTransaction.getId().intValue()))
            .andExpect(jsonPath("$.treatment ").value(DEFAULT_TREATMENT.toString()))
            .andExpect(jsonPath("$.cause").value(DEFAULT_CAUSE.toString()))
            .andExpect(jsonPath("$.charge").value(DEFAULT_CHARGE.doubleValue()))
            .andExpect(jsonPath("$.received").value(DEFAULT_RECEIVED.doubleValue()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.doubleValue()))
            .andExpect(jsonPath("$.refDoctor").value(DEFAULT_REF_DOCTOR.toString()))
            .andExpect(jsonPath("$.treatNo").value(DEFAULT_TREAT_NO))
            .andExpect(jsonPath("$.testReport").value(DEFAULT_TEST_REPORT.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTestTransaction() throws Exception {
        // Get the testTransaction
        restTestTransactionMockMvc.perform(get("/api/test-transactions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTestTransaction() throws Exception {
        // Initialize the database
        testTransactionRepository.saveAndFlush(testTransaction);
        int databaseSizeBeforeUpdate = testTransactionRepository.findAll().size();

        // Update the testTransaction
        TestTransaction updatedTestTransaction = testTransactionRepository.findOne(testTransaction.getId());
        updatedTestTransaction
            .treatment (UPDATED_TREATMENT)
            .cause(UPDATED_CAUSE)
            .charge(UPDATED_CHARGE)
            .received(UPDATED_RECEIVED)
            .balance(UPDATED_BALANCE)
            .refDoctor(UPDATED_REF_DOCTOR)
            .treatNo(UPDATED_TREAT_NO)
            .testReport(UPDATED_TEST_REPORT)
            .date(UPDATED_DATE);

        restTestTransactionMockMvc.perform(put("/api/test-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTestTransaction)))
            .andExpect(status().isOk());

        // Validate the TestTransaction in the database
        List<TestTransaction> testTransactionList = testTransactionRepository.findAll();
        assertThat(testTransactionList).hasSize(databaseSizeBeforeUpdate);
        TestTransaction testTestTransaction = testTransactionList.get(testTransactionList.size() - 1);
        assertThat(testTestTransaction.getTreatment ()).isEqualTo(UPDATED_TREATMENT);
        assertThat(testTestTransaction.getCause()).isEqualTo(UPDATED_CAUSE);
        assertThat(testTestTransaction.getCharge()).isEqualTo(UPDATED_CHARGE);
        assertThat(testTestTransaction.getReceived()).isEqualTo(UPDATED_RECEIVED);
        assertThat(testTestTransaction.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testTestTransaction.getRefDoctor()).isEqualTo(UPDATED_REF_DOCTOR);
        assertThat(testTestTransaction.getTreatNo()).isEqualTo(UPDATED_TREAT_NO);
        assertThat(testTestTransaction.getTestReport()).isEqualTo(UPDATED_TEST_REPORT);
        assertThat(testTestTransaction.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingTestTransaction() throws Exception {
        int databaseSizeBeforeUpdate = testTransactionRepository.findAll().size();

        // Create the TestTransaction

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTestTransactionMockMvc.perform(put("/api/test-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(testTransaction)))
            .andExpect(status().isCreated());

        // Validate the TestTransaction in the database
        List<TestTransaction> testTransactionList = testTransactionRepository.findAll();
        assertThat(testTransactionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTestTransaction() throws Exception {
        // Initialize the database
        testTransactionRepository.saveAndFlush(testTransaction);
        int databaseSizeBeforeDelete = testTransactionRepository.findAll().size();

        // Get the testTransaction
        restTestTransactionMockMvc.perform(delete("/api/test-transactions/{id}", testTransaction.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TestTransaction> testTransactionList = testTransactionRepository.findAll();
        assertThat(testTransactionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestTransaction.class);
        TestTransaction testTransaction1 = new TestTransaction();
        testTransaction1.setId(1L);
        TestTransaction testTransaction2 = new TestTransaction();
        testTransaction2.setId(testTransaction1.getId());
        assertThat(testTransaction1).isEqualTo(testTransaction2);
        testTransaction2.setId(2L);
        assertThat(testTransaction1).isNotEqualTo(testTransaction2);
        testTransaction1.setId(null);
        assertThat(testTransaction1).isNotEqualTo(testTransaction2);
    }
}
