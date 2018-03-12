package com.pgdit.web.rest;

import com.pgdit.ClinicApp;

import com.pgdit.domain.AppointSchedule;
import com.pgdit.repository.AppointScheduleRepository;
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
 * Test class for the AppointScheduleResource REST controller.
 *
 * @see AppointScheduleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClinicApp.class)
public class AppointScheduleResourceIntTest {

    private static final Instant DEFAULT_SCHEDULE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SCHEDULE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_SCHEDULE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SCHEDULE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private AppointScheduleRepository appointScheduleRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAppointScheduleMockMvc;

    private AppointSchedule appointSchedule;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AppointScheduleResource appointScheduleResource = new AppointScheduleResource(appointScheduleRepository);
        this.restAppointScheduleMockMvc = MockMvcBuilders.standaloneSetup(appointScheduleResource)
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
    public static AppointSchedule createEntity(EntityManager em) {
        AppointSchedule appointSchedule = new AppointSchedule()
            .scheduleDate(DEFAULT_SCHEDULE_DATE)
            .scheduleTime(DEFAULT_SCHEDULE_TIME);
        return appointSchedule;
    }

    @Before
    public void initTest() {
        appointSchedule = createEntity(em);
    }

    @Test
    @Transactional
    public void createAppointSchedule() throws Exception {
        int databaseSizeBeforeCreate = appointScheduleRepository.findAll().size();

        // Create the AppointSchedule
        restAppointScheduleMockMvc.perform(post("/api/appoint-schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appointSchedule)))
            .andExpect(status().isCreated());

        // Validate the AppointSchedule in the database
        List<AppointSchedule> appointScheduleList = appointScheduleRepository.findAll();
        assertThat(appointScheduleList).hasSize(databaseSizeBeforeCreate + 1);
        AppointSchedule testAppointSchedule = appointScheduleList.get(appointScheduleList.size() - 1);
        assertThat(testAppointSchedule.getScheduleDate()).isEqualTo(DEFAULT_SCHEDULE_DATE);
        assertThat(testAppointSchedule.getScheduleTime()).isEqualTo(DEFAULT_SCHEDULE_TIME);
    }

    @Test
    @Transactional
    public void createAppointScheduleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = appointScheduleRepository.findAll().size();

        // Create the AppointSchedule with an existing ID
        appointSchedule.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppointScheduleMockMvc.perform(post("/api/appoint-schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appointSchedule)))
            .andExpect(status().isBadRequest());

        // Validate the AppointSchedule in the database
        List<AppointSchedule> appointScheduleList = appointScheduleRepository.findAll();
        assertThat(appointScheduleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAppointSchedules() throws Exception {
        // Initialize the database
        appointScheduleRepository.saveAndFlush(appointSchedule);

        // Get all the appointScheduleList
        restAppointScheduleMockMvc.perform(get("/api/appoint-schedules?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appointSchedule.getId().intValue())))
            .andExpect(jsonPath("$.[*].scheduleDate").value(hasItem(DEFAULT_SCHEDULE_DATE.toString())))
            .andExpect(jsonPath("$.[*].scheduleTime").value(hasItem(DEFAULT_SCHEDULE_TIME.toString())));
    }

    @Test
    @Transactional
    public void getAppointSchedule() throws Exception {
        // Initialize the database
        appointScheduleRepository.saveAndFlush(appointSchedule);

        // Get the appointSchedule
        restAppointScheduleMockMvc.perform(get("/api/appoint-schedules/{id}", appointSchedule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(appointSchedule.getId().intValue()))
            .andExpect(jsonPath("$.scheduleDate").value(DEFAULT_SCHEDULE_DATE.toString()))
            .andExpect(jsonPath("$.scheduleTime").value(DEFAULT_SCHEDULE_TIME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAppointSchedule() throws Exception {
        // Get the appointSchedule
        restAppointScheduleMockMvc.perform(get("/api/appoint-schedules/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAppointSchedule() throws Exception {
        // Initialize the database
        appointScheduleRepository.saveAndFlush(appointSchedule);
        int databaseSizeBeforeUpdate = appointScheduleRepository.findAll().size();

        // Update the appointSchedule
        AppointSchedule updatedAppointSchedule = appointScheduleRepository.findOne(appointSchedule.getId());
        updatedAppointSchedule
            .scheduleDate(UPDATED_SCHEDULE_DATE)
            .scheduleTime(UPDATED_SCHEDULE_TIME);

        restAppointScheduleMockMvc.perform(put("/api/appoint-schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAppointSchedule)))
            .andExpect(status().isOk());

        // Validate the AppointSchedule in the database
        List<AppointSchedule> appointScheduleList = appointScheduleRepository.findAll();
        assertThat(appointScheduleList).hasSize(databaseSizeBeforeUpdate);
        AppointSchedule testAppointSchedule = appointScheduleList.get(appointScheduleList.size() - 1);
        assertThat(testAppointSchedule.getScheduleDate()).isEqualTo(UPDATED_SCHEDULE_DATE);
        assertThat(testAppointSchedule.getScheduleTime()).isEqualTo(UPDATED_SCHEDULE_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingAppointSchedule() throws Exception {
        int databaseSizeBeforeUpdate = appointScheduleRepository.findAll().size();

        // Create the AppointSchedule

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAppointScheduleMockMvc.perform(put("/api/appoint-schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appointSchedule)))
            .andExpect(status().isCreated());

        // Validate the AppointSchedule in the database
        List<AppointSchedule> appointScheduleList = appointScheduleRepository.findAll();
        assertThat(appointScheduleList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAppointSchedule() throws Exception {
        // Initialize the database
        appointScheduleRepository.saveAndFlush(appointSchedule);
        int databaseSizeBeforeDelete = appointScheduleRepository.findAll().size();

        // Get the appointSchedule
        restAppointScheduleMockMvc.perform(delete("/api/appoint-schedules/{id}", appointSchedule.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AppointSchedule> appointScheduleList = appointScheduleRepository.findAll();
        assertThat(appointScheduleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppointSchedule.class);
        AppointSchedule appointSchedule1 = new AppointSchedule();
        appointSchedule1.setId(1L);
        AppointSchedule appointSchedule2 = new AppointSchedule();
        appointSchedule2.setId(appointSchedule1.getId());
        assertThat(appointSchedule1).isEqualTo(appointSchedule2);
        appointSchedule2.setId(2L);
        assertThat(appointSchedule1).isNotEqualTo(appointSchedule2);
        appointSchedule1.setId(null);
        assertThat(appointSchedule1).isNotEqualTo(appointSchedule2);
    }
}
