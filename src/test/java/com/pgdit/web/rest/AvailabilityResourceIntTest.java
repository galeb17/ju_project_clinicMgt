package com.pgdit.web.rest;

import com.pgdit.ClinicApp;

import com.pgdit.domain.Availability;
import com.pgdit.repository.AvailabilityRepository;
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

import com.pgdit.domain.enumeration.Gender;
/**
 * Test class for the AvailabilityResource REST controller.
 *
 * @see AvailabilityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClinicApp.class)
public class AvailabilityResourceIntTest {

    private static final String DEFAULT_WARDS = "AAAAAAAAAA";
    private static final String UPDATED_WARDS = "BBBBBBBBBB";

    private static final String DEFAULT_BED_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BED_NO = "AAAAAAAAAA";
    private static final String UPDATED_BED_NO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_RESERVED = false;
    private static final Boolean UPDATED_RESERVED = true;

    private static final Double DEFAULT_CHARGE = 1D;
    private static final Double UPDATED_CHARGE = 2D;

    private static final Gender DEFAULT_GENDER = Gender.male;
    private static final Gender UPDATED_GENDER = Gender.female;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAvailabilityMockMvc;

    private Availability availability;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AvailabilityResource availabilityResource = new AvailabilityResource(availabilityRepository);
        this.restAvailabilityMockMvc = MockMvcBuilders.standaloneSetup(availabilityResource)
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
    public static Availability createEntity(EntityManager em) {
        Availability availability = new Availability()
            .wards(DEFAULT_WARDS)
            .bedCode(DEFAULT_BED_CODE)
            .bedNo(DEFAULT_BED_NO)
            .reserved(DEFAULT_RESERVED)
            .Charge(DEFAULT_CHARGE)
            .gender(DEFAULT_GENDER);
        return availability;
    }

    @Before
    public void initTest() {
        availability = createEntity(em);
    }

    @Test
    @Transactional
    public void createAvailability() throws Exception {
        int databaseSizeBeforeCreate = availabilityRepository.findAll().size();

        // Create the Availability
        restAvailabilityMockMvc.perform(post("/api/availabilities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(availability)))
            .andExpect(status().isCreated());

        // Validate the Availability in the database
        List<Availability> availabilityList = availabilityRepository.findAll();
        assertThat(availabilityList).hasSize(databaseSizeBeforeCreate + 1);
        Availability testAvailability = availabilityList.get(availabilityList.size() - 1);
        assertThat(testAvailability.getWards()).isEqualTo(DEFAULT_WARDS);
        assertThat(testAvailability.getBedCode()).isEqualTo(DEFAULT_BED_CODE);
        assertThat(testAvailability.getBedNo()).isEqualTo(DEFAULT_BED_NO);
        assertThat(testAvailability.isReserved()).isEqualTo(DEFAULT_RESERVED);
        assertThat(testAvailability.getCharge()).isEqualTo(DEFAULT_CHARGE);
        assertThat(testAvailability.getGender()).isEqualTo(DEFAULT_GENDER);
    }

    @Test
    @Transactional
    public void createAvailabilityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = availabilityRepository.findAll().size();

        // Create the Availability with an existing ID
        availability.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAvailabilityMockMvc.perform(post("/api/availabilities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(availability)))
            .andExpect(status().isBadRequest());

        // Validate the Availability in the database
        List<Availability> availabilityList = availabilityRepository.findAll();
        assertThat(availabilityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAvailabilities() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get all the availabilityList
        restAvailabilityMockMvc.perform(get("/api/availabilities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(availability.getId().intValue())))
            .andExpect(jsonPath("$.[*].wards").value(hasItem(DEFAULT_WARDS.toString())))
            .andExpect(jsonPath("$.[*].bedCode").value(hasItem(DEFAULT_BED_CODE.toString())))
            .andExpect(jsonPath("$.[*].bedNo").value(hasItem(DEFAULT_BED_NO.toString())))
            .andExpect(jsonPath("$.[*].reserved").value(hasItem(DEFAULT_RESERVED.booleanValue())))
            .andExpect(jsonPath("$.[*].Charge").value(hasItem(DEFAULT_CHARGE.doubleValue())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())));
    }

    @Test
    @Transactional
    public void getAvailability() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);

        // Get the availability
        restAvailabilityMockMvc.perform(get("/api/availabilities/{id}", availability.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(availability.getId().intValue()))
            .andExpect(jsonPath("$.wards").value(DEFAULT_WARDS.toString()))
            .andExpect(jsonPath("$.bedCode").value(DEFAULT_BED_CODE.toString()))
            .andExpect(jsonPath("$.bedNo").value(DEFAULT_BED_NO.toString()))
            .andExpect(jsonPath("$.reserved").value(DEFAULT_RESERVED.booleanValue()))
            .andExpect(jsonPath("$.Charge").value(DEFAULT_CHARGE.doubleValue()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAvailability() throws Exception {
        // Get the availability
        restAvailabilityMockMvc.perform(get("/api/availabilities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAvailability() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);
        int databaseSizeBeforeUpdate = availabilityRepository.findAll().size();

        // Update the availability
        Availability updatedAvailability = availabilityRepository.findOne(availability.getId());
        updatedAvailability
            .wards(UPDATED_WARDS)
            .bedCode(UPDATED_BED_CODE)
            .bedNo(UPDATED_BED_NO)
            .reserved(UPDATED_RESERVED)
            .Charge(UPDATED_CHARGE)
            .gender(UPDATED_GENDER);

        restAvailabilityMockMvc.perform(put("/api/availabilities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAvailability)))
            .andExpect(status().isOk());

        // Validate the Availability in the database
        List<Availability> availabilityList = availabilityRepository.findAll();
        assertThat(availabilityList).hasSize(databaseSizeBeforeUpdate);
        Availability testAvailability = availabilityList.get(availabilityList.size() - 1);
        assertThat(testAvailability.getWards()).isEqualTo(UPDATED_WARDS);
        assertThat(testAvailability.getBedCode()).isEqualTo(UPDATED_BED_CODE);
        assertThat(testAvailability.getBedNo()).isEqualTo(UPDATED_BED_NO);
        assertThat(testAvailability.isReserved()).isEqualTo(UPDATED_RESERVED);
        assertThat(testAvailability.getCharge()).isEqualTo(UPDATED_CHARGE);
        assertThat(testAvailability.getGender()).isEqualTo(UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void updateNonExistingAvailability() throws Exception {
        int databaseSizeBeforeUpdate = availabilityRepository.findAll().size();

        // Create the Availability

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAvailabilityMockMvc.perform(put("/api/availabilities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(availability)))
            .andExpect(status().isCreated());

        // Validate the Availability in the database
        List<Availability> availabilityList = availabilityRepository.findAll();
        assertThat(availabilityList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAvailability() throws Exception {
        // Initialize the database
        availabilityRepository.saveAndFlush(availability);
        int databaseSizeBeforeDelete = availabilityRepository.findAll().size();

        // Get the availability
        restAvailabilityMockMvc.perform(delete("/api/availabilities/{id}", availability.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Availability> availabilityList = availabilityRepository.findAll();
        assertThat(availabilityList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Availability.class);
        Availability availability1 = new Availability();
        availability1.setId(1L);
        Availability availability2 = new Availability();
        availability2.setId(availability1.getId());
        assertThat(availability1).isEqualTo(availability2);
        availability2.setId(2L);
        assertThat(availability1).isNotEqualTo(availability2);
        availability1.setId(null);
        assertThat(availability1).isNotEqualTo(availability2);
    }
}
