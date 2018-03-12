package com.pgdit.web.rest;

import com.pgdit.ClinicApp;

import com.pgdit.domain.Firoj;
import com.pgdit.repository.FirojRepository;
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
 * Test class for the FirojResource REST controller.
 *
 * @see FirojResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClinicApp.class)
public class FirojResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    @Autowired
    private FirojRepository firojRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFirojMockMvc;

    private Firoj firoj;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FirojResource firojResource = new FirojResource(firojRepository);
        this.restFirojMockMvc = MockMvcBuilders.standaloneSetup(firojResource)
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
    public static Firoj createEntity(EntityManager em) {
        Firoj firoj = new Firoj()
            .name(DEFAULT_NAME)
            .address(DEFAULT_ADDRESS);
        return firoj;
    }

    @Before
    public void initTest() {
        firoj = createEntity(em);
    }

    @Test
    @Transactional
    public void createFiroj() throws Exception {
        int databaseSizeBeforeCreate = firojRepository.findAll().size();

        // Create the Firoj
        restFirojMockMvc.perform(post("/api/firojs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(firoj)))
            .andExpect(status().isCreated());

        // Validate the Firoj in the database
        List<Firoj> firojList = firojRepository.findAll();
        assertThat(firojList).hasSize(databaseSizeBeforeCreate + 1);
        Firoj testFiroj = firojList.get(firojList.size() - 1);
        assertThat(testFiroj.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFiroj.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    public void createFirojWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = firojRepository.findAll().size();

        // Create the Firoj with an existing ID
        firoj.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFirojMockMvc.perform(post("/api/firojs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(firoj)))
            .andExpect(status().isBadRequest());

        // Validate the Firoj in the database
        List<Firoj> firojList = firojRepository.findAll();
        assertThat(firojList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFirojs() throws Exception {
        // Initialize the database
        firojRepository.saveAndFlush(firoj);

        // Get all the firojList
        restFirojMockMvc.perform(get("/api/firojs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(firoj.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())));
    }

    @Test
    @Transactional
    public void getFiroj() throws Exception {
        // Initialize the database
        firojRepository.saveAndFlush(firoj);

        // Get the firoj
        restFirojMockMvc.perform(get("/api/firojs/{id}", firoj.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(firoj.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFiroj() throws Exception {
        // Get the firoj
        restFirojMockMvc.perform(get("/api/firojs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFiroj() throws Exception {
        // Initialize the database
        firojRepository.saveAndFlush(firoj);
        int databaseSizeBeforeUpdate = firojRepository.findAll().size();

        // Update the firoj
        Firoj updatedFiroj = firojRepository.findOne(firoj.getId());
        updatedFiroj
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS);

        restFirojMockMvc.perform(put("/api/firojs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFiroj)))
            .andExpect(status().isOk());

        // Validate the Firoj in the database
        List<Firoj> firojList = firojRepository.findAll();
        assertThat(firojList).hasSize(databaseSizeBeforeUpdate);
        Firoj testFiroj = firojList.get(firojList.size() - 1);
        assertThat(testFiroj.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFiroj.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void updateNonExistingFiroj() throws Exception {
        int databaseSizeBeforeUpdate = firojRepository.findAll().size();

        // Create the Firoj

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFirojMockMvc.perform(put("/api/firojs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(firoj)))
            .andExpect(status().isCreated());

        // Validate the Firoj in the database
        List<Firoj> firojList = firojRepository.findAll();
        assertThat(firojList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFiroj() throws Exception {
        // Initialize the database
        firojRepository.saveAndFlush(firoj);
        int databaseSizeBeforeDelete = firojRepository.findAll().size();

        // Get the firoj
        restFirojMockMvc.perform(delete("/api/firojs/{id}", firoj.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Firoj> firojList = firojRepository.findAll();
        assertThat(firojList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Firoj.class);
        Firoj firoj1 = new Firoj();
        firoj1.setId(1L);
        Firoj firoj2 = new Firoj();
        firoj2.setId(firoj1.getId());
        assertThat(firoj1).isEqualTo(firoj2);
        firoj2.setId(2L);
        assertThat(firoj1).isNotEqualTo(firoj2);
        firoj1.setId(null);
        assertThat(firoj1).isNotEqualTo(firoj2);
    }
}
