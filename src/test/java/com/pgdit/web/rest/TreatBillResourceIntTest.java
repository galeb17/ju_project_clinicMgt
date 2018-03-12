package com.pgdit.web.rest;

import com.pgdit.ClinicApp;

import com.pgdit.domain.TreatBill;
import com.pgdit.repository.TreatBillRepository;
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
 * Test class for the TreatBillResource REST controller.
 *
 * @see TreatBillResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClinicApp.class)
public class TreatBillResourceIntTest {

    private static final Double DEFAULT_CHARGE = 1D;
    private static final Double UPDATED_CHARGE = 2D;

    private static final Double DEFAULT_RECEIVED = 1D;
    private static final Double UPDATED_RECEIVED = 2D;

    private static final Double DEFAULT_BALANCE = 1D;
    private static final Double UPDATED_BALANCE = 2D;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private TreatBillRepository treatBillRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTreatBillMockMvc;

    private TreatBill treatBill;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TreatBillResource treatBillResource = new TreatBillResource(treatBillRepository);
        this.restTreatBillMockMvc = MockMvcBuilders.standaloneSetup(treatBillResource)
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
    public static TreatBill createEntity(EntityManager em) {
        TreatBill treatBill = new TreatBill()
            .charge(DEFAULT_CHARGE)
            .received(DEFAULT_RECEIVED)
            .balance(DEFAULT_BALANCE)
            .date(DEFAULT_DATE);
        return treatBill;
    }

    @Before
    public void initTest() {
        treatBill = createEntity(em);
    }

    @Test
    @Transactional
    public void createTreatBill() throws Exception {
        int databaseSizeBeforeCreate = treatBillRepository.findAll().size();

        // Create the TreatBill
        restTreatBillMockMvc.perform(post("/api/treat-bills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(treatBill)))
            .andExpect(status().isCreated());

        // Validate the TreatBill in the database
        List<TreatBill> treatBillList = treatBillRepository.findAll();
        assertThat(treatBillList).hasSize(databaseSizeBeforeCreate + 1);
        TreatBill testTreatBill = treatBillList.get(treatBillList.size() - 1);
        assertThat(testTreatBill.getCharge()).isEqualTo(DEFAULT_CHARGE);
        assertThat(testTreatBill.getReceived()).isEqualTo(DEFAULT_RECEIVED);
        assertThat(testTreatBill.getBalance()).isEqualTo(DEFAULT_BALANCE);
        assertThat(testTreatBill.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createTreatBillWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = treatBillRepository.findAll().size();

        // Create the TreatBill with an existing ID
        treatBill.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTreatBillMockMvc.perform(post("/api/treat-bills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(treatBill)))
            .andExpect(status().isBadRequest());

        // Validate the TreatBill in the database
        List<TreatBill> treatBillList = treatBillRepository.findAll();
        assertThat(treatBillList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkChargeIsRequired() throws Exception {
        int databaseSizeBeforeTest = treatBillRepository.findAll().size();
        // set the field null
        treatBill.setCharge(null);

        // Create the TreatBill, which fails.

        restTreatBillMockMvc.perform(post("/api/treat-bills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(treatBill)))
            .andExpect(status().isBadRequest());

        List<TreatBill> treatBillList = treatBillRepository.findAll();
        assertThat(treatBillList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReceivedIsRequired() throws Exception {
        int databaseSizeBeforeTest = treatBillRepository.findAll().size();
        // set the field null
        treatBill.setReceived(null);

        // Create the TreatBill, which fails.

        restTreatBillMockMvc.perform(post("/api/treat-bills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(treatBill)))
            .andExpect(status().isBadRequest());

        List<TreatBill> treatBillList = treatBillRepository.findAll();
        assertThat(treatBillList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = treatBillRepository.findAll().size();
        // set the field null
        treatBill.setBalance(null);

        // Create the TreatBill, which fails.

        restTreatBillMockMvc.perform(post("/api/treat-bills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(treatBill)))
            .andExpect(status().isBadRequest());

        List<TreatBill> treatBillList = treatBillRepository.findAll();
        assertThat(treatBillList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTreatBills() throws Exception {
        // Initialize the database
        treatBillRepository.saveAndFlush(treatBill);

        // Get all the treatBillList
        restTreatBillMockMvc.perform(get("/api/treat-bills?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(treatBill.getId().intValue())))
            .andExpect(jsonPath("$.[*].charge").value(hasItem(DEFAULT_CHARGE.doubleValue())))
            .andExpect(jsonPath("$.[*].received").value(hasItem(DEFAULT_RECEIVED.doubleValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    public void getTreatBill() throws Exception {
        // Initialize the database
        treatBillRepository.saveAndFlush(treatBill);

        // Get the treatBill
        restTreatBillMockMvc.perform(get("/api/treat-bills/{id}", treatBill.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(treatBill.getId().intValue()))
            .andExpect(jsonPath("$.charge").value(DEFAULT_CHARGE.doubleValue()))
            .andExpect(jsonPath("$.received").value(DEFAULT_RECEIVED.doubleValue()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.doubleValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTreatBill() throws Exception {
        // Get the treatBill
        restTreatBillMockMvc.perform(get("/api/treat-bills/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTreatBill() throws Exception {
        // Initialize the database
        treatBillRepository.saveAndFlush(treatBill);
        int databaseSizeBeforeUpdate = treatBillRepository.findAll().size();

        // Update the treatBill
        TreatBill updatedTreatBill = treatBillRepository.findOne(treatBill.getId());
        updatedTreatBill
            .charge(UPDATED_CHARGE)
            .received(UPDATED_RECEIVED)
            .balance(UPDATED_BALANCE)
            .date(UPDATED_DATE);

        restTreatBillMockMvc.perform(put("/api/treat-bills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTreatBill)))
            .andExpect(status().isOk());

        // Validate the TreatBill in the database
        List<TreatBill> treatBillList = treatBillRepository.findAll();
        assertThat(treatBillList).hasSize(databaseSizeBeforeUpdate);
        TreatBill testTreatBill = treatBillList.get(treatBillList.size() - 1);
        assertThat(testTreatBill.getCharge()).isEqualTo(UPDATED_CHARGE);
        assertThat(testTreatBill.getReceived()).isEqualTo(UPDATED_RECEIVED);
        assertThat(testTreatBill.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testTreatBill.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingTreatBill() throws Exception {
        int databaseSizeBeforeUpdate = treatBillRepository.findAll().size();

        // Create the TreatBill

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTreatBillMockMvc.perform(put("/api/treat-bills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(treatBill)))
            .andExpect(status().isCreated());

        // Validate the TreatBill in the database
        List<TreatBill> treatBillList = treatBillRepository.findAll();
        assertThat(treatBillList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTreatBill() throws Exception {
        // Initialize the database
        treatBillRepository.saveAndFlush(treatBill);
        int databaseSizeBeforeDelete = treatBillRepository.findAll().size();

        // Get the treatBill
        restTreatBillMockMvc.perform(delete("/api/treat-bills/{id}", treatBill.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TreatBill> treatBillList = treatBillRepository.findAll();
        assertThat(treatBillList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TreatBill.class);
        TreatBill treatBill1 = new TreatBill();
        treatBill1.setId(1L);
        TreatBill treatBill2 = new TreatBill();
        treatBill2.setId(treatBill1.getId());
        assertThat(treatBill1).isEqualTo(treatBill2);
        treatBill2.setId(2L);
        assertThat(treatBill1).isNotEqualTo(treatBill2);
        treatBill1.setId(null);
        assertThat(treatBill1).isNotEqualTo(treatBill2);
    }
}
