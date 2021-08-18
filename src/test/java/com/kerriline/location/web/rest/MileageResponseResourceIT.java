package com.kerriline.location.web.rest;

import static com.kerriline.location.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.kerriline.location.IntegrationTest;
import com.kerriline.location.domain.MileageRequest;
import com.kerriline.location.domain.MileageResponse;
import com.kerriline.location.repository.MileageResponseRepository;
import com.kerriline.location.service.criteria.MileageResponseCriteria;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MileageResponseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MileageResponseResourceIT {

    private static final ZonedDateTime DEFAULT_RESPONSE_DATETIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_RESPONSE_DATETIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_RESPONSE_DATETIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_TANK_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TANK_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_MILEAGE_CURRENT = "AAAAAAAAAA";
    private static final String UPDATED_MILEAGE_CURRENT = "BBBBBBBBBB";

    private static final String DEFAULT_MILEAGE_DATETIME = "AAAAAAAAAA";
    private static final String UPDATED_MILEAGE_DATETIME = "BBBBBBBBBB";

    private static final String DEFAULT_MILEAGE_REMAIN = "AAAAAAAAAA";
    private static final String UPDATED_MILEAGE_REMAIN = "BBBBBBBBBB";

    private static final String DEFAULT_MILEAGE_UPDATE_DATETIME = "AAAAAAAAAA";
    private static final String UPDATED_MILEAGE_UPDATE_DATETIME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/mileage-responses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MileageResponseRepository mileageResponseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMileageResponseMockMvc;

    private MileageResponse mileageResponse;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MileageResponse createEntity(EntityManager em) {
        MileageResponse mileageResponse = new MileageResponse()
            .responseDatetime(DEFAULT_RESPONSE_DATETIME)
            .tankNumber(DEFAULT_TANK_NUMBER)
            .mileageCurrent(DEFAULT_MILEAGE_CURRENT)
            .mileageDatetime(DEFAULT_MILEAGE_DATETIME)
            .mileageRemain(DEFAULT_MILEAGE_REMAIN)
            .mileageUpdateDatetime(DEFAULT_MILEAGE_UPDATE_DATETIME);
        return mileageResponse;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MileageResponse createUpdatedEntity(EntityManager em) {
        MileageResponse mileageResponse = new MileageResponse()
            .responseDatetime(UPDATED_RESPONSE_DATETIME)
            .tankNumber(UPDATED_TANK_NUMBER)
            .mileageCurrent(UPDATED_MILEAGE_CURRENT)
            .mileageDatetime(UPDATED_MILEAGE_DATETIME)
            .mileageRemain(UPDATED_MILEAGE_REMAIN)
            .mileageUpdateDatetime(UPDATED_MILEAGE_UPDATE_DATETIME);
        return mileageResponse;
    }

    @BeforeEach
    public void initTest() {
        mileageResponse = createEntity(em);
    }

    @Test
    @Transactional
    void createMileageResponse() throws Exception {
        int databaseSizeBeforeCreate = mileageResponseRepository.findAll().size();
        // Create the MileageResponse
        restMileageResponseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mileageResponse))
            )
            .andExpect(status().isCreated());

        // Validate the MileageResponse in the database
        List<MileageResponse> mileageResponseList = mileageResponseRepository.findAll();
        assertThat(mileageResponseList).hasSize(databaseSizeBeforeCreate + 1);
        MileageResponse testMileageResponse = mileageResponseList.get(mileageResponseList.size() - 1);
        assertThat(testMileageResponse.getResponseDatetime()).isEqualTo(DEFAULT_RESPONSE_DATETIME);
        assertThat(testMileageResponse.getTankNumber()).isEqualTo(DEFAULT_TANK_NUMBER);
        assertThat(testMileageResponse.getMileageCurrent()).isEqualTo(DEFAULT_MILEAGE_CURRENT);
        assertThat(testMileageResponse.getMileageDatetime()).isEqualTo(DEFAULT_MILEAGE_DATETIME);
        assertThat(testMileageResponse.getMileageRemain()).isEqualTo(DEFAULT_MILEAGE_REMAIN);
        assertThat(testMileageResponse.getMileageUpdateDatetime()).isEqualTo(DEFAULT_MILEAGE_UPDATE_DATETIME);
    }

    @Test
    @Transactional
    void createMileageResponseWithExistingId() throws Exception {
        // Create the MileageResponse with an existing ID
        mileageResponse.setId(1L);

        int databaseSizeBeforeCreate = mileageResponseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMileageResponseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mileageResponse))
            )
            .andExpect(status().isBadRequest());

        // Validate the MileageResponse in the database
        List<MileageResponse> mileageResponseList = mileageResponseRepository.findAll();
        assertThat(mileageResponseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMileageResponses() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList
        restMileageResponseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mileageResponse.getId().intValue())))
            .andExpect(jsonPath("$.[*].responseDatetime").value(hasItem(sameInstant(DEFAULT_RESPONSE_DATETIME))))
            .andExpect(jsonPath("$.[*].tankNumber").value(hasItem(DEFAULT_TANK_NUMBER)))
            .andExpect(jsonPath("$.[*].mileageCurrent").value(hasItem(DEFAULT_MILEAGE_CURRENT)))
            .andExpect(jsonPath("$.[*].mileageDatetime").value(hasItem(DEFAULT_MILEAGE_DATETIME)))
            .andExpect(jsonPath("$.[*].mileageRemain").value(hasItem(DEFAULT_MILEAGE_REMAIN)))
            .andExpect(jsonPath("$.[*].mileageUpdateDatetime").value(hasItem(DEFAULT_MILEAGE_UPDATE_DATETIME)));
    }

    @Test
    @Transactional
    void getMileageResponse() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get the mileageResponse
        restMileageResponseMockMvc
            .perform(get(ENTITY_API_URL_ID, mileageResponse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mileageResponse.getId().intValue()))
            .andExpect(jsonPath("$.responseDatetime").value(sameInstant(DEFAULT_RESPONSE_DATETIME)))
            .andExpect(jsonPath("$.tankNumber").value(DEFAULT_TANK_NUMBER))
            .andExpect(jsonPath("$.mileageCurrent").value(DEFAULT_MILEAGE_CURRENT))
            .andExpect(jsonPath("$.mileageDatetime").value(DEFAULT_MILEAGE_DATETIME))
            .andExpect(jsonPath("$.mileageRemain").value(DEFAULT_MILEAGE_REMAIN))
            .andExpect(jsonPath("$.mileageUpdateDatetime").value(DEFAULT_MILEAGE_UPDATE_DATETIME));
    }

    @Test
    @Transactional
    void getMileageResponsesByIdFiltering() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        Long id = mileageResponse.getId();

        defaultMileageResponseShouldBeFound("id.equals=" + id);
        defaultMileageResponseShouldNotBeFound("id.notEquals=" + id);

        defaultMileageResponseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMileageResponseShouldNotBeFound("id.greaterThan=" + id);

        defaultMileageResponseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMileageResponseShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMileageResponsesByResponseDatetimeIsEqualToSomething() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where responseDatetime equals to DEFAULT_RESPONSE_DATETIME
        defaultMileageResponseShouldBeFound("responseDatetime.equals=" + DEFAULT_RESPONSE_DATETIME);

        // Get all the mileageResponseList where responseDatetime equals to UPDATED_RESPONSE_DATETIME
        defaultMileageResponseShouldNotBeFound("responseDatetime.equals=" + UPDATED_RESPONSE_DATETIME);
    }

    @Test
    @Transactional
    void getAllMileageResponsesByResponseDatetimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where responseDatetime not equals to DEFAULT_RESPONSE_DATETIME
        defaultMileageResponseShouldNotBeFound("responseDatetime.notEquals=" + DEFAULT_RESPONSE_DATETIME);

        // Get all the mileageResponseList where responseDatetime not equals to UPDATED_RESPONSE_DATETIME
        defaultMileageResponseShouldBeFound("responseDatetime.notEquals=" + UPDATED_RESPONSE_DATETIME);
    }

    @Test
    @Transactional
    void getAllMileageResponsesByResponseDatetimeIsInShouldWork() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where responseDatetime in DEFAULT_RESPONSE_DATETIME or UPDATED_RESPONSE_DATETIME
        defaultMileageResponseShouldBeFound("responseDatetime.in=" + DEFAULT_RESPONSE_DATETIME + "," + UPDATED_RESPONSE_DATETIME);

        // Get all the mileageResponseList where responseDatetime equals to UPDATED_RESPONSE_DATETIME
        defaultMileageResponseShouldNotBeFound("responseDatetime.in=" + UPDATED_RESPONSE_DATETIME);
    }

    @Test
    @Transactional
    void getAllMileageResponsesByResponseDatetimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where responseDatetime is not null
        defaultMileageResponseShouldBeFound("responseDatetime.specified=true");

        // Get all the mileageResponseList where responseDatetime is null
        defaultMileageResponseShouldNotBeFound("responseDatetime.specified=false");
    }

    @Test
    @Transactional
    void getAllMileageResponsesByResponseDatetimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where responseDatetime is greater than or equal to DEFAULT_RESPONSE_DATETIME
        defaultMileageResponseShouldBeFound("responseDatetime.greaterThanOrEqual=" + DEFAULT_RESPONSE_DATETIME);

        // Get all the mileageResponseList where responseDatetime is greater than or equal to UPDATED_RESPONSE_DATETIME
        defaultMileageResponseShouldNotBeFound("responseDatetime.greaterThanOrEqual=" + UPDATED_RESPONSE_DATETIME);
    }

    @Test
    @Transactional
    void getAllMileageResponsesByResponseDatetimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where responseDatetime is less than or equal to DEFAULT_RESPONSE_DATETIME
        defaultMileageResponseShouldBeFound("responseDatetime.lessThanOrEqual=" + DEFAULT_RESPONSE_DATETIME);

        // Get all the mileageResponseList where responseDatetime is less than or equal to SMALLER_RESPONSE_DATETIME
        defaultMileageResponseShouldNotBeFound("responseDatetime.lessThanOrEqual=" + SMALLER_RESPONSE_DATETIME);
    }

    @Test
    @Transactional
    void getAllMileageResponsesByResponseDatetimeIsLessThanSomething() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where responseDatetime is less than DEFAULT_RESPONSE_DATETIME
        defaultMileageResponseShouldNotBeFound("responseDatetime.lessThan=" + DEFAULT_RESPONSE_DATETIME);

        // Get all the mileageResponseList where responseDatetime is less than UPDATED_RESPONSE_DATETIME
        defaultMileageResponseShouldBeFound("responseDatetime.lessThan=" + UPDATED_RESPONSE_DATETIME);
    }

    @Test
    @Transactional
    void getAllMileageResponsesByResponseDatetimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where responseDatetime is greater than DEFAULT_RESPONSE_DATETIME
        defaultMileageResponseShouldNotBeFound("responseDatetime.greaterThan=" + DEFAULT_RESPONSE_DATETIME);

        // Get all the mileageResponseList where responseDatetime is greater than SMALLER_RESPONSE_DATETIME
        defaultMileageResponseShouldBeFound("responseDatetime.greaterThan=" + SMALLER_RESPONSE_DATETIME);
    }

    @Test
    @Transactional
    void getAllMileageResponsesByTankNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where tankNumber equals to DEFAULT_TANK_NUMBER
        defaultMileageResponseShouldBeFound("tankNumber.equals=" + DEFAULT_TANK_NUMBER);

        // Get all the mileageResponseList where tankNumber equals to UPDATED_TANK_NUMBER
        defaultMileageResponseShouldNotBeFound("tankNumber.equals=" + UPDATED_TANK_NUMBER);
    }

    @Test
    @Transactional
    void getAllMileageResponsesByTankNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where tankNumber not equals to DEFAULT_TANK_NUMBER
        defaultMileageResponseShouldNotBeFound("tankNumber.notEquals=" + DEFAULT_TANK_NUMBER);

        // Get all the mileageResponseList where tankNumber not equals to UPDATED_TANK_NUMBER
        defaultMileageResponseShouldBeFound("tankNumber.notEquals=" + UPDATED_TANK_NUMBER);
    }

    @Test
    @Transactional
    void getAllMileageResponsesByTankNumberIsInShouldWork() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where tankNumber in DEFAULT_TANK_NUMBER or UPDATED_TANK_NUMBER
        defaultMileageResponseShouldBeFound("tankNumber.in=" + DEFAULT_TANK_NUMBER + "," + UPDATED_TANK_NUMBER);

        // Get all the mileageResponseList where tankNumber equals to UPDATED_TANK_NUMBER
        defaultMileageResponseShouldNotBeFound("tankNumber.in=" + UPDATED_TANK_NUMBER);
    }

    @Test
    @Transactional
    void getAllMileageResponsesByTankNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where tankNumber is not null
        defaultMileageResponseShouldBeFound("tankNumber.specified=true");

        // Get all the mileageResponseList where tankNumber is null
        defaultMileageResponseShouldNotBeFound("tankNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllMileageResponsesByTankNumberContainsSomething() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where tankNumber contains DEFAULT_TANK_NUMBER
        defaultMileageResponseShouldBeFound("tankNumber.contains=" + DEFAULT_TANK_NUMBER);

        // Get all the mileageResponseList where tankNumber contains UPDATED_TANK_NUMBER
        defaultMileageResponseShouldNotBeFound("tankNumber.contains=" + UPDATED_TANK_NUMBER);
    }

    @Test
    @Transactional
    void getAllMileageResponsesByTankNumberNotContainsSomething() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where tankNumber does not contain DEFAULT_TANK_NUMBER
        defaultMileageResponseShouldNotBeFound("tankNumber.doesNotContain=" + DEFAULT_TANK_NUMBER);

        // Get all the mileageResponseList where tankNumber does not contain UPDATED_TANK_NUMBER
        defaultMileageResponseShouldBeFound("tankNumber.doesNotContain=" + UPDATED_TANK_NUMBER);
    }

    @Test
    @Transactional
    void getAllMileageResponsesByMileageCurrentIsEqualToSomething() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where mileageCurrent equals to DEFAULT_MILEAGE_CURRENT
        defaultMileageResponseShouldBeFound("mileageCurrent.equals=" + DEFAULT_MILEAGE_CURRENT);

        // Get all the mileageResponseList where mileageCurrent equals to UPDATED_MILEAGE_CURRENT
        defaultMileageResponseShouldNotBeFound("mileageCurrent.equals=" + UPDATED_MILEAGE_CURRENT);
    }

    @Test
    @Transactional
    void getAllMileageResponsesByMileageCurrentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where mileageCurrent not equals to DEFAULT_MILEAGE_CURRENT
        defaultMileageResponseShouldNotBeFound("mileageCurrent.notEquals=" + DEFAULT_MILEAGE_CURRENT);

        // Get all the mileageResponseList where mileageCurrent not equals to UPDATED_MILEAGE_CURRENT
        defaultMileageResponseShouldBeFound("mileageCurrent.notEquals=" + UPDATED_MILEAGE_CURRENT);
    }

    @Test
    @Transactional
    void getAllMileageResponsesByMileageCurrentIsInShouldWork() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where mileageCurrent in DEFAULT_MILEAGE_CURRENT or UPDATED_MILEAGE_CURRENT
        defaultMileageResponseShouldBeFound("mileageCurrent.in=" + DEFAULT_MILEAGE_CURRENT + "," + UPDATED_MILEAGE_CURRENT);

        // Get all the mileageResponseList where mileageCurrent equals to UPDATED_MILEAGE_CURRENT
        defaultMileageResponseShouldNotBeFound("mileageCurrent.in=" + UPDATED_MILEAGE_CURRENT);
    }

    @Test
    @Transactional
    void getAllMileageResponsesByMileageCurrentIsNullOrNotNull() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where mileageCurrent is not null
        defaultMileageResponseShouldBeFound("mileageCurrent.specified=true");

        // Get all the mileageResponseList where mileageCurrent is null
        defaultMileageResponseShouldNotBeFound("mileageCurrent.specified=false");
    }

    @Test
    @Transactional
    void getAllMileageResponsesByMileageCurrentContainsSomething() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where mileageCurrent contains DEFAULT_MILEAGE_CURRENT
        defaultMileageResponseShouldBeFound("mileageCurrent.contains=" + DEFAULT_MILEAGE_CURRENT);

        // Get all the mileageResponseList where mileageCurrent contains UPDATED_MILEAGE_CURRENT
        defaultMileageResponseShouldNotBeFound("mileageCurrent.contains=" + UPDATED_MILEAGE_CURRENT);
    }

    @Test
    @Transactional
    void getAllMileageResponsesByMileageCurrentNotContainsSomething() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where mileageCurrent does not contain DEFAULT_MILEAGE_CURRENT
        defaultMileageResponseShouldNotBeFound("mileageCurrent.doesNotContain=" + DEFAULT_MILEAGE_CURRENT);

        // Get all the mileageResponseList where mileageCurrent does not contain UPDATED_MILEAGE_CURRENT
        defaultMileageResponseShouldBeFound("mileageCurrent.doesNotContain=" + UPDATED_MILEAGE_CURRENT);
    }

    @Test
    @Transactional
    void getAllMileageResponsesByMileageDatetimeIsEqualToSomething() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where mileageDatetime equals to DEFAULT_MILEAGE_DATETIME
        defaultMileageResponseShouldBeFound("mileageDatetime.equals=" + DEFAULT_MILEAGE_DATETIME);

        // Get all the mileageResponseList where mileageDatetime equals to UPDATED_MILEAGE_DATETIME
        defaultMileageResponseShouldNotBeFound("mileageDatetime.equals=" + UPDATED_MILEAGE_DATETIME);
    }

    @Test
    @Transactional
    void getAllMileageResponsesByMileageDatetimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where mileageDatetime not equals to DEFAULT_MILEAGE_DATETIME
        defaultMileageResponseShouldNotBeFound("mileageDatetime.notEquals=" + DEFAULT_MILEAGE_DATETIME);

        // Get all the mileageResponseList where mileageDatetime not equals to UPDATED_MILEAGE_DATETIME
        defaultMileageResponseShouldBeFound("mileageDatetime.notEquals=" + UPDATED_MILEAGE_DATETIME);
    }

    @Test
    @Transactional
    void getAllMileageResponsesByMileageDatetimeIsInShouldWork() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where mileageDatetime in DEFAULT_MILEAGE_DATETIME or UPDATED_MILEAGE_DATETIME
        defaultMileageResponseShouldBeFound("mileageDatetime.in=" + DEFAULT_MILEAGE_DATETIME + "," + UPDATED_MILEAGE_DATETIME);

        // Get all the mileageResponseList where mileageDatetime equals to UPDATED_MILEAGE_DATETIME
        defaultMileageResponseShouldNotBeFound("mileageDatetime.in=" + UPDATED_MILEAGE_DATETIME);
    }

    @Test
    @Transactional
    void getAllMileageResponsesByMileageDatetimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where mileageDatetime is not null
        defaultMileageResponseShouldBeFound("mileageDatetime.specified=true");

        // Get all the mileageResponseList where mileageDatetime is null
        defaultMileageResponseShouldNotBeFound("mileageDatetime.specified=false");
    }

    @Test
    @Transactional
    void getAllMileageResponsesByMileageDatetimeContainsSomething() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where mileageDatetime contains DEFAULT_MILEAGE_DATETIME
        defaultMileageResponseShouldBeFound("mileageDatetime.contains=" + DEFAULT_MILEAGE_DATETIME);

        // Get all the mileageResponseList where mileageDatetime contains UPDATED_MILEAGE_DATETIME
        defaultMileageResponseShouldNotBeFound("mileageDatetime.contains=" + UPDATED_MILEAGE_DATETIME);
    }

    @Test
    @Transactional
    void getAllMileageResponsesByMileageDatetimeNotContainsSomething() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where mileageDatetime does not contain DEFAULT_MILEAGE_DATETIME
        defaultMileageResponseShouldNotBeFound("mileageDatetime.doesNotContain=" + DEFAULT_MILEAGE_DATETIME);

        // Get all the mileageResponseList where mileageDatetime does not contain UPDATED_MILEAGE_DATETIME
        defaultMileageResponseShouldBeFound("mileageDatetime.doesNotContain=" + UPDATED_MILEAGE_DATETIME);
    }

    @Test
    @Transactional
    void getAllMileageResponsesByMileageRemainIsEqualToSomething() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where mileageRemain equals to DEFAULT_MILEAGE_REMAIN
        defaultMileageResponseShouldBeFound("mileageRemain.equals=" + DEFAULT_MILEAGE_REMAIN);

        // Get all the mileageResponseList where mileageRemain equals to UPDATED_MILEAGE_REMAIN
        defaultMileageResponseShouldNotBeFound("mileageRemain.equals=" + UPDATED_MILEAGE_REMAIN);
    }

    @Test
    @Transactional
    void getAllMileageResponsesByMileageRemainIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where mileageRemain not equals to DEFAULT_MILEAGE_REMAIN
        defaultMileageResponseShouldNotBeFound("mileageRemain.notEquals=" + DEFAULT_MILEAGE_REMAIN);

        // Get all the mileageResponseList where mileageRemain not equals to UPDATED_MILEAGE_REMAIN
        defaultMileageResponseShouldBeFound("mileageRemain.notEquals=" + UPDATED_MILEAGE_REMAIN);
    }

    @Test
    @Transactional
    void getAllMileageResponsesByMileageRemainIsInShouldWork() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where mileageRemain in DEFAULT_MILEAGE_REMAIN or UPDATED_MILEAGE_REMAIN
        defaultMileageResponseShouldBeFound("mileageRemain.in=" + DEFAULT_MILEAGE_REMAIN + "," + UPDATED_MILEAGE_REMAIN);

        // Get all the mileageResponseList where mileageRemain equals to UPDATED_MILEAGE_REMAIN
        defaultMileageResponseShouldNotBeFound("mileageRemain.in=" + UPDATED_MILEAGE_REMAIN);
    }

    @Test
    @Transactional
    void getAllMileageResponsesByMileageRemainIsNullOrNotNull() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where mileageRemain is not null
        defaultMileageResponseShouldBeFound("mileageRemain.specified=true");

        // Get all the mileageResponseList where mileageRemain is null
        defaultMileageResponseShouldNotBeFound("mileageRemain.specified=false");
    }

    @Test
    @Transactional
    void getAllMileageResponsesByMileageRemainContainsSomething() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where mileageRemain contains DEFAULT_MILEAGE_REMAIN
        defaultMileageResponseShouldBeFound("mileageRemain.contains=" + DEFAULT_MILEAGE_REMAIN);

        // Get all the mileageResponseList where mileageRemain contains UPDATED_MILEAGE_REMAIN
        defaultMileageResponseShouldNotBeFound("mileageRemain.contains=" + UPDATED_MILEAGE_REMAIN);
    }

    @Test
    @Transactional
    void getAllMileageResponsesByMileageRemainNotContainsSomething() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where mileageRemain does not contain DEFAULT_MILEAGE_REMAIN
        defaultMileageResponseShouldNotBeFound("mileageRemain.doesNotContain=" + DEFAULT_MILEAGE_REMAIN);

        // Get all the mileageResponseList where mileageRemain does not contain UPDATED_MILEAGE_REMAIN
        defaultMileageResponseShouldBeFound("mileageRemain.doesNotContain=" + UPDATED_MILEAGE_REMAIN);
    }

    @Test
    @Transactional
    void getAllMileageResponsesByMileageUpdateDatetimeIsEqualToSomething() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where mileageUpdateDatetime equals to DEFAULT_MILEAGE_UPDATE_DATETIME
        defaultMileageResponseShouldBeFound("mileageUpdateDatetime.equals=" + DEFAULT_MILEAGE_UPDATE_DATETIME);

        // Get all the mileageResponseList where mileageUpdateDatetime equals to UPDATED_MILEAGE_UPDATE_DATETIME
        defaultMileageResponseShouldNotBeFound("mileageUpdateDatetime.equals=" + UPDATED_MILEAGE_UPDATE_DATETIME);
    }

    @Test
    @Transactional
    void getAllMileageResponsesByMileageUpdateDatetimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where mileageUpdateDatetime not equals to DEFAULT_MILEAGE_UPDATE_DATETIME
        defaultMileageResponseShouldNotBeFound("mileageUpdateDatetime.notEquals=" + DEFAULT_MILEAGE_UPDATE_DATETIME);

        // Get all the mileageResponseList where mileageUpdateDatetime not equals to UPDATED_MILEAGE_UPDATE_DATETIME
        defaultMileageResponseShouldBeFound("mileageUpdateDatetime.notEquals=" + UPDATED_MILEAGE_UPDATE_DATETIME);
    }

    @Test
    @Transactional
    void getAllMileageResponsesByMileageUpdateDatetimeIsInShouldWork() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where mileageUpdateDatetime in DEFAULT_MILEAGE_UPDATE_DATETIME or UPDATED_MILEAGE_UPDATE_DATETIME
        defaultMileageResponseShouldBeFound(
            "mileageUpdateDatetime.in=" + DEFAULT_MILEAGE_UPDATE_DATETIME + "," + UPDATED_MILEAGE_UPDATE_DATETIME
        );

        // Get all the mileageResponseList where mileageUpdateDatetime equals to UPDATED_MILEAGE_UPDATE_DATETIME
        defaultMileageResponseShouldNotBeFound("mileageUpdateDatetime.in=" + UPDATED_MILEAGE_UPDATE_DATETIME);
    }

    @Test
    @Transactional
    void getAllMileageResponsesByMileageUpdateDatetimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where mileageUpdateDatetime is not null
        defaultMileageResponseShouldBeFound("mileageUpdateDatetime.specified=true");

        // Get all the mileageResponseList where mileageUpdateDatetime is null
        defaultMileageResponseShouldNotBeFound("mileageUpdateDatetime.specified=false");
    }

    @Test
    @Transactional
    void getAllMileageResponsesByMileageUpdateDatetimeContainsSomething() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where mileageUpdateDatetime contains DEFAULT_MILEAGE_UPDATE_DATETIME
        defaultMileageResponseShouldBeFound("mileageUpdateDatetime.contains=" + DEFAULT_MILEAGE_UPDATE_DATETIME);

        // Get all the mileageResponseList where mileageUpdateDatetime contains UPDATED_MILEAGE_UPDATE_DATETIME
        defaultMileageResponseShouldNotBeFound("mileageUpdateDatetime.contains=" + UPDATED_MILEAGE_UPDATE_DATETIME);
    }

    @Test
    @Transactional
    void getAllMileageResponsesByMileageUpdateDatetimeNotContainsSomething() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        // Get all the mileageResponseList where mileageUpdateDatetime does not contain DEFAULT_MILEAGE_UPDATE_DATETIME
        defaultMileageResponseShouldNotBeFound("mileageUpdateDatetime.doesNotContain=" + DEFAULT_MILEAGE_UPDATE_DATETIME);

        // Get all the mileageResponseList where mileageUpdateDatetime does not contain UPDATED_MILEAGE_UPDATE_DATETIME
        defaultMileageResponseShouldBeFound("mileageUpdateDatetime.doesNotContain=" + UPDATED_MILEAGE_UPDATE_DATETIME);
    }

    @Test
    @Transactional
    void getAllMileageResponsesByMileageRequestIsEqualToSomething() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);
        MileageRequest mileageRequest = MileageRequestResourceIT.createEntity(em);
        em.persist(mileageRequest);
        em.flush();
        mileageResponse.setMileageRequest(mileageRequest);
        mileageRequest.setMileageResponse(mileageResponse);
        mileageResponseRepository.saveAndFlush(mileageResponse);
        Long mileageRequestId = mileageRequest.getId();

        // Get all the mileageResponseList where mileageRequest equals to mileageRequestId
        defaultMileageResponseShouldBeFound("mileageRequestId.equals=" + mileageRequestId);

        // Get all the mileageResponseList where mileageRequest equals to (mileageRequestId + 1)
        defaultMileageResponseShouldNotBeFound("mileageRequestId.equals=" + (mileageRequestId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMileageResponseShouldBeFound(String filter) throws Exception {
        restMileageResponseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mileageResponse.getId().intValue())))
            .andExpect(jsonPath("$.[*].responseDatetime").value(hasItem(sameInstant(DEFAULT_RESPONSE_DATETIME))))
            .andExpect(jsonPath("$.[*].tankNumber").value(hasItem(DEFAULT_TANK_NUMBER)))
            .andExpect(jsonPath("$.[*].mileageCurrent").value(hasItem(DEFAULT_MILEAGE_CURRENT)))
            .andExpect(jsonPath("$.[*].mileageDatetime").value(hasItem(DEFAULT_MILEAGE_DATETIME)))
            .andExpect(jsonPath("$.[*].mileageRemain").value(hasItem(DEFAULT_MILEAGE_REMAIN)))
            .andExpect(jsonPath("$.[*].mileageUpdateDatetime").value(hasItem(DEFAULT_MILEAGE_UPDATE_DATETIME)));

        // Check, that the count call also returns 1
        restMileageResponseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMileageResponseShouldNotBeFound(String filter) throws Exception {
        restMileageResponseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMileageResponseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMileageResponse() throws Exception {
        // Get the mileageResponse
        restMileageResponseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMileageResponse() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        int databaseSizeBeforeUpdate = mileageResponseRepository.findAll().size();

        // Update the mileageResponse
        MileageResponse updatedMileageResponse = mileageResponseRepository.findById(mileageResponse.getId()).get();
        // Disconnect from session so that the updates on updatedMileageResponse are not directly saved in db
        em.detach(updatedMileageResponse);
        updatedMileageResponse
            .responseDatetime(UPDATED_RESPONSE_DATETIME)
            .tankNumber(UPDATED_TANK_NUMBER)
            .mileageCurrent(UPDATED_MILEAGE_CURRENT)
            .mileageDatetime(UPDATED_MILEAGE_DATETIME)
            .mileageRemain(UPDATED_MILEAGE_REMAIN)
            .mileageUpdateDatetime(UPDATED_MILEAGE_UPDATE_DATETIME);

        restMileageResponseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMileageResponse.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMileageResponse))
            )
            .andExpect(status().isOk());

        // Validate the MileageResponse in the database
        List<MileageResponse> mileageResponseList = mileageResponseRepository.findAll();
        assertThat(mileageResponseList).hasSize(databaseSizeBeforeUpdate);
        MileageResponse testMileageResponse = mileageResponseList.get(mileageResponseList.size() - 1);
        assertThat(testMileageResponse.getResponseDatetime()).isEqualTo(UPDATED_RESPONSE_DATETIME);
        assertThat(testMileageResponse.getTankNumber()).isEqualTo(UPDATED_TANK_NUMBER);
        assertThat(testMileageResponse.getMileageCurrent()).isEqualTo(UPDATED_MILEAGE_CURRENT);
        assertThat(testMileageResponse.getMileageDatetime()).isEqualTo(UPDATED_MILEAGE_DATETIME);
        assertThat(testMileageResponse.getMileageRemain()).isEqualTo(UPDATED_MILEAGE_REMAIN);
        assertThat(testMileageResponse.getMileageUpdateDatetime()).isEqualTo(UPDATED_MILEAGE_UPDATE_DATETIME);
    }

    @Test
    @Transactional
    void putNonExistingMileageResponse() throws Exception {
        int databaseSizeBeforeUpdate = mileageResponseRepository.findAll().size();
        mileageResponse.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMileageResponseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mileageResponse.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mileageResponse))
            )
            .andExpect(status().isBadRequest());

        // Validate the MileageResponse in the database
        List<MileageResponse> mileageResponseList = mileageResponseRepository.findAll();
        assertThat(mileageResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMileageResponse() throws Exception {
        int databaseSizeBeforeUpdate = mileageResponseRepository.findAll().size();
        mileageResponse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMileageResponseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mileageResponse))
            )
            .andExpect(status().isBadRequest());

        // Validate the MileageResponse in the database
        List<MileageResponse> mileageResponseList = mileageResponseRepository.findAll();
        assertThat(mileageResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMileageResponse() throws Exception {
        int databaseSizeBeforeUpdate = mileageResponseRepository.findAll().size();
        mileageResponse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMileageResponseMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mileageResponse))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MileageResponse in the database
        List<MileageResponse> mileageResponseList = mileageResponseRepository.findAll();
        assertThat(mileageResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMileageResponseWithPatch() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        int databaseSizeBeforeUpdate = mileageResponseRepository.findAll().size();

        // Update the mileageResponse using partial update
        MileageResponse partialUpdatedMileageResponse = new MileageResponse();
        partialUpdatedMileageResponse.setId(mileageResponse.getId());

        partialUpdatedMileageResponse
            .tankNumber(UPDATED_TANK_NUMBER)
            .mileageDatetime(UPDATED_MILEAGE_DATETIME)
            .mileageRemain(UPDATED_MILEAGE_REMAIN)
            .mileageUpdateDatetime(UPDATED_MILEAGE_UPDATE_DATETIME);

        restMileageResponseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMileageResponse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMileageResponse))
            )
            .andExpect(status().isOk());

        // Validate the MileageResponse in the database
        List<MileageResponse> mileageResponseList = mileageResponseRepository.findAll();
        assertThat(mileageResponseList).hasSize(databaseSizeBeforeUpdate);
        MileageResponse testMileageResponse = mileageResponseList.get(mileageResponseList.size() - 1);
        assertThat(testMileageResponse.getResponseDatetime()).isEqualTo(DEFAULT_RESPONSE_DATETIME);
        assertThat(testMileageResponse.getTankNumber()).isEqualTo(UPDATED_TANK_NUMBER);
        assertThat(testMileageResponse.getMileageCurrent()).isEqualTo(DEFAULT_MILEAGE_CURRENT);
        assertThat(testMileageResponse.getMileageDatetime()).isEqualTo(UPDATED_MILEAGE_DATETIME);
        assertThat(testMileageResponse.getMileageRemain()).isEqualTo(UPDATED_MILEAGE_REMAIN);
        assertThat(testMileageResponse.getMileageUpdateDatetime()).isEqualTo(UPDATED_MILEAGE_UPDATE_DATETIME);
    }

    @Test
    @Transactional
    void fullUpdateMileageResponseWithPatch() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        int databaseSizeBeforeUpdate = mileageResponseRepository.findAll().size();

        // Update the mileageResponse using partial update
        MileageResponse partialUpdatedMileageResponse = new MileageResponse();
        partialUpdatedMileageResponse.setId(mileageResponse.getId());

        partialUpdatedMileageResponse
            .responseDatetime(UPDATED_RESPONSE_DATETIME)
            .tankNumber(UPDATED_TANK_NUMBER)
            .mileageCurrent(UPDATED_MILEAGE_CURRENT)
            .mileageDatetime(UPDATED_MILEAGE_DATETIME)
            .mileageRemain(UPDATED_MILEAGE_REMAIN)
            .mileageUpdateDatetime(UPDATED_MILEAGE_UPDATE_DATETIME);

        restMileageResponseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMileageResponse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMileageResponse))
            )
            .andExpect(status().isOk());

        // Validate the MileageResponse in the database
        List<MileageResponse> mileageResponseList = mileageResponseRepository.findAll();
        assertThat(mileageResponseList).hasSize(databaseSizeBeforeUpdate);
        MileageResponse testMileageResponse = mileageResponseList.get(mileageResponseList.size() - 1);
        assertThat(testMileageResponse.getResponseDatetime()).isEqualTo(UPDATED_RESPONSE_DATETIME);
        assertThat(testMileageResponse.getTankNumber()).isEqualTo(UPDATED_TANK_NUMBER);
        assertThat(testMileageResponse.getMileageCurrent()).isEqualTo(UPDATED_MILEAGE_CURRENT);
        assertThat(testMileageResponse.getMileageDatetime()).isEqualTo(UPDATED_MILEAGE_DATETIME);
        assertThat(testMileageResponse.getMileageRemain()).isEqualTo(UPDATED_MILEAGE_REMAIN);
        assertThat(testMileageResponse.getMileageUpdateDatetime()).isEqualTo(UPDATED_MILEAGE_UPDATE_DATETIME);
    }

    @Test
    @Transactional
    void patchNonExistingMileageResponse() throws Exception {
        int databaseSizeBeforeUpdate = mileageResponseRepository.findAll().size();
        mileageResponse.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMileageResponseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mileageResponse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mileageResponse))
            )
            .andExpect(status().isBadRequest());

        // Validate the MileageResponse in the database
        List<MileageResponse> mileageResponseList = mileageResponseRepository.findAll();
        assertThat(mileageResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMileageResponse() throws Exception {
        int databaseSizeBeforeUpdate = mileageResponseRepository.findAll().size();
        mileageResponse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMileageResponseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mileageResponse))
            )
            .andExpect(status().isBadRequest());

        // Validate the MileageResponse in the database
        List<MileageResponse> mileageResponseList = mileageResponseRepository.findAll();
        assertThat(mileageResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMileageResponse() throws Exception {
        int databaseSizeBeforeUpdate = mileageResponseRepository.findAll().size();
        mileageResponse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMileageResponseMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mileageResponse))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MileageResponse in the database
        List<MileageResponse> mileageResponseList = mileageResponseRepository.findAll();
        assertThat(mileageResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMileageResponse() throws Exception {
        // Initialize the database
        mileageResponseRepository.saveAndFlush(mileageResponse);

        int databaseSizeBeforeDelete = mileageResponseRepository.findAll().size();

        // Delete the mileageResponse
        restMileageResponseMockMvc
            .perform(delete(ENTITY_API_URL_ID, mileageResponse.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MileageResponse> mileageResponseList = mileageResponseRepository.findAll();
        assertThat(mileageResponseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
