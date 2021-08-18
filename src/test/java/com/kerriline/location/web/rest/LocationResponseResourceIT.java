package com.kerriline.location.web.rest;

import static com.kerriline.location.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.kerriline.location.IntegrationTest;
import com.kerriline.location.domain.LocationRequest;
import com.kerriline.location.domain.LocationResponse;
import com.kerriline.location.repository.LocationResponseRepository;
import com.kerriline.location.service.criteria.LocationResponseCriteria;
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
 * Integration tests for the {@link LocationResponseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LocationResponseResourceIT {

    private static final ZonedDateTime DEFAULT_RESPONSE_DATETIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_RESPONSE_DATETIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_RESPONSE_DATETIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_TANK_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TANK_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_TANK_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TANK_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_CARGO_ID = "AAAAAAAAAA";
    private static final String UPDATED_CARGO_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CARGO_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CARGO_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_WEIGHT = "AAAAAAAAAA";
    private static final String UPDATED_WEIGHT = "BBBBBBBBBB";

    private static final String DEFAULT_RECEIVER_ID = "AAAAAAAAAA";
    private static final String UPDATED_RECEIVER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_TANK_INDEX = "AAAAAAAAAA";
    private static final String UPDATED_TANK_INDEX = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION_STATION_ID = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION_STATION_ID = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION_STATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION_STATION_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION_DATETIME = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION_DATETIME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION_OPERATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION_OPERATION = "BBBBBBBBBB";

    private static final String DEFAULT_STATE_FROM_STATION_ID = "AAAAAAAAAA";
    private static final String UPDATED_STATE_FROM_STATION_ID = "BBBBBBBBBB";

    private static final String DEFAULT_STATE_FROM_STATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_STATE_FROM_STATION_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STATE_TO_STATION_ID = "AAAAAAAAAA";
    private static final String UPDATED_STATE_TO_STATION_ID = "BBBBBBBBBB";

    private static final String DEFAULT_STATE_TO_STATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_STATE_TO_STATION_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STATE_SEND_DATETIME = "AAAAAAAAAA";
    private static final String UPDATED_STATE_SEND_DATETIME = "BBBBBBBBBB";

    private static final String DEFAULT_STATE_SENDER_ID = "AAAAAAAAAA";
    private static final String UPDATED_STATE_SENDER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PLANED_SERVICE_DATETIME = "AAAAAAAAAA";
    private static final String UPDATED_PLANED_SERVICE_DATETIME = "BBBBBBBBBB";

    private static final String DEFAULT_TANK_OWNER = "AAAAAAAAAA";
    private static final String UPDATED_TANK_OWNER = "BBBBBBBBBB";

    private static final String DEFAULT_TANK_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_TANK_MODEL = "BBBBBBBBBB";

    private static final String DEFAULT_DEFECT_REGION = "AAAAAAAAAA";
    private static final String UPDATED_DEFECT_REGION = "BBBBBBBBBB";

    private static final String DEFAULT_DEFECT_STATION = "AAAAAAAAAA";
    private static final String UPDATED_DEFECT_STATION = "BBBBBBBBBB";

    private static final String DEFAULT_DEFECT_DATETIME = "AAAAAAAAAA";
    private static final String UPDATED_DEFECT_DATETIME = "BBBBBBBBBB";

    private static final String DEFAULT_DEFECT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_DEFECT_DETAILS = "BBBBBBBBBB";

    private static final String DEFAULT_REPAIR_REGION = "AAAAAAAAAA";
    private static final String UPDATED_REPAIR_REGION = "BBBBBBBBBB";

    private static final String DEFAULT_REPAIR_STATION = "AAAAAAAAAA";
    private static final String UPDATED_REPAIR_STATION = "BBBBBBBBBB";

    private static final String DEFAULT_REPAIR_DATETIME = "AAAAAAAAAA";
    private static final String UPDATED_REPAIR_DATETIME = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATE_DATETIME = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE_DATETIME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/location-responses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LocationResponseRepository locationResponseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocationResponseMockMvc;

    private LocationResponse locationResponse;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocationResponse createEntity(EntityManager em) {
        LocationResponse locationResponse = new LocationResponse()
            .responseDatetime(DEFAULT_RESPONSE_DATETIME)
            .tankNumber(DEFAULT_TANK_NUMBER)
            .tankType(DEFAULT_TANK_TYPE)
            .cargoId(DEFAULT_CARGO_ID)
            .cargoName(DEFAULT_CARGO_NAME)
            .weight(DEFAULT_WEIGHT)
            .receiverId(DEFAULT_RECEIVER_ID)
            .tankIndex(DEFAULT_TANK_INDEX)
            .locationStationId(DEFAULT_LOCATION_STATION_ID)
            .locationStationName(DEFAULT_LOCATION_STATION_NAME)
            .locationDatetime(DEFAULT_LOCATION_DATETIME)
            .locationOperation(DEFAULT_LOCATION_OPERATION)
            .stateFromStationId(DEFAULT_STATE_FROM_STATION_ID)
            .stateFromStationName(DEFAULT_STATE_FROM_STATION_NAME)
            .stateToStationId(DEFAULT_STATE_TO_STATION_ID)
            .stateToStationName(DEFAULT_STATE_TO_STATION_NAME)
            .stateSendDatetime(DEFAULT_STATE_SEND_DATETIME)
            .stateSenderId(DEFAULT_STATE_SENDER_ID)
            .planedServiceDatetime(DEFAULT_PLANED_SERVICE_DATETIME)
            .tankOwner(DEFAULT_TANK_OWNER)
            .tankModel(DEFAULT_TANK_MODEL)
            .defectRegion(DEFAULT_DEFECT_REGION)
            .defectStation(DEFAULT_DEFECT_STATION)
            .defectDatetime(DEFAULT_DEFECT_DATETIME)
            .defectDetails(DEFAULT_DEFECT_DETAILS)
            .repairRegion(DEFAULT_REPAIR_REGION)
            .repairStation(DEFAULT_REPAIR_STATION)
            .repairDatetime(DEFAULT_REPAIR_DATETIME)
            .updateDatetime(DEFAULT_UPDATE_DATETIME);
        return locationResponse;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocationResponse createUpdatedEntity(EntityManager em) {
        LocationResponse locationResponse = new LocationResponse()
            .responseDatetime(UPDATED_RESPONSE_DATETIME)
            .tankNumber(UPDATED_TANK_NUMBER)
            .tankType(UPDATED_TANK_TYPE)
            .cargoId(UPDATED_CARGO_ID)
            .cargoName(UPDATED_CARGO_NAME)
            .weight(UPDATED_WEIGHT)
            .receiverId(UPDATED_RECEIVER_ID)
            .tankIndex(UPDATED_TANK_INDEX)
            .locationStationId(UPDATED_LOCATION_STATION_ID)
            .locationStationName(UPDATED_LOCATION_STATION_NAME)
            .locationDatetime(UPDATED_LOCATION_DATETIME)
            .locationOperation(UPDATED_LOCATION_OPERATION)
            .stateFromStationId(UPDATED_STATE_FROM_STATION_ID)
            .stateFromStationName(UPDATED_STATE_FROM_STATION_NAME)
            .stateToStationId(UPDATED_STATE_TO_STATION_ID)
            .stateToStationName(UPDATED_STATE_TO_STATION_NAME)
            .stateSendDatetime(UPDATED_STATE_SEND_DATETIME)
            .stateSenderId(UPDATED_STATE_SENDER_ID)
            .planedServiceDatetime(UPDATED_PLANED_SERVICE_DATETIME)
            .tankOwner(UPDATED_TANK_OWNER)
            .tankModel(UPDATED_TANK_MODEL)
            .defectRegion(UPDATED_DEFECT_REGION)
            .defectStation(UPDATED_DEFECT_STATION)
            .defectDatetime(UPDATED_DEFECT_DATETIME)
            .defectDetails(UPDATED_DEFECT_DETAILS)
            .repairRegion(UPDATED_REPAIR_REGION)
            .repairStation(UPDATED_REPAIR_STATION)
            .repairDatetime(UPDATED_REPAIR_DATETIME)
            .updateDatetime(UPDATED_UPDATE_DATETIME);
        return locationResponse;
    }

    @BeforeEach
    public void initTest() {
        locationResponse = createEntity(em);
    }

    @Test
    @Transactional
    void createLocationResponse() throws Exception {
        int databaseSizeBeforeCreate = locationResponseRepository.findAll().size();
        // Create the LocationResponse
        restLocationResponseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationResponse))
            )
            .andExpect(status().isCreated());

        // Validate the LocationResponse in the database
        List<LocationResponse> locationResponseList = locationResponseRepository.findAll();
        assertThat(locationResponseList).hasSize(databaseSizeBeforeCreate + 1);
        LocationResponse testLocationResponse = locationResponseList.get(locationResponseList.size() - 1);
        assertThat(testLocationResponse.getResponseDatetime()).isEqualTo(DEFAULT_RESPONSE_DATETIME);
        assertThat(testLocationResponse.getTankNumber()).isEqualTo(DEFAULT_TANK_NUMBER);
        assertThat(testLocationResponse.getTankType()).isEqualTo(DEFAULT_TANK_TYPE);
        assertThat(testLocationResponse.getCargoId()).isEqualTo(DEFAULT_CARGO_ID);
        assertThat(testLocationResponse.getCargoName()).isEqualTo(DEFAULT_CARGO_NAME);
        assertThat(testLocationResponse.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testLocationResponse.getReceiverId()).isEqualTo(DEFAULT_RECEIVER_ID);
        assertThat(testLocationResponse.getTankIndex()).isEqualTo(DEFAULT_TANK_INDEX);
        assertThat(testLocationResponse.getLocationStationId()).isEqualTo(DEFAULT_LOCATION_STATION_ID);
        assertThat(testLocationResponse.getLocationStationName()).isEqualTo(DEFAULT_LOCATION_STATION_NAME);
        assertThat(testLocationResponse.getLocationDatetime()).isEqualTo(DEFAULT_LOCATION_DATETIME);
        assertThat(testLocationResponse.getLocationOperation()).isEqualTo(DEFAULT_LOCATION_OPERATION);
        assertThat(testLocationResponse.getStateFromStationId()).isEqualTo(DEFAULT_STATE_FROM_STATION_ID);
        assertThat(testLocationResponse.getStateFromStationName()).isEqualTo(DEFAULT_STATE_FROM_STATION_NAME);
        assertThat(testLocationResponse.getStateToStationId()).isEqualTo(DEFAULT_STATE_TO_STATION_ID);
        assertThat(testLocationResponse.getStateToStationName()).isEqualTo(DEFAULT_STATE_TO_STATION_NAME);
        assertThat(testLocationResponse.getStateSendDatetime()).isEqualTo(DEFAULT_STATE_SEND_DATETIME);
        assertThat(testLocationResponse.getStateSenderId()).isEqualTo(DEFAULT_STATE_SENDER_ID);
        assertThat(testLocationResponse.getPlanedServiceDatetime()).isEqualTo(DEFAULT_PLANED_SERVICE_DATETIME);
        assertThat(testLocationResponse.getTankOwner()).isEqualTo(DEFAULT_TANK_OWNER);
        assertThat(testLocationResponse.getTankModel()).isEqualTo(DEFAULT_TANK_MODEL);
        assertThat(testLocationResponse.getDefectRegion()).isEqualTo(DEFAULT_DEFECT_REGION);
        assertThat(testLocationResponse.getDefectStation()).isEqualTo(DEFAULT_DEFECT_STATION);
        assertThat(testLocationResponse.getDefectDatetime()).isEqualTo(DEFAULT_DEFECT_DATETIME);
        assertThat(testLocationResponse.getDefectDetails()).isEqualTo(DEFAULT_DEFECT_DETAILS);
        assertThat(testLocationResponse.getRepairRegion()).isEqualTo(DEFAULT_REPAIR_REGION);
        assertThat(testLocationResponse.getRepairStation()).isEqualTo(DEFAULT_REPAIR_STATION);
        assertThat(testLocationResponse.getRepairDatetime()).isEqualTo(DEFAULT_REPAIR_DATETIME);
        assertThat(testLocationResponse.getUpdateDatetime()).isEqualTo(DEFAULT_UPDATE_DATETIME);
    }

    @Test
    @Transactional
    void createLocationResponseWithExistingId() throws Exception {
        // Create the LocationResponse with an existing ID
        locationResponse.setId(1L);

        int databaseSizeBeforeCreate = locationResponseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocationResponseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationResponse))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationResponse in the database
        List<LocationResponse> locationResponseList = locationResponseRepository.findAll();
        assertThat(locationResponseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLocationResponses() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList
        restLocationResponseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(locationResponse.getId().intValue())))
            .andExpect(jsonPath("$.[*].responseDatetime").value(hasItem(sameInstant(DEFAULT_RESPONSE_DATETIME))))
            .andExpect(jsonPath("$.[*].tankNumber").value(hasItem(DEFAULT_TANK_NUMBER)))
            .andExpect(jsonPath("$.[*].tankType").value(hasItem(DEFAULT_TANK_TYPE)))
            .andExpect(jsonPath("$.[*].cargoId").value(hasItem(DEFAULT_CARGO_ID)))
            .andExpect(jsonPath("$.[*].cargoName").value(hasItem(DEFAULT_CARGO_NAME)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)))
            .andExpect(jsonPath("$.[*].receiverId").value(hasItem(DEFAULT_RECEIVER_ID)))
            .andExpect(jsonPath("$.[*].tankIndex").value(hasItem(DEFAULT_TANK_INDEX)))
            .andExpect(jsonPath("$.[*].locationStationId").value(hasItem(DEFAULT_LOCATION_STATION_ID)))
            .andExpect(jsonPath("$.[*].locationStationName").value(hasItem(DEFAULT_LOCATION_STATION_NAME)))
            .andExpect(jsonPath("$.[*].locationDatetime").value(hasItem(DEFAULT_LOCATION_DATETIME)))
            .andExpect(jsonPath("$.[*].locationOperation").value(hasItem(DEFAULT_LOCATION_OPERATION)))
            .andExpect(jsonPath("$.[*].stateFromStationId").value(hasItem(DEFAULT_STATE_FROM_STATION_ID)))
            .andExpect(jsonPath("$.[*].stateFromStationName").value(hasItem(DEFAULT_STATE_FROM_STATION_NAME)))
            .andExpect(jsonPath("$.[*].stateToStationId").value(hasItem(DEFAULT_STATE_TO_STATION_ID)))
            .andExpect(jsonPath("$.[*].stateToStationName").value(hasItem(DEFAULT_STATE_TO_STATION_NAME)))
            .andExpect(jsonPath("$.[*].stateSendDatetime").value(hasItem(DEFAULT_STATE_SEND_DATETIME)))
            .andExpect(jsonPath("$.[*].stateSenderId").value(hasItem(DEFAULT_STATE_SENDER_ID)))
            .andExpect(jsonPath("$.[*].planedServiceDatetime").value(hasItem(DEFAULT_PLANED_SERVICE_DATETIME)))
            .andExpect(jsonPath("$.[*].tankOwner").value(hasItem(DEFAULT_TANK_OWNER)))
            .andExpect(jsonPath("$.[*].tankModel").value(hasItem(DEFAULT_TANK_MODEL)))
            .andExpect(jsonPath("$.[*].defectRegion").value(hasItem(DEFAULT_DEFECT_REGION)))
            .andExpect(jsonPath("$.[*].defectStation").value(hasItem(DEFAULT_DEFECT_STATION)))
            .andExpect(jsonPath("$.[*].defectDatetime").value(hasItem(DEFAULT_DEFECT_DATETIME)))
            .andExpect(jsonPath("$.[*].defectDetails").value(hasItem(DEFAULT_DEFECT_DETAILS)))
            .andExpect(jsonPath("$.[*].repairRegion").value(hasItem(DEFAULT_REPAIR_REGION)))
            .andExpect(jsonPath("$.[*].repairStation").value(hasItem(DEFAULT_REPAIR_STATION)))
            .andExpect(jsonPath("$.[*].repairDatetime").value(hasItem(DEFAULT_REPAIR_DATETIME)))
            .andExpect(jsonPath("$.[*].updateDatetime").value(hasItem(DEFAULT_UPDATE_DATETIME)));
    }

    @Test
    @Transactional
    void getLocationResponse() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get the locationResponse
        restLocationResponseMockMvc
            .perform(get(ENTITY_API_URL_ID, locationResponse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(locationResponse.getId().intValue()))
            .andExpect(jsonPath("$.responseDatetime").value(sameInstant(DEFAULT_RESPONSE_DATETIME)))
            .andExpect(jsonPath("$.tankNumber").value(DEFAULT_TANK_NUMBER))
            .andExpect(jsonPath("$.tankType").value(DEFAULT_TANK_TYPE))
            .andExpect(jsonPath("$.cargoId").value(DEFAULT_CARGO_ID))
            .andExpect(jsonPath("$.cargoName").value(DEFAULT_CARGO_NAME))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT))
            .andExpect(jsonPath("$.receiverId").value(DEFAULT_RECEIVER_ID))
            .andExpect(jsonPath("$.tankIndex").value(DEFAULT_TANK_INDEX))
            .andExpect(jsonPath("$.locationStationId").value(DEFAULT_LOCATION_STATION_ID))
            .andExpect(jsonPath("$.locationStationName").value(DEFAULT_LOCATION_STATION_NAME))
            .andExpect(jsonPath("$.locationDatetime").value(DEFAULT_LOCATION_DATETIME))
            .andExpect(jsonPath("$.locationOperation").value(DEFAULT_LOCATION_OPERATION))
            .andExpect(jsonPath("$.stateFromStationId").value(DEFAULT_STATE_FROM_STATION_ID))
            .andExpect(jsonPath("$.stateFromStationName").value(DEFAULT_STATE_FROM_STATION_NAME))
            .andExpect(jsonPath("$.stateToStationId").value(DEFAULT_STATE_TO_STATION_ID))
            .andExpect(jsonPath("$.stateToStationName").value(DEFAULT_STATE_TO_STATION_NAME))
            .andExpect(jsonPath("$.stateSendDatetime").value(DEFAULT_STATE_SEND_DATETIME))
            .andExpect(jsonPath("$.stateSenderId").value(DEFAULT_STATE_SENDER_ID))
            .andExpect(jsonPath("$.planedServiceDatetime").value(DEFAULT_PLANED_SERVICE_DATETIME))
            .andExpect(jsonPath("$.tankOwner").value(DEFAULT_TANK_OWNER))
            .andExpect(jsonPath("$.tankModel").value(DEFAULT_TANK_MODEL))
            .andExpect(jsonPath("$.defectRegion").value(DEFAULT_DEFECT_REGION))
            .andExpect(jsonPath("$.defectStation").value(DEFAULT_DEFECT_STATION))
            .andExpect(jsonPath("$.defectDatetime").value(DEFAULT_DEFECT_DATETIME))
            .andExpect(jsonPath("$.defectDetails").value(DEFAULT_DEFECT_DETAILS))
            .andExpect(jsonPath("$.repairRegion").value(DEFAULT_REPAIR_REGION))
            .andExpect(jsonPath("$.repairStation").value(DEFAULT_REPAIR_STATION))
            .andExpect(jsonPath("$.repairDatetime").value(DEFAULT_REPAIR_DATETIME))
            .andExpect(jsonPath("$.updateDatetime").value(DEFAULT_UPDATE_DATETIME));
    }

    @Test
    @Transactional
    void getLocationResponsesByIdFiltering() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        Long id = locationResponse.getId();

        defaultLocationResponseShouldBeFound("id.equals=" + id);
        defaultLocationResponseShouldNotBeFound("id.notEquals=" + id);

        defaultLocationResponseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLocationResponseShouldNotBeFound("id.greaterThan=" + id);

        defaultLocationResponseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLocationResponseShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByResponseDatetimeIsEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where responseDatetime equals to DEFAULT_RESPONSE_DATETIME
        defaultLocationResponseShouldBeFound("responseDatetime.equals=" + DEFAULT_RESPONSE_DATETIME);

        // Get all the locationResponseList where responseDatetime equals to UPDATED_RESPONSE_DATETIME
        defaultLocationResponseShouldNotBeFound("responseDatetime.equals=" + UPDATED_RESPONSE_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByResponseDatetimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where responseDatetime not equals to DEFAULT_RESPONSE_DATETIME
        defaultLocationResponseShouldNotBeFound("responseDatetime.notEquals=" + DEFAULT_RESPONSE_DATETIME);

        // Get all the locationResponseList where responseDatetime not equals to UPDATED_RESPONSE_DATETIME
        defaultLocationResponseShouldBeFound("responseDatetime.notEquals=" + UPDATED_RESPONSE_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByResponseDatetimeIsInShouldWork() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where responseDatetime in DEFAULT_RESPONSE_DATETIME or UPDATED_RESPONSE_DATETIME
        defaultLocationResponseShouldBeFound("responseDatetime.in=" + DEFAULT_RESPONSE_DATETIME + "," + UPDATED_RESPONSE_DATETIME);

        // Get all the locationResponseList where responseDatetime equals to UPDATED_RESPONSE_DATETIME
        defaultLocationResponseShouldNotBeFound("responseDatetime.in=" + UPDATED_RESPONSE_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByResponseDatetimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where responseDatetime is not null
        defaultLocationResponseShouldBeFound("responseDatetime.specified=true");

        // Get all the locationResponseList where responseDatetime is null
        defaultLocationResponseShouldNotBeFound("responseDatetime.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationResponsesByResponseDatetimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where responseDatetime is greater than or equal to DEFAULT_RESPONSE_DATETIME
        defaultLocationResponseShouldBeFound("responseDatetime.greaterThanOrEqual=" + DEFAULT_RESPONSE_DATETIME);

        // Get all the locationResponseList where responseDatetime is greater than or equal to UPDATED_RESPONSE_DATETIME
        defaultLocationResponseShouldNotBeFound("responseDatetime.greaterThanOrEqual=" + UPDATED_RESPONSE_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByResponseDatetimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where responseDatetime is less than or equal to DEFAULT_RESPONSE_DATETIME
        defaultLocationResponseShouldBeFound("responseDatetime.lessThanOrEqual=" + DEFAULT_RESPONSE_DATETIME);

        // Get all the locationResponseList where responseDatetime is less than or equal to SMALLER_RESPONSE_DATETIME
        defaultLocationResponseShouldNotBeFound("responseDatetime.lessThanOrEqual=" + SMALLER_RESPONSE_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByResponseDatetimeIsLessThanSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where responseDatetime is less than DEFAULT_RESPONSE_DATETIME
        defaultLocationResponseShouldNotBeFound("responseDatetime.lessThan=" + DEFAULT_RESPONSE_DATETIME);

        // Get all the locationResponseList where responseDatetime is less than UPDATED_RESPONSE_DATETIME
        defaultLocationResponseShouldBeFound("responseDatetime.lessThan=" + UPDATED_RESPONSE_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByResponseDatetimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where responseDatetime is greater than DEFAULT_RESPONSE_DATETIME
        defaultLocationResponseShouldNotBeFound("responseDatetime.greaterThan=" + DEFAULT_RESPONSE_DATETIME);

        // Get all the locationResponseList where responseDatetime is greater than SMALLER_RESPONSE_DATETIME
        defaultLocationResponseShouldBeFound("responseDatetime.greaterThan=" + SMALLER_RESPONSE_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByTankNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where tankNumber equals to DEFAULT_TANK_NUMBER
        defaultLocationResponseShouldBeFound("tankNumber.equals=" + DEFAULT_TANK_NUMBER);

        // Get all the locationResponseList where tankNumber equals to UPDATED_TANK_NUMBER
        defaultLocationResponseShouldNotBeFound("tankNumber.equals=" + UPDATED_TANK_NUMBER);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByTankNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where tankNumber not equals to DEFAULT_TANK_NUMBER
        defaultLocationResponseShouldNotBeFound("tankNumber.notEquals=" + DEFAULT_TANK_NUMBER);

        // Get all the locationResponseList where tankNumber not equals to UPDATED_TANK_NUMBER
        defaultLocationResponseShouldBeFound("tankNumber.notEquals=" + UPDATED_TANK_NUMBER);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByTankNumberIsInShouldWork() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where tankNumber in DEFAULT_TANK_NUMBER or UPDATED_TANK_NUMBER
        defaultLocationResponseShouldBeFound("tankNumber.in=" + DEFAULT_TANK_NUMBER + "," + UPDATED_TANK_NUMBER);

        // Get all the locationResponseList where tankNumber equals to UPDATED_TANK_NUMBER
        defaultLocationResponseShouldNotBeFound("tankNumber.in=" + UPDATED_TANK_NUMBER);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByTankNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where tankNumber is not null
        defaultLocationResponseShouldBeFound("tankNumber.specified=true");

        // Get all the locationResponseList where tankNumber is null
        defaultLocationResponseShouldNotBeFound("tankNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationResponsesByTankNumberContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where tankNumber contains DEFAULT_TANK_NUMBER
        defaultLocationResponseShouldBeFound("tankNumber.contains=" + DEFAULT_TANK_NUMBER);

        // Get all the locationResponseList where tankNumber contains UPDATED_TANK_NUMBER
        defaultLocationResponseShouldNotBeFound("tankNumber.contains=" + UPDATED_TANK_NUMBER);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByTankNumberNotContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where tankNumber does not contain DEFAULT_TANK_NUMBER
        defaultLocationResponseShouldNotBeFound("tankNumber.doesNotContain=" + DEFAULT_TANK_NUMBER);

        // Get all the locationResponseList where tankNumber does not contain UPDATED_TANK_NUMBER
        defaultLocationResponseShouldBeFound("tankNumber.doesNotContain=" + UPDATED_TANK_NUMBER);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByTankTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where tankType equals to DEFAULT_TANK_TYPE
        defaultLocationResponseShouldBeFound("tankType.equals=" + DEFAULT_TANK_TYPE);

        // Get all the locationResponseList where tankType equals to UPDATED_TANK_TYPE
        defaultLocationResponseShouldNotBeFound("tankType.equals=" + UPDATED_TANK_TYPE);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByTankTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where tankType not equals to DEFAULT_TANK_TYPE
        defaultLocationResponseShouldNotBeFound("tankType.notEquals=" + DEFAULT_TANK_TYPE);

        // Get all the locationResponseList where tankType not equals to UPDATED_TANK_TYPE
        defaultLocationResponseShouldBeFound("tankType.notEquals=" + UPDATED_TANK_TYPE);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByTankTypeIsInShouldWork() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where tankType in DEFAULT_TANK_TYPE or UPDATED_TANK_TYPE
        defaultLocationResponseShouldBeFound("tankType.in=" + DEFAULT_TANK_TYPE + "," + UPDATED_TANK_TYPE);

        // Get all the locationResponseList where tankType equals to UPDATED_TANK_TYPE
        defaultLocationResponseShouldNotBeFound("tankType.in=" + UPDATED_TANK_TYPE);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByTankTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where tankType is not null
        defaultLocationResponseShouldBeFound("tankType.specified=true");

        // Get all the locationResponseList where tankType is null
        defaultLocationResponseShouldNotBeFound("tankType.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationResponsesByTankTypeContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where tankType contains DEFAULT_TANK_TYPE
        defaultLocationResponseShouldBeFound("tankType.contains=" + DEFAULT_TANK_TYPE);

        // Get all the locationResponseList where tankType contains UPDATED_TANK_TYPE
        defaultLocationResponseShouldNotBeFound("tankType.contains=" + UPDATED_TANK_TYPE);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByTankTypeNotContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where tankType does not contain DEFAULT_TANK_TYPE
        defaultLocationResponseShouldNotBeFound("tankType.doesNotContain=" + DEFAULT_TANK_TYPE);

        // Get all the locationResponseList where tankType does not contain UPDATED_TANK_TYPE
        defaultLocationResponseShouldBeFound("tankType.doesNotContain=" + UPDATED_TANK_TYPE);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByCargoIdIsEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where cargoId equals to DEFAULT_CARGO_ID
        defaultLocationResponseShouldBeFound("cargoId.equals=" + DEFAULT_CARGO_ID);

        // Get all the locationResponseList where cargoId equals to UPDATED_CARGO_ID
        defaultLocationResponseShouldNotBeFound("cargoId.equals=" + UPDATED_CARGO_ID);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByCargoIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where cargoId not equals to DEFAULT_CARGO_ID
        defaultLocationResponseShouldNotBeFound("cargoId.notEquals=" + DEFAULT_CARGO_ID);

        // Get all the locationResponseList where cargoId not equals to UPDATED_CARGO_ID
        defaultLocationResponseShouldBeFound("cargoId.notEquals=" + UPDATED_CARGO_ID);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByCargoIdIsInShouldWork() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where cargoId in DEFAULT_CARGO_ID or UPDATED_CARGO_ID
        defaultLocationResponseShouldBeFound("cargoId.in=" + DEFAULT_CARGO_ID + "," + UPDATED_CARGO_ID);

        // Get all the locationResponseList where cargoId equals to UPDATED_CARGO_ID
        defaultLocationResponseShouldNotBeFound("cargoId.in=" + UPDATED_CARGO_ID);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByCargoIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where cargoId is not null
        defaultLocationResponseShouldBeFound("cargoId.specified=true");

        // Get all the locationResponseList where cargoId is null
        defaultLocationResponseShouldNotBeFound("cargoId.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationResponsesByCargoIdContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where cargoId contains DEFAULT_CARGO_ID
        defaultLocationResponseShouldBeFound("cargoId.contains=" + DEFAULT_CARGO_ID);

        // Get all the locationResponseList where cargoId contains UPDATED_CARGO_ID
        defaultLocationResponseShouldNotBeFound("cargoId.contains=" + UPDATED_CARGO_ID);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByCargoIdNotContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where cargoId does not contain DEFAULT_CARGO_ID
        defaultLocationResponseShouldNotBeFound("cargoId.doesNotContain=" + DEFAULT_CARGO_ID);

        // Get all the locationResponseList where cargoId does not contain UPDATED_CARGO_ID
        defaultLocationResponseShouldBeFound("cargoId.doesNotContain=" + UPDATED_CARGO_ID);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByCargoNameIsEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where cargoName equals to DEFAULT_CARGO_NAME
        defaultLocationResponseShouldBeFound("cargoName.equals=" + DEFAULT_CARGO_NAME);

        // Get all the locationResponseList where cargoName equals to UPDATED_CARGO_NAME
        defaultLocationResponseShouldNotBeFound("cargoName.equals=" + UPDATED_CARGO_NAME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByCargoNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where cargoName not equals to DEFAULT_CARGO_NAME
        defaultLocationResponseShouldNotBeFound("cargoName.notEquals=" + DEFAULT_CARGO_NAME);

        // Get all the locationResponseList where cargoName not equals to UPDATED_CARGO_NAME
        defaultLocationResponseShouldBeFound("cargoName.notEquals=" + UPDATED_CARGO_NAME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByCargoNameIsInShouldWork() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where cargoName in DEFAULT_CARGO_NAME or UPDATED_CARGO_NAME
        defaultLocationResponseShouldBeFound("cargoName.in=" + DEFAULT_CARGO_NAME + "," + UPDATED_CARGO_NAME);

        // Get all the locationResponseList where cargoName equals to UPDATED_CARGO_NAME
        defaultLocationResponseShouldNotBeFound("cargoName.in=" + UPDATED_CARGO_NAME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByCargoNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where cargoName is not null
        defaultLocationResponseShouldBeFound("cargoName.specified=true");

        // Get all the locationResponseList where cargoName is null
        defaultLocationResponseShouldNotBeFound("cargoName.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationResponsesByCargoNameContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where cargoName contains DEFAULT_CARGO_NAME
        defaultLocationResponseShouldBeFound("cargoName.contains=" + DEFAULT_CARGO_NAME);

        // Get all the locationResponseList where cargoName contains UPDATED_CARGO_NAME
        defaultLocationResponseShouldNotBeFound("cargoName.contains=" + UPDATED_CARGO_NAME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByCargoNameNotContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where cargoName does not contain DEFAULT_CARGO_NAME
        defaultLocationResponseShouldNotBeFound("cargoName.doesNotContain=" + DEFAULT_CARGO_NAME);

        // Get all the locationResponseList where cargoName does not contain UPDATED_CARGO_NAME
        defaultLocationResponseShouldBeFound("cargoName.doesNotContain=" + UPDATED_CARGO_NAME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByWeightIsEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where weight equals to DEFAULT_WEIGHT
        defaultLocationResponseShouldBeFound("weight.equals=" + DEFAULT_WEIGHT);

        // Get all the locationResponseList where weight equals to UPDATED_WEIGHT
        defaultLocationResponseShouldNotBeFound("weight.equals=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByWeightIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where weight not equals to DEFAULT_WEIGHT
        defaultLocationResponseShouldNotBeFound("weight.notEquals=" + DEFAULT_WEIGHT);

        // Get all the locationResponseList where weight not equals to UPDATED_WEIGHT
        defaultLocationResponseShouldBeFound("weight.notEquals=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByWeightIsInShouldWork() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where weight in DEFAULT_WEIGHT or UPDATED_WEIGHT
        defaultLocationResponseShouldBeFound("weight.in=" + DEFAULT_WEIGHT + "," + UPDATED_WEIGHT);

        // Get all the locationResponseList where weight equals to UPDATED_WEIGHT
        defaultLocationResponseShouldNotBeFound("weight.in=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByWeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where weight is not null
        defaultLocationResponseShouldBeFound("weight.specified=true");

        // Get all the locationResponseList where weight is null
        defaultLocationResponseShouldNotBeFound("weight.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationResponsesByWeightContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where weight contains DEFAULT_WEIGHT
        defaultLocationResponseShouldBeFound("weight.contains=" + DEFAULT_WEIGHT);

        // Get all the locationResponseList where weight contains UPDATED_WEIGHT
        defaultLocationResponseShouldNotBeFound("weight.contains=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByWeightNotContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where weight does not contain DEFAULT_WEIGHT
        defaultLocationResponseShouldNotBeFound("weight.doesNotContain=" + DEFAULT_WEIGHT);

        // Get all the locationResponseList where weight does not contain UPDATED_WEIGHT
        defaultLocationResponseShouldBeFound("weight.doesNotContain=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByReceiverIdIsEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where receiverId equals to DEFAULT_RECEIVER_ID
        defaultLocationResponseShouldBeFound("receiverId.equals=" + DEFAULT_RECEIVER_ID);

        // Get all the locationResponseList where receiverId equals to UPDATED_RECEIVER_ID
        defaultLocationResponseShouldNotBeFound("receiverId.equals=" + UPDATED_RECEIVER_ID);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByReceiverIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where receiverId not equals to DEFAULT_RECEIVER_ID
        defaultLocationResponseShouldNotBeFound("receiverId.notEquals=" + DEFAULT_RECEIVER_ID);

        // Get all the locationResponseList where receiverId not equals to UPDATED_RECEIVER_ID
        defaultLocationResponseShouldBeFound("receiverId.notEquals=" + UPDATED_RECEIVER_ID);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByReceiverIdIsInShouldWork() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where receiverId in DEFAULT_RECEIVER_ID or UPDATED_RECEIVER_ID
        defaultLocationResponseShouldBeFound("receiverId.in=" + DEFAULT_RECEIVER_ID + "," + UPDATED_RECEIVER_ID);

        // Get all the locationResponseList where receiverId equals to UPDATED_RECEIVER_ID
        defaultLocationResponseShouldNotBeFound("receiverId.in=" + UPDATED_RECEIVER_ID);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByReceiverIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where receiverId is not null
        defaultLocationResponseShouldBeFound("receiverId.specified=true");

        // Get all the locationResponseList where receiverId is null
        defaultLocationResponseShouldNotBeFound("receiverId.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationResponsesByReceiverIdContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where receiverId contains DEFAULT_RECEIVER_ID
        defaultLocationResponseShouldBeFound("receiverId.contains=" + DEFAULT_RECEIVER_ID);

        // Get all the locationResponseList where receiverId contains UPDATED_RECEIVER_ID
        defaultLocationResponseShouldNotBeFound("receiverId.contains=" + UPDATED_RECEIVER_ID);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByReceiverIdNotContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where receiverId does not contain DEFAULT_RECEIVER_ID
        defaultLocationResponseShouldNotBeFound("receiverId.doesNotContain=" + DEFAULT_RECEIVER_ID);

        // Get all the locationResponseList where receiverId does not contain UPDATED_RECEIVER_ID
        defaultLocationResponseShouldBeFound("receiverId.doesNotContain=" + UPDATED_RECEIVER_ID);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByTankIndexIsEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where tankIndex equals to DEFAULT_TANK_INDEX
        defaultLocationResponseShouldBeFound("tankIndex.equals=" + DEFAULT_TANK_INDEX);

        // Get all the locationResponseList where tankIndex equals to UPDATED_TANK_INDEX
        defaultLocationResponseShouldNotBeFound("tankIndex.equals=" + UPDATED_TANK_INDEX);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByTankIndexIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where tankIndex not equals to DEFAULT_TANK_INDEX
        defaultLocationResponseShouldNotBeFound("tankIndex.notEquals=" + DEFAULT_TANK_INDEX);

        // Get all the locationResponseList where tankIndex not equals to UPDATED_TANK_INDEX
        defaultLocationResponseShouldBeFound("tankIndex.notEquals=" + UPDATED_TANK_INDEX);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByTankIndexIsInShouldWork() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where tankIndex in DEFAULT_TANK_INDEX or UPDATED_TANK_INDEX
        defaultLocationResponseShouldBeFound("tankIndex.in=" + DEFAULT_TANK_INDEX + "," + UPDATED_TANK_INDEX);

        // Get all the locationResponseList where tankIndex equals to UPDATED_TANK_INDEX
        defaultLocationResponseShouldNotBeFound("tankIndex.in=" + UPDATED_TANK_INDEX);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByTankIndexIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where tankIndex is not null
        defaultLocationResponseShouldBeFound("tankIndex.specified=true");

        // Get all the locationResponseList where tankIndex is null
        defaultLocationResponseShouldNotBeFound("tankIndex.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationResponsesByTankIndexContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where tankIndex contains DEFAULT_TANK_INDEX
        defaultLocationResponseShouldBeFound("tankIndex.contains=" + DEFAULT_TANK_INDEX);

        // Get all the locationResponseList where tankIndex contains UPDATED_TANK_INDEX
        defaultLocationResponseShouldNotBeFound("tankIndex.contains=" + UPDATED_TANK_INDEX);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByTankIndexNotContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where tankIndex does not contain DEFAULT_TANK_INDEX
        defaultLocationResponseShouldNotBeFound("tankIndex.doesNotContain=" + DEFAULT_TANK_INDEX);

        // Get all the locationResponseList where tankIndex does not contain UPDATED_TANK_INDEX
        defaultLocationResponseShouldBeFound("tankIndex.doesNotContain=" + UPDATED_TANK_INDEX);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByLocationStationIdIsEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where locationStationId equals to DEFAULT_LOCATION_STATION_ID
        defaultLocationResponseShouldBeFound("locationStationId.equals=" + DEFAULT_LOCATION_STATION_ID);

        // Get all the locationResponseList where locationStationId equals to UPDATED_LOCATION_STATION_ID
        defaultLocationResponseShouldNotBeFound("locationStationId.equals=" + UPDATED_LOCATION_STATION_ID);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByLocationStationIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where locationStationId not equals to DEFAULT_LOCATION_STATION_ID
        defaultLocationResponseShouldNotBeFound("locationStationId.notEquals=" + DEFAULT_LOCATION_STATION_ID);

        // Get all the locationResponseList where locationStationId not equals to UPDATED_LOCATION_STATION_ID
        defaultLocationResponseShouldBeFound("locationStationId.notEquals=" + UPDATED_LOCATION_STATION_ID);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByLocationStationIdIsInShouldWork() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where locationStationId in DEFAULT_LOCATION_STATION_ID or UPDATED_LOCATION_STATION_ID
        defaultLocationResponseShouldBeFound("locationStationId.in=" + DEFAULT_LOCATION_STATION_ID + "," + UPDATED_LOCATION_STATION_ID);

        // Get all the locationResponseList where locationStationId equals to UPDATED_LOCATION_STATION_ID
        defaultLocationResponseShouldNotBeFound("locationStationId.in=" + UPDATED_LOCATION_STATION_ID);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByLocationStationIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where locationStationId is not null
        defaultLocationResponseShouldBeFound("locationStationId.specified=true");

        // Get all the locationResponseList where locationStationId is null
        defaultLocationResponseShouldNotBeFound("locationStationId.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationResponsesByLocationStationIdContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where locationStationId contains DEFAULT_LOCATION_STATION_ID
        defaultLocationResponseShouldBeFound("locationStationId.contains=" + DEFAULT_LOCATION_STATION_ID);

        // Get all the locationResponseList where locationStationId contains UPDATED_LOCATION_STATION_ID
        defaultLocationResponseShouldNotBeFound("locationStationId.contains=" + UPDATED_LOCATION_STATION_ID);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByLocationStationIdNotContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where locationStationId does not contain DEFAULT_LOCATION_STATION_ID
        defaultLocationResponseShouldNotBeFound("locationStationId.doesNotContain=" + DEFAULT_LOCATION_STATION_ID);

        // Get all the locationResponseList where locationStationId does not contain UPDATED_LOCATION_STATION_ID
        defaultLocationResponseShouldBeFound("locationStationId.doesNotContain=" + UPDATED_LOCATION_STATION_ID);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByLocationStationNameIsEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where locationStationName equals to DEFAULT_LOCATION_STATION_NAME
        defaultLocationResponseShouldBeFound("locationStationName.equals=" + DEFAULT_LOCATION_STATION_NAME);

        // Get all the locationResponseList where locationStationName equals to UPDATED_LOCATION_STATION_NAME
        defaultLocationResponseShouldNotBeFound("locationStationName.equals=" + UPDATED_LOCATION_STATION_NAME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByLocationStationNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where locationStationName not equals to DEFAULT_LOCATION_STATION_NAME
        defaultLocationResponseShouldNotBeFound("locationStationName.notEquals=" + DEFAULT_LOCATION_STATION_NAME);

        // Get all the locationResponseList where locationStationName not equals to UPDATED_LOCATION_STATION_NAME
        defaultLocationResponseShouldBeFound("locationStationName.notEquals=" + UPDATED_LOCATION_STATION_NAME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByLocationStationNameIsInShouldWork() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where locationStationName in DEFAULT_LOCATION_STATION_NAME or UPDATED_LOCATION_STATION_NAME
        defaultLocationResponseShouldBeFound(
            "locationStationName.in=" + DEFAULT_LOCATION_STATION_NAME + "," + UPDATED_LOCATION_STATION_NAME
        );

        // Get all the locationResponseList where locationStationName equals to UPDATED_LOCATION_STATION_NAME
        defaultLocationResponseShouldNotBeFound("locationStationName.in=" + UPDATED_LOCATION_STATION_NAME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByLocationStationNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where locationStationName is not null
        defaultLocationResponseShouldBeFound("locationStationName.specified=true");

        // Get all the locationResponseList where locationStationName is null
        defaultLocationResponseShouldNotBeFound("locationStationName.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationResponsesByLocationStationNameContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where locationStationName contains DEFAULT_LOCATION_STATION_NAME
        defaultLocationResponseShouldBeFound("locationStationName.contains=" + DEFAULT_LOCATION_STATION_NAME);

        // Get all the locationResponseList where locationStationName contains UPDATED_LOCATION_STATION_NAME
        defaultLocationResponseShouldNotBeFound("locationStationName.contains=" + UPDATED_LOCATION_STATION_NAME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByLocationStationNameNotContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where locationStationName does not contain DEFAULT_LOCATION_STATION_NAME
        defaultLocationResponseShouldNotBeFound("locationStationName.doesNotContain=" + DEFAULT_LOCATION_STATION_NAME);

        // Get all the locationResponseList where locationStationName does not contain UPDATED_LOCATION_STATION_NAME
        defaultLocationResponseShouldBeFound("locationStationName.doesNotContain=" + UPDATED_LOCATION_STATION_NAME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByLocationDatetimeIsEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where locationDatetime equals to DEFAULT_LOCATION_DATETIME
        defaultLocationResponseShouldBeFound("locationDatetime.equals=" + DEFAULT_LOCATION_DATETIME);

        // Get all the locationResponseList where locationDatetime equals to UPDATED_LOCATION_DATETIME
        defaultLocationResponseShouldNotBeFound("locationDatetime.equals=" + UPDATED_LOCATION_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByLocationDatetimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where locationDatetime not equals to DEFAULT_LOCATION_DATETIME
        defaultLocationResponseShouldNotBeFound("locationDatetime.notEquals=" + DEFAULT_LOCATION_DATETIME);

        // Get all the locationResponseList where locationDatetime not equals to UPDATED_LOCATION_DATETIME
        defaultLocationResponseShouldBeFound("locationDatetime.notEquals=" + UPDATED_LOCATION_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByLocationDatetimeIsInShouldWork() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where locationDatetime in DEFAULT_LOCATION_DATETIME or UPDATED_LOCATION_DATETIME
        defaultLocationResponseShouldBeFound("locationDatetime.in=" + DEFAULT_LOCATION_DATETIME + "," + UPDATED_LOCATION_DATETIME);

        // Get all the locationResponseList where locationDatetime equals to UPDATED_LOCATION_DATETIME
        defaultLocationResponseShouldNotBeFound("locationDatetime.in=" + UPDATED_LOCATION_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByLocationDatetimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where locationDatetime is not null
        defaultLocationResponseShouldBeFound("locationDatetime.specified=true");

        // Get all the locationResponseList where locationDatetime is null
        defaultLocationResponseShouldNotBeFound("locationDatetime.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationResponsesByLocationDatetimeContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where locationDatetime contains DEFAULT_LOCATION_DATETIME
        defaultLocationResponseShouldBeFound("locationDatetime.contains=" + DEFAULT_LOCATION_DATETIME);

        // Get all the locationResponseList where locationDatetime contains UPDATED_LOCATION_DATETIME
        defaultLocationResponseShouldNotBeFound("locationDatetime.contains=" + UPDATED_LOCATION_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByLocationDatetimeNotContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where locationDatetime does not contain DEFAULT_LOCATION_DATETIME
        defaultLocationResponseShouldNotBeFound("locationDatetime.doesNotContain=" + DEFAULT_LOCATION_DATETIME);

        // Get all the locationResponseList where locationDatetime does not contain UPDATED_LOCATION_DATETIME
        defaultLocationResponseShouldBeFound("locationDatetime.doesNotContain=" + UPDATED_LOCATION_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByLocationOperationIsEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where locationOperation equals to DEFAULT_LOCATION_OPERATION
        defaultLocationResponseShouldBeFound("locationOperation.equals=" + DEFAULT_LOCATION_OPERATION);

        // Get all the locationResponseList where locationOperation equals to UPDATED_LOCATION_OPERATION
        defaultLocationResponseShouldNotBeFound("locationOperation.equals=" + UPDATED_LOCATION_OPERATION);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByLocationOperationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where locationOperation not equals to DEFAULT_LOCATION_OPERATION
        defaultLocationResponseShouldNotBeFound("locationOperation.notEquals=" + DEFAULT_LOCATION_OPERATION);

        // Get all the locationResponseList where locationOperation not equals to UPDATED_LOCATION_OPERATION
        defaultLocationResponseShouldBeFound("locationOperation.notEquals=" + UPDATED_LOCATION_OPERATION);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByLocationOperationIsInShouldWork() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where locationOperation in DEFAULT_LOCATION_OPERATION or UPDATED_LOCATION_OPERATION
        defaultLocationResponseShouldBeFound("locationOperation.in=" + DEFAULT_LOCATION_OPERATION + "," + UPDATED_LOCATION_OPERATION);

        // Get all the locationResponseList where locationOperation equals to UPDATED_LOCATION_OPERATION
        defaultLocationResponseShouldNotBeFound("locationOperation.in=" + UPDATED_LOCATION_OPERATION);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByLocationOperationIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where locationOperation is not null
        defaultLocationResponseShouldBeFound("locationOperation.specified=true");

        // Get all the locationResponseList where locationOperation is null
        defaultLocationResponseShouldNotBeFound("locationOperation.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationResponsesByLocationOperationContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where locationOperation contains DEFAULT_LOCATION_OPERATION
        defaultLocationResponseShouldBeFound("locationOperation.contains=" + DEFAULT_LOCATION_OPERATION);

        // Get all the locationResponseList where locationOperation contains UPDATED_LOCATION_OPERATION
        defaultLocationResponseShouldNotBeFound("locationOperation.contains=" + UPDATED_LOCATION_OPERATION);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByLocationOperationNotContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where locationOperation does not contain DEFAULT_LOCATION_OPERATION
        defaultLocationResponseShouldNotBeFound("locationOperation.doesNotContain=" + DEFAULT_LOCATION_OPERATION);

        // Get all the locationResponseList where locationOperation does not contain UPDATED_LOCATION_OPERATION
        defaultLocationResponseShouldBeFound("locationOperation.doesNotContain=" + UPDATED_LOCATION_OPERATION);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateFromStationIdIsEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateFromStationId equals to DEFAULT_STATE_FROM_STATION_ID
        defaultLocationResponseShouldBeFound("stateFromStationId.equals=" + DEFAULT_STATE_FROM_STATION_ID);

        // Get all the locationResponseList where stateFromStationId equals to UPDATED_STATE_FROM_STATION_ID
        defaultLocationResponseShouldNotBeFound("stateFromStationId.equals=" + UPDATED_STATE_FROM_STATION_ID);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateFromStationIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateFromStationId not equals to DEFAULT_STATE_FROM_STATION_ID
        defaultLocationResponseShouldNotBeFound("stateFromStationId.notEquals=" + DEFAULT_STATE_FROM_STATION_ID);

        // Get all the locationResponseList where stateFromStationId not equals to UPDATED_STATE_FROM_STATION_ID
        defaultLocationResponseShouldBeFound("stateFromStationId.notEquals=" + UPDATED_STATE_FROM_STATION_ID);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateFromStationIdIsInShouldWork() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateFromStationId in DEFAULT_STATE_FROM_STATION_ID or UPDATED_STATE_FROM_STATION_ID
        defaultLocationResponseShouldBeFound(
            "stateFromStationId.in=" + DEFAULT_STATE_FROM_STATION_ID + "," + UPDATED_STATE_FROM_STATION_ID
        );

        // Get all the locationResponseList where stateFromStationId equals to UPDATED_STATE_FROM_STATION_ID
        defaultLocationResponseShouldNotBeFound("stateFromStationId.in=" + UPDATED_STATE_FROM_STATION_ID);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateFromStationIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateFromStationId is not null
        defaultLocationResponseShouldBeFound("stateFromStationId.specified=true");

        // Get all the locationResponseList where stateFromStationId is null
        defaultLocationResponseShouldNotBeFound("stateFromStationId.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateFromStationIdContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateFromStationId contains DEFAULT_STATE_FROM_STATION_ID
        defaultLocationResponseShouldBeFound("stateFromStationId.contains=" + DEFAULT_STATE_FROM_STATION_ID);

        // Get all the locationResponseList where stateFromStationId contains UPDATED_STATE_FROM_STATION_ID
        defaultLocationResponseShouldNotBeFound("stateFromStationId.contains=" + UPDATED_STATE_FROM_STATION_ID);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateFromStationIdNotContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateFromStationId does not contain DEFAULT_STATE_FROM_STATION_ID
        defaultLocationResponseShouldNotBeFound("stateFromStationId.doesNotContain=" + DEFAULT_STATE_FROM_STATION_ID);

        // Get all the locationResponseList where stateFromStationId does not contain UPDATED_STATE_FROM_STATION_ID
        defaultLocationResponseShouldBeFound("stateFromStationId.doesNotContain=" + UPDATED_STATE_FROM_STATION_ID);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateFromStationNameIsEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateFromStationName equals to DEFAULT_STATE_FROM_STATION_NAME
        defaultLocationResponseShouldBeFound("stateFromStationName.equals=" + DEFAULT_STATE_FROM_STATION_NAME);

        // Get all the locationResponseList where stateFromStationName equals to UPDATED_STATE_FROM_STATION_NAME
        defaultLocationResponseShouldNotBeFound("stateFromStationName.equals=" + UPDATED_STATE_FROM_STATION_NAME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateFromStationNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateFromStationName not equals to DEFAULT_STATE_FROM_STATION_NAME
        defaultLocationResponseShouldNotBeFound("stateFromStationName.notEquals=" + DEFAULT_STATE_FROM_STATION_NAME);

        // Get all the locationResponseList where stateFromStationName not equals to UPDATED_STATE_FROM_STATION_NAME
        defaultLocationResponseShouldBeFound("stateFromStationName.notEquals=" + UPDATED_STATE_FROM_STATION_NAME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateFromStationNameIsInShouldWork() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateFromStationName in DEFAULT_STATE_FROM_STATION_NAME or UPDATED_STATE_FROM_STATION_NAME
        defaultLocationResponseShouldBeFound(
            "stateFromStationName.in=" + DEFAULT_STATE_FROM_STATION_NAME + "," + UPDATED_STATE_FROM_STATION_NAME
        );

        // Get all the locationResponseList where stateFromStationName equals to UPDATED_STATE_FROM_STATION_NAME
        defaultLocationResponseShouldNotBeFound("stateFromStationName.in=" + UPDATED_STATE_FROM_STATION_NAME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateFromStationNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateFromStationName is not null
        defaultLocationResponseShouldBeFound("stateFromStationName.specified=true");

        // Get all the locationResponseList where stateFromStationName is null
        defaultLocationResponseShouldNotBeFound("stateFromStationName.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateFromStationNameContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateFromStationName contains DEFAULT_STATE_FROM_STATION_NAME
        defaultLocationResponseShouldBeFound("stateFromStationName.contains=" + DEFAULT_STATE_FROM_STATION_NAME);

        // Get all the locationResponseList where stateFromStationName contains UPDATED_STATE_FROM_STATION_NAME
        defaultLocationResponseShouldNotBeFound("stateFromStationName.contains=" + UPDATED_STATE_FROM_STATION_NAME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateFromStationNameNotContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateFromStationName does not contain DEFAULT_STATE_FROM_STATION_NAME
        defaultLocationResponseShouldNotBeFound("stateFromStationName.doesNotContain=" + DEFAULT_STATE_FROM_STATION_NAME);

        // Get all the locationResponseList where stateFromStationName does not contain UPDATED_STATE_FROM_STATION_NAME
        defaultLocationResponseShouldBeFound("stateFromStationName.doesNotContain=" + UPDATED_STATE_FROM_STATION_NAME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateToStationIdIsEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateToStationId equals to DEFAULT_STATE_TO_STATION_ID
        defaultLocationResponseShouldBeFound("stateToStationId.equals=" + DEFAULT_STATE_TO_STATION_ID);

        // Get all the locationResponseList where stateToStationId equals to UPDATED_STATE_TO_STATION_ID
        defaultLocationResponseShouldNotBeFound("stateToStationId.equals=" + UPDATED_STATE_TO_STATION_ID);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateToStationIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateToStationId not equals to DEFAULT_STATE_TO_STATION_ID
        defaultLocationResponseShouldNotBeFound("stateToStationId.notEquals=" + DEFAULT_STATE_TO_STATION_ID);

        // Get all the locationResponseList where stateToStationId not equals to UPDATED_STATE_TO_STATION_ID
        defaultLocationResponseShouldBeFound("stateToStationId.notEquals=" + UPDATED_STATE_TO_STATION_ID);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateToStationIdIsInShouldWork() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateToStationId in DEFAULT_STATE_TO_STATION_ID or UPDATED_STATE_TO_STATION_ID
        defaultLocationResponseShouldBeFound("stateToStationId.in=" + DEFAULT_STATE_TO_STATION_ID + "," + UPDATED_STATE_TO_STATION_ID);

        // Get all the locationResponseList where stateToStationId equals to UPDATED_STATE_TO_STATION_ID
        defaultLocationResponseShouldNotBeFound("stateToStationId.in=" + UPDATED_STATE_TO_STATION_ID);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateToStationIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateToStationId is not null
        defaultLocationResponseShouldBeFound("stateToStationId.specified=true");

        // Get all the locationResponseList where stateToStationId is null
        defaultLocationResponseShouldNotBeFound("stateToStationId.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateToStationIdContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateToStationId contains DEFAULT_STATE_TO_STATION_ID
        defaultLocationResponseShouldBeFound("stateToStationId.contains=" + DEFAULT_STATE_TO_STATION_ID);

        // Get all the locationResponseList where stateToStationId contains UPDATED_STATE_TO_STATION_ID
        defaultLocationResponseShouldNotBeFound("stateToStationId.contains=" + UPDATED_STATE_TO_STATION_ID);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateToStationIdNotContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateToStationId does not contain DEFAULT_STATE_TO_STATION_ID
        defaultLocationResponseShouldNotBeFound("stateToStationId.doesNotContain=" + DEFAULT_STATE_TO_STATION_ID);

        // Get all the locationResponseList where stateToStationId does not contain UPDATED_STATE_TO_STATION_ID
        defaultLocationResponseShouldBeFound("stateToStationId.doesNotContain=" + UPDATED_STATE_TO_STATION_ID);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateToStationNameIsEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateToStationName equals to DEFAULT_STATE_TO_STATION_NAME
        defaultLocationResponseShouldBeFound("stateToStationName.equals=" + DEFAULT_STATE_TO_STATION_NAME);

        // Get all the locationResponseList where stateToStationName equals to UPDATED_STATE_TO_STATION_NAME
        defaultLocationResponseShouldNotBeFound("stateToStationName.equals=" + UPDATED_STATE_TO_STATION_NAME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateToStationNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateToStationName not equals to DEFAULT_STATE_TO_STATION_NAME
        defaultLocationResponseShouldNotBeFound("stateToStationName.notEquals=" + DEFAULT_STATE_TO_STATION_NAME);

        // Get all the locationResponseList where stateToStationName not equals to UPDATED_STATE_TO_STATION_NAME
        defaultLocationResponseShouldBeFound("stateToStationName.notEquals=" + UPDATED_STATE_TO_STATION_NAME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateToStationNameIsInShouldWork() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateToStationName in DEFAULT_STATE_TO_STATION_NAME or UPDATED_STATE_TO_STATION_NAME
        defaultLocationResponseShouldBeFound(
            "stateToStationName.in=" + DEFAULT_STATE_TO_STATION_NAME + "," + UPDATED_STATE_TO_STATION_NAME
        );

        // Get all the locationResponseList where stateToStationName equals to UPDATED_STATE_TO_STATION_NAME
        defaultLocationResponseShouldNotBeFound("stateToStationName.in=" + UPDATED_STATE_TO_STATION_NAME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateToStationNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateToStationName is not null
        defaultLocationResponseShouldBeFound("stateToStationName.specified=true");

        // Get all the locationResponseList where stateToStationName is null
        defaultLocationResponseShouldNotBeFound("stateToStationName.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateToStationNameContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateToStationName contains DEFAULT_STATE_TO_STATION_NAME
        defaultLocationResponseShouldBeFound("stateToStationName.contains=" + DEFAULT_STATE_TO_STATION_NAME);

        // Get all the locationResponseList where stateToStationName contains UPDATED_STATE_TO_STATION_NAME
        defaultLocationResponseShouldNotBeFound("stateToStationName.contains=" + UPDATED_STATE_TO_STATION_NAME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateToStationNameNotContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateToStationName does not contain DEFAULT_STATE_TO_STATION_NAME
        defaultLocationResponseShouldNotBeFound("stateToStationName.doesNotContain=" + DEFAULT_STATE_TO_STATION_NAME);

        // Get all the locationResponseList where stateToStationName does not contain UPDATED_STATE_TO_STATION_NAME
        defaultLocationResponseShouldBeFound("stateToStationName.doesNotContain=" + UPDATED_STATE_TO_STATION_NAME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateSendDatetimeIsEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateSendDatetime equals to DEFAULT_STATE_SEND_DATETIME
        defaultLocationResponseShouldBeFound("stateSendDatetime.equals=" + DEFAULT_STATE_SEND_DATETIME);

        // Get all the locationResponseList where stateSendDatetime equals to UPDATED_STATE_SEND_DATETIME
        defaultLocationResponseShouldNotBeFound("stateSendDatetime.equals=" + UPDATED_STATE_SEND_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateSendDatetimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateSendDatetime not equals to DEFAULT_STATE_SEND_DATETIME
        defaultLocationResponseShouldNotBeFound("stateSendDatetime.notEquals=" + DEFAULT_STATE_SEND_DATETIME);

        // Get all the locationResponseList where stateSendDatetime not equals to UPDATED_STATE_SEND_DATETIME
        defaultLocationResponseShouldBeFound("stateSendDatetime.notEquals=" + UPDATED_STATE_SEND_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateSendDatetimeIsInShouldWork() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateSendDatetime in DEFAULT_STATE_SEND_DATETIME or UPDATED_STATE_SEND_DATETIME
        defaultLocationResponseShouldBeFound("stateSendDatetime.in=" + DEFAULT_STATE_SEND_DATETIME + "," + UPDATED_STATE_SEND_DATETIME);

        // Get all the locationResponseList where stateSendDatetime equals to UPDATED_STATE_SEND_DATETIME
        defaultLocationResponseShouldNotBeFound("stateSendDatetime.in=" + UPDATED_STATE_SEND_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateSendDatetimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateSendDatetime is not null
        defaultLocationResponseShouldBeFound("stateSendDatetime.specified=true");

        // Get all the locationResponseList where stateSendDatetime is null
        defaultLocationResponseShouldNotBeFound("stateSendDatetime.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateSendDatetimeContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateSendDatetime contains DEFAULT_STATE_SEND_DATETIME
        defaultLocationResponseShouldBeFound("stateSendDatetime.contains=" + DEFAULT_STATE_SEND_DATETIME);

        // Get all the locationResponseList where stateSendDatetime contains UPDATED_STATE_SEND_DATETIME
        defaultLocationResponseShouldNotBeFound("stateSendDatetime.contains=" + UPDATED_STATE_SEND_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateSendDatetimeNotContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateSendDatetime does not contain DEFAULT_STATE_SEND_DATETIME
        defaultLocationResponseShouldNotBeFound("stateSendDatetime.doesNotContain=" + DEFAULT_STATE_SEND_DATETIME);

        // Get all the locationResponseList where stateSendDatetime does not contain UPDATED_STATE_SEND_DATETIME
        defaultLocationResponseShouldBeFound("stateSendDatetime.doesNotContain=" + UPDATED_STATE_SEND_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateSenderIdIsEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateSenderId equals to DEFAULT_STATE_SENDER_ID
        defaultLocationResponseShouldBeFound("stateSenderId.equals=" + DEFAULT_STATE_SENDER_ID);

        // Get all the locationResponseList where stateSenderId equals to UPDATED_STATE_SENDER_ID
        defaultLocationResponseShouldNotBeFound("stateSenderId.equals=" + UPDATED_STATE_SENDER_ID);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateSenderIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateSenderId not equals to DEFAULT_STATE_SENDER_ID
        defaultLocationResponseShouldNotBeFound("stateSenderId.notEquals=" + DEFAULT_STATE_SENDER_ID);

        // Get all the locationResponseList where stateSenderId not equals to UPDATED_STATE_SENDER_ID
        defaultLocationResponseShouldBeFound("stateSenderId.notEquals=" + UPDATED_STATE_SENDER_ID);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateSenderIdIsInShouldWork() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateSenderId in DEFAULT_STATE_SENDER_ID or UPDATED_STATE_SENDER_ID
        defaultLocationResponseShouldBeFound("stateSenderId.in=" + DEFAULT_STATE_SENDER_ID + "," + UPDATED_STATE_SENDER_ID);

        // Get all the locationResponseList where stateSenderId equals to UPDATED_STATE_SENDER_ID
        defaultLocationResponseShouldNotBeFound("stateSenderId.in=" + UPDATED_STATE_SENDER_ID);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateSenderIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateSenderId is not null
        defaultLocationResponseShouldBeFound("stateSenderId.specified=true");

        // Get all the locationResponseList where stateSenderId is null
        defaultLocationResponseShouldNotBeFound("stateSenderId.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateSenderIdContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateSenderId contains DEFAULT_STATE_SENDER_ID
        defaultLocationResponseShouldBeFound("stateSenderId.contains=" + DEFAULT_STATE_SENDER_ID);

        // Get all the locationResponseList where stateSenderId contains UPDATED_STATE_SENDER_ID
        defaultLocationResponseShouldNotBeFound("stateSenderId.contains=" + UPDATED_STATE_SENDER_ID);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByStateSenderIdNotContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where stateSenderId does not contain DEFAULT_STATE_SENDER_ID
        defaultLocationResponseShouldNotBeFound("stateSenderId.doesNotContain=" + DEFAULT_STATE_SENDER_ID);

        // Get all the locationResponseList where stateSenderId does not contain UPDATED_STATE_SENDER_ID
        defaultLocationResponseShouldBeFound("stateSenderId.doesNotContain=" + UPDATED_STATE_SENDER_ID);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByPlanedServiceDatetimeIsEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where planedServiceDatetime equals to DEFAULT_PLANED_SERVICE_DATETIME
        defaultLocationResponseShouldBeFound("planedServiceDatetime.equals=" + DEFAULT_PLANED_SERVICE_DATETIME);

        // Get all the locationResponseList where planedServiceDatetime equals to UPDATED_PLANED_SERVICE_DATETIME
        defaultLocationResponseShouldNotBeFound("planedServiceDatetime.equals=" + UPDATED_PLANED_SERVICE_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByPlanedServiceDatetimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where planedServiceDatetime not equals to DEFAULT_PLANED_SERVICE_DATETIME
        defaultLocationResponseShouldNotBeFound("planedServiceDatetime.notEquals=" + DEFAULT_PLANED_SERVICE_DATETIME);

        // Get all the locationResponseList where planedServiceDatetime not equals to UPDATED_PLANED_SERVICE_DATETIME
        defaultLocationResponseShouldBeFound("planedServiceDatetime.notEquals=" + UPDATED_PLANED_SERVICE_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByPlanedServiceDatetimeIsInShouldWork() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where planedServiceDatetime in DEFAULT_PLANED_SERVICE_DATETIME or UPDATED_PLANED_SERVICE_DATETIME
        defaultLocationResponseShouldBeFound(
            "planedServiceDatetime.in=" + DEFAULT_PLANED_SERVICE_DATETIME + "," + UPDATED_PLANED_SERVICE_DATETIME
        );

        // Get all the locationResponseList where planedServiceDatetime equals to UPDATED_PLANED_SERVICE_DATETIME
        defaultLocationResponseShouldNotBeFound("planedServiceDatetime.in=" + UPDATED_PLANED_SERVICE_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByPlanedServiceDatetimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where planedServiceDatetime is not null
        defaultLocationResponseShouldBeFound("planedServiceDatetime.specified=true");

        // Get all the locationResponseList where planedServiceDatetime is null
        defaultLocationResponseShouldNotBeFound("planedServiceDatetime.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationResponsesByPlanedServiceDatetimeContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where planedServiceDatetime contains DEFAULT_PLANED_SERVICE_DATETIME
        defaultLocationResponseShouldBeFound("planedServiceDatetime.contains=" + DEFAULT_PLANED_SERVICE_DATETIME);

        // Get all the locationResponseList where planedServiceDatetime contains UPDATED_PLANED_SERVICE_DATETIME
        defaultLocationResponseShouldNotBeFound("planedServiceDatetime.contains=" + UPDATED_PLANED_SERVICE_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByPlanedServiceDatetimeNotContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where planedServiceDatetime does not contain DEFAULT_PLANED_SERVICE_DATETIME
        defaultLocationResponseShouldNotBeFound("planedServiceDatetime.doesNotContain=" + DEFAULT_PLANED_SERVICE_DATETIME);

        // Get all the locationResponseList where planedServiceDatetime does not contain UPDATED_PLANED_SERVICE_DATETIME
        defaultLocationResponseShouldBeFound("planedServiceDatetime.doesNotContain=" + UPDATED_PLANED_SERVICE_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByTankOwnerIsEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where tankOwner equals to DEFAULT_TANK_OWNER
        defaultLocationResponseShouldBeFound("tankOwner.equals=" + DEFAULT_TANK_OWNER);

        // Get all the locationResponseList where tankOwner equals to UPDATED_TANK_OWNER
        defaultLocationResponseShouldNotBeFound("tankOwner.equals=" + UPDATED_TANK_OWNER);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByTankOwnerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where tankOwner not equals to DEFAULT_TANK_OWNER
        defaultLocationResponseShouldNotBeFound("tankOwner.notEquals=" + DEFAULT_TANK_OWNER);

        // Get all the locationResponseList where tankOwner not equals to UPDATED_TANK_OWNER
        defaultLocationResponseShouldBeFound("tankOwner.notEquals=" + UPDATED_TANK_OWNER);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByTankOwnerIsInShouldWork() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where tankOwner in DEFAULT_TANK_OWNER or UPDATED_TANK_OWNER
        defaultLocationResponseShouldBeFound("tankOwner.in=" + DEFAULT_TANK_OWNER + "," + UPDATED_TANK_OWNER);

        // Get all the locationResponseList where tankOwner equals to UPDATED_TANK_OWNER
        defaultLocationResponseShouldNotBeFound("tankOwner.in=" + UPDATED_TANK_OWNER);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByTankOwnerIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where tankOwner is not null
        defaultLocationResponseShouldBeFound("tankOwner.specified=true");

        // Get all the locationResponseList where tankOwner is null
        defaultLocationResponseShouldNotBeFound("tankOwner.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationResponsesByTankOwnerContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where tankOwner contains DEFAULT_TANK_OWNER
        defaultLocationResponseShouldBeFound("tankOwner.contains=" + DEFAULT_TANK_OWNER);

        // Get all the locationResponseList where tankOwner contains UPDATED_TANK_OWNER
        defaultLocationResponseShouldNotBeFound("tankOwner.contains=" + UPDATED_TANK_OWNER);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByTankOwnerNotContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where tankOwner does not contain DEFAULT_TANK_OWNER
        defaultLocationResponseShouldNotBeFound("tankOwner.doesNotContain=" + DEFAULT_TANK_OWNER);

        // Get all the locationResponseList where tankOwner does not contain UPDATED_TANK_OWNER
        defaultLocationResponseShouldBeFound("tankOwner.doesNotContain=" + UPDATED_TANK_OWNER);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByTankModelIsEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where tankModel equals to DEFAULT_TANK_MODEL
        defaultLocationResponseShouldBeFound("tankModel.equals=" + DEFAULT_TANK_MODEL);

        // Get all the locationResponseList where tankModel equals to UPDATED_TANK_MODEL
        defaultLocationResponseShouldNotBeFound("tankModel.equals=" + UPDATED_TANK_MODEL);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByTankModelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where tankModel not equals to DEFAULT_TANK_MODEL
        defaultLocationResponseShouldNotBeFound("tankModel.notEquals=" + DEFAULT_TANK_MODEL);

        // Get all the locationResponseList where tankModel not equals to UPDATED_TANK_MODEL
        defaultLocationResponseShouldBeFound("tankModel.notEquals=" + UPDATED_TANK_MODEL);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByTankModelIsInShouldWork() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where tankModel in DEFAULT_TANK_MODEL or UPDATED_TANK_MODEL
        defaultLocationResponseShouldBeFound("tankModel.in=" + DEFAULT_TANK_MODEL + "," + UPDATED_TANK_MODEL);

        // Get all the locationResponseList where tankModel equals to UPDATED_TANK_MODEL
        defaultLocationResponseShouldNotBeFound("tankModel.in=" + UPDATED_TANK_MODEL);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByTankModelIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where tankModel is not null
        defaultLocationResponseShouldBeFound("tankModel.specified=true");

        // Get all the locationResponseList where tankModel is null
        defaultLocationResponseShouldNotBeFound("tankModel.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationResponsesByTankModelContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where tankModel contains DEFAULT_TANK_MODEL
        defaultLocationResponseShouldBeFound("tankModel.contains=" + DEFAULT_TANK_MODEL);

        // Get all the locationResponseList where tankModel contains UPDATED_TANK_MODEL
        defaultLocationResponseShouldNotBeFound("tankModel.contains=" + UPDATED_TANK_MODEL);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByTankModelNotContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where tankModel does not contain DEFAULT_TANK_MODEL
        defaultLocationResponseShouldNotBeFound("tankModel.doesNotContain=" + DEFAULT_TANK_MODEL);

        // Get all the locationResponseList where tankModel does not contain UPDATED_TANK_MODEL
        defaultLocationResponseShouldBeFound("tankModel.doesNotContain=" + UPDATED_TANK_MODEL);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByDefectRegionIsEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where defectRegion equals to DEFAULT_DEFECT_REGION
        defaultLocationResponseShouldBeFound("defectRegion.equals=" + DEFAULT_DEFECT_REGION);

        // Get all the locationResponseList where defectRegion equals to UPDATED_DEFECT_REGION
        defaultLocationResponseShouldNotBeFound("defectRegion.equals=" + UPDATED_DEFECT_REGION);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByDefectRegionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where defectRegion not equals to DEFAULT_DEFECT_REGION
        defaultLocationResponseShouldNotBeFound("defectRegion.notEquals=" + DEFAULT_DEFECT_REGION);

        // Get all the locationResponseList where defectRegion not equals to UPDATED_DEFECT_REGION
        defaultLocationResponseShouldBeFound("defectRegion.notEquals=" + UPDATED_DEFECT_REGION);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByDefectRegionIsInShouldWork() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where defectRegion in DEFAULT_DEFECT_REGION or UPDATED_DEFECT_REGION
        defaultLocationResponseShouldBeFound("defectRegion.in=" + DEFAULT_DEFECT_REGION + "," + UPDATED_DEFECT_REGION);

        // Get all the locationResponseList where defectRegion equals to UPDATED_DEFECT_REGION
        defaultLocationResponseShouldNotBeFound("defectRegion.in=" + UPDATED_DEFECT_REGION);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByDefectRegionIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where defectRegion is not null
        defaultLocationResponseShouldBeFound("defectRegion.specified=true");

        // Get all the locationResponseList where defectRegion is null
        defaultLocationResponseShouldNotBeFound("defectRegion.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationResponsesByDefectRegionContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where defectRegion contains DEFAULT_DEFECT_REGION
        defaultLocationResponseShouldBeFound("defectRegion.contains=" + DEFAULT_DEFECT_REGION);

        // Get all the locationResponseList where defectRegion contains UPDATED_DEFECT_REGION
        defaultLocationResponseShouldNotBeFound("defectRegion.contains=" + UPDATED_DEFECT_REGION);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByDefectRegionNotContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where defectRegion does not contain DEFAULT_DEFECT_REGION
        defaultLocationResponseShouldNotBeFound("defectRegion.doesNotContain=" + DEFAULT_DEFECT_REGION);

        // Get all the locationResponseList where defectRegion does not contain UPDATED_DEFECT_REGION
        defaultLocationResponseShouldBeFound("defectRegion.doesNotContain=" + UPDATED_DEFECT_REGION);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByDefectStationIsEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where defectStation equals to DEFAULT_DEFECT_STATION
        defaultLocationResponseShouldBeFound("defectStation.equals=" + DEFAULT_DEFECT_STATION);

        // Get all the locationResponseList where defectStation equals to UPDATED_DEFECT_STATION
        defaultLocationResponseShouldNotBeFound("defectStation.equals=" + UPDATED_DEFECT_STATION);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByDefectStationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where defectStation not equals to DEFAULT_DEFECT_STATION
        defaultLocationResponseShouldNotBeFound("defectStation.notEquals=" + DEFAULT_DEFECT_STATION);

        // Get all the locationResponseList where defectStation not equals to UPDATED_DEFECT_STATION
        defaultLocationResponseShouldBeFound("defectStation.notEquals=" + UPDATED_DEFECT_STATION);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByDefectStationIsInShouldWork() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where defectStation in DEFAULT_DEFECT_STATION or UPDATED_DEFECT_STATION
        defaultLocationResponseShouldBeFound("defectStation.in=" + DEFAULT_DEFECT_STATION + "," + UPDATED_DEFECT_STATION);

        // Get all the locationResponseList where defectStation equals to UPDATED_DEFECT_STATION
        defaultLocationResponseShouldNotBeFound("defectStation.in=" + UPDATED_DEFECT_STATION);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByDefectStationIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where defectStation is not null
        defaultLocationResponseShouldBeFound("defectStation.specified=true");

        // Get all the locationResponseList where defectStation is null
        defaultLocationResponseShouldNotBeFound("defectStation.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationResponsesByDefectStationContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where defectStation contains DEFAULT_DEFECT_STATION
        defaultLocationResponseShouldBeFound("defectStation.contains=" + DEFAULT_DEFECT_STATION);

        // Get all the locationResponseList where defectStation contains UPDATED_DEFECT_STATION
        defaultLocationResponseShouldNotBeFound("defectStation.contains=" + UPDATED_DEFECT_STATION);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByDefectStationNotContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where defectStation does not contain DEFAULT_DEFECT_STATION
        defaultLocationResponseShouldNotBeFound("defectStation.doesNotContain=" + DEFAULT_DEFECT_STATION);

        // Get all the locationResponseList where defectStation does not contain UPDATED_DEFECT_STATION
        defaultLocationResponseShouldBeFound("defectStation.doesNotContain=" + UPDATED_DEFECT_STATION);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByDefectDatetimeIsEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where defectDatetime equals to DEFAULT_DEFECT_DATETIME
        defaultLocationResponseShouldBeFound("defectDatetime.equals=" + DEFAULT_DEFECT_DATETIME);

        // Get all the locationResponseList where defectDatetime equals to UPDATED_DEFECT_DATETIME
        defaultLocationResponseShouldNotBeFound("defectDatetime.equals=" + UPDATED_DEFECT_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByDefectDatetimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where defectDatetime not equals to DEFAULT_DEFECT_DATETIME
        defaultLocationResponseShouldNotBeFound("defectDatetime.notEquals=" + DEFAULT_DEFECT_DATETIME);

        // Get all the locationResponseList where defectDatetime not equals to UPDATED_DEFECT_DATETIME
        defaultLocationResponseShouldBeFound("defectDatetime.notEquals=" + UPDATED_DEFECT_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByDefectDatetimeIsInShouldWork() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where defectDatetime in DEFAULT_DEFECT_DATETIME or UPDATED_DEFECT_DATETIME
        defaultLocationResponseShouldBeFound("defectDatetime.in=" + DEFAULT_DEFECT_DATETIME + "," + UPDATED_DEFECT_DATETIME);

        // Get all the locationResponseList where defectDatetime equals to UPDATED_DEFECT_DATETIME
        defaultLocationResponseShouldNotBeFound("defectDatetime.in=" + UPDATED_DEFECT_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByDefectDatetimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where defectDatetime is not null
        defaultLocationResponseShouldBeFound("defectDatetime.specified=true");

        // Get all the locationResponseList where defectDatetime is null
        defaultLocationResponseShouldNotBeFound("defectDatetime.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationResponsesByDefectDatetimeContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where defectDatetime contains DEFAULT_DEFECT_DATETIME
        defaultLocationResponseShouldBeFound("defectDatetime.contains=" + DEFAULT_DEFECT_DATETIME);

        // Get all the locationResponseList where defectDatetime contains UPDATED_DEFECT_DATETIME
        defaultLocationResponseShouldNotBeFound("defectDatetime.contains=" + UPDATED_DEFECT_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByDefectDatetimeNotContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where defectDatetime does not contain DEFAULT_DEFECT_DATETIME
        defaultLocationResponseShouldNotBeFound("defectDatetime.doesNotContain=" + DEFAULT_DEFECT_DATETIME);

        // Get all the locationResponseList where defectDatetime does not contain UPDATED_DEFECT_DATETIME
        defaultLocationResponseShouldBeFound("defectDatetime.doesNotContain=" + UPDATED_DEFECT_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByDefectDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where defectDetails equals to DEFAULT_DEFECT_DETAILS
        defaultLocationResponseShouldBeFound("defectDetails.equals=" + DEFAULT_DEFECT_DETAILS);

        // Get all the locationResponseList where defectDetails equals to UPDATED_DEFECT_DETAILS
        defaultLocationResponseShouldNotBeFound("defectDetails.equals=" + UPDATED_DEFECT_DETAILS);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByDefectDetailsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where defectDetails not equals to DEFAULT_DEFECT_DETAILS
        defaultLocationResponseShouldNotBeFound("defectDetails.notEquals=" + DEFAULT_DEFECT_DETAILS);

        // Get all the locationResponseList where defectDetails not equals to UPDATED_DEFECT_DETAILS
        defaultLocationResponseShouldBeFound("defectDetails.notEquals=" + UPDATED_DEFECT_DETAILS);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByDefectDetailsIsInShouldWork() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where defectDetails in DEFAULT_DEFECT_DETAILS or UPDATED_DEFECT_DETAILS
        defaultLocationResponseShouldBeFound("defectDetails.in=" + DEFAULT_DEFECT_DETAILS + "," + UPDATED_DEFECT_DETAILS);

        // Get all the locationResponseList where defectDetails equals to UPDATED_DEFECT_DETAILS
        defaultLocationResponseShouldNotBeFound("defectDetails.in=" + UPDATED_DEFECT_DETAILS);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByDefectDetailsIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where defectDetails is not null
        defaultLocationResponseShouldBeFound("defectDetails.specified=true");

        // Get all the locationResponseList where defectDetails is null
        defaultLocationResponseShouldNotBeFound("defectDetails.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationResponsesByDefectDetailsContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where defectDetails contains DEFAULT_DEFECT_DETAILS
        defaultLocationResponseShouldBeFound("defectDetails.contains=" + DEFAULT_DEFECT_DETAILS);

        // Get all the locationResponseList where defectDetails contains UPDATED_DEFECT_DETAILS
        defaultLocationResponseShouldNotBeFound("defectDetails.contains=" + UPDATED_DEFECT_DETAILS);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByDefectDetailsNotContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where defectDetails does not contain DEFAULT_DEFECT_DETAILS
        defaultLocationResponseShouldNotBeFound("defectDetails.doesNotContain=" + DEFAULT_DEFECT_DETAILS);

        // Get all the locationResponseList where defectDetails does not contain UPDATED_DEFECT_DETAILS
        defaultLocationResponseShouldBeFound("defectDetails.doesNotContain=" + UPDATED_DEFECT_DETAILS);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByRepairRegionIsEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where repairRegion equals to DEFAULT_REPAIR_REGION
        defaultLocationResponseShouldBeFound("repairRegion.equals=" + DEFAULT_REPAIR_REGION);

        // Get all the locationResponseList where repairRegion equals to UPDATED_REPAIR_REGION
        defaultLocationResponseShouldNotBeFound("repairRegion.equals=" + UPDATED_REPAIR_REGION);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByRepairRegionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where repairRegion not equals to DEFAULT_REPAIR_REGION
        defaultLocationResponseShouldNotBeFound("repairRegion.notEquals=" + DEFAULT_REPAIR_REGION);

        // Get all the locationResponseList where repairRegion not equals to UPDATED_REPAIR_REGION
        defaultLocationResponseShouldBeFound("repairRegion.notEquals=" + UPDATED_REPAIR_REGION);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByRepairRegionIsInShouldWork() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where repairRegion in DEFAULT_REPAIR_REGION or UPDATED_REPAIR_REGION
        defaultLocationResponseShouldBeFound("repairRegion.in=" + DEFAULT_REPAIR_REGION + "," + UPDATED_REPAIR_REGION);

        // Get all the locationResponseList where repairRegion equals to UPDATED_REPAIR_REGION
        defaultLocationResponseShouldNotBeFound("repairRegion.in=" + UPDATED_REPAIR_REGION);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByRepairRegionIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where repairRegion is not null
        defaultLocationResponseShouldBeFound("repairRegion.specified=true");

        // Get all the locationResponseList where repairRegion is null
        defaultLocationResponseShouldNotBeFound("repairRegion.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationResponsesByRepairRegionContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where repairRegion contains DEFAULT_REPAIR_REGION
        defaultLocationResponseShouldBeFound("repairRegion.contains=" + DEFAULT_REPAIR_REGION);

        // Get all the locationResponseList where repairRegion contains UPDATED_REPAIR_REGION
        defaultLocationResponseShouldNotBeFound("repairRegion.contains=" + UPDATED_REPAIR_REGION);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByRepairRegionNotContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where repairRegion does not contain DEFAULT_REPAIR_REGION
        defaultLocationResponseShouldNotBeFound("repairRegion.doesNotContain=" + DEFAULT_REPAIR_REGION);

        // Get all the locationResponseList where repairRegion does not contain UPDATED_REPAIR_REGION
        defaultLocationResponseShouldBeFound("repairRegion.doesNotContain=" + UPDATED_REPAIR_REGION);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByRepairStationIsEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where repairStation equals to DEFAULT_REPAIR_STATION
        defaultLocationResponseShouldBeFound("repairStation.equals=" + DEFAULT_REPAIR_STATION);

        // Get all the locationResponseList where repairStation equals to UPDATED_REPAIR_STATION
        defaultLocationResponseShouldNotBeFound("repairStation.equals=" + UPDATED_REPAIR_STATION);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByRepairStationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where repairStation not equals to DEFAULT_REPAIR_STATION
        defaultLocationResponseShouldNotBeFound("repairStation.notEquals=" + DEFAULT_REPAIR_STATION);

        // Get all the locationResponseList where repairStation not equals to UPDATED_REPAIR_STATION
        defaultLocationResponseShouldBeFound("repairStation.notEquals=" + UPDATED_REPAIR_STATION);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByRepairStationIsInShouldWork() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where repairStation in DEFAULT_REPAIR_STATION or UPDATED_REPAIR_STATION
        defaultLocationResponseShouldBeFound("repairStation.in=" + DEFAULT_REPAIR_STATION + "," + UPDATED_REPAIR_STATION);

        // Get all the locationResponseList where repairStation equals to UPDATED_REPAIR_STATION
        defaultLocationResponseShouldNotBeFound("repairStation.in=" + UPDATED_REPAIR_STATION);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByRepairStationIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where repairStation is not null
        defaultLocationResponseShouldBeFound("repairStation.specified=true");

        // Get all the locationResponseList where repairStation is null
        defaultLocationResponseShouldNotBeFound("repairStation.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationResponsesByRepairStationContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where repairStation contains DEFAULT_REPAIR_STATION
        defaultLocationResponseShouldBeFound("repairStation.contains=" + DEFAULT_REPAIR_STATION);

        // Get all the locationResponseList where repairStation contains UPDATED_REPAIR_STATION
        defaultLocationResponseShouldNotBeFound("repairStation.contains=" + UPDATED_REPAIR_STATION);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByRepairStationNotContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where repairStation does not contain DEFAULT_REPAIR_STATION
        defaultLocationResponseShouldNotBeFound("repairStation.doesNotContain=" + DEFAULT_REPAIR_STATION);

        // Get all the locationResponseList where repairStation does not contain UPDATED_REPAIR_STATION
        defaultLocationResponseShouldBeFound("repairStation.doesNotContain=" + UPDATED_REPAIR_STATION);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByRepairDatetimeIsEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where repairDatetime equals to DEFAULT_REPAIR_DATETIME
        defaultLocationResponseShouldBeFound("repairDatetime.equals=" + DEFAULT_REPAIR_DATETIME);

        // Get all the locationResponseList where repairDatetime equals to UPDATED_REPAIR_DATETIME
        defaultLocationResponseShouldNotBeFound("repairDatetime.equals=" + UPDATED_REPAIR_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByRepairDatetimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where repairDatetime not equals to DEFAULT_REPAIR_DATETIME
        defaultLocationResponseShouldNotBeFound("repairDatetime.notEquals=" + DEFAULT_REPAIR_DATETIME);

        // Get all the locationResponseList where repairDatetime not equals to UPDATED_REPAIR_DATETIME
        defaultLocationResponseShouldBeFound("repairDatetime.notEquals=" + UPDATED_REPAIR_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByRepairDatetimeIsInShouldWork() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where repairDatetime in DEFAULT_REPAIR_DATETIME or UPDATED_REPAIR_DATETIME
        defaultLocationResponseShouldBeFound("repairDatetime.in=" + DEFAULT_REPAIR_DATETIME + "," + UPDATED_REPAIR_DATETIME);

        // Get all the locationResponseList where repairDatetime equals to UPDATED_REPAIR_DATETIME
        defaultLocationResponseShouldNotBeFound("repairDatetime.in=" + UPDATED_REPAIR_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByRepairDatetimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where repairDatetime is not null
        defaultLocationResponseShouldBeFound("repairDatetime.specified=true");

        // Get all the locationResponseList where repairDatetime is null
        defaultLocationResponseShouldNotBeFound("repairDatetime.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationResponsesByRepairDatetimeContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where repairDatetime contains DEFAULT_REPAIR_DATETIME
        defaultLocationResponseShouldBeFound("repairDatetime.contains=" + DEFAULT_REPAIR_DATETIME);

        // Get all the locationResponseList where repairDatetime contains UPDATED_REPAIR_DATETIME
        defaultLocationResponseShouldNotBeFound("repairDatetime.contains=" + UPDATED_REPAIR_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByRepairDatetimeNotContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where repairDatetime does not contain DEFAULT_REPAIR_DATETIME
        defaultLocationResponseShouldNotBeFound("repairDatetime.doesNotContain=" + DEFAULT_REPAIR_DATETIME);

        // Get all the locationResponseList where repairDatetime does not contain UPDATED_REPAIR_DATETIME
        defaultLocationResponseShouldBeFound("repairDatetime.doesNotContain=" + UPDATED_REPAIR_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByUpdateDatetimeIsEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where updateDatetime equals to DEFAULT_UPDATE_DATETIME
        defaultLocationResponseShouldBeFound("updateDatetime.equals=" + DEFAULT_UPDATE_DATETIME);

        // Get all the locationResponseList where updateDatetime equals to UPDATED_UPDATE_DATETIME
        defaultLocationResponseShouldNotBeFound("updateDatetime.equals=" + UPDATED_UPDATE_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByUpdateDatetimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where updateDatetime not equals to DEFAULT_UPDATE_DATETIME
        defaultLocationResponseShouldNotBeFound("updateDatetime.notEquals=" + DEFAULT_UPDATE_DATETIME);

        // Get all the locationResponseList where updateDatetime not equals to UPDATED_UPDATE_DATETIME
        defaultLocationResponseShouldBeFound("updateDatetime.notEquals=" + UPDATED_UPDATE_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByUpdateDatetimeIsInShouldWork() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where updateDatetime in DEFAULT_UPDATE_DATETIME or UPDATED_UPDATE_DATETIME
        defaultLocationResponseShouldBeFound("updateDatetime.in=" + DEFAULT_UPDATE_DATETIME + "," + UPDATED_UPDATE_DATETIME);

        // Get all the locationResponseList where updateDatetime equals to UPDATED_UPDATE_DATETIME
        defaultLocationResponseShouldNotBeFound("updateDatetime.in=" + UPDATED_UPDATE_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByUpdateDatetimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where updateDatetime is not null
        defaultLocationResponseShouldBeFound("updateDatetime.specified=true");

        // Get all the locationResponseList where updateDatetime is null
        defaultLocationResponseShouldNotBeFound("updateDatetime.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationResponsesByUpdateDatetimeContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where updateDatetime contains DEFAULT_UPDATE_DATETIME
        defaultLocationResponseShouldBeFound("updateDatetime.contains=" + DEFAULT_UPDATE_DATETIME);

        // Get all the locationResponseList where updateDatetime contains UPDATED_UPDATE_DATETIME
        defaultLocationResponseShouldNotBeFound("updateDatetime.contains=" + UPDATED_UPDATE_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByUpdateDatetimeNotContainsSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        // Get all the locationResponseList where updateDatetime does not contain DEFAULT_UPDATE_DATETIME
        defaultLocationResponseShouldNotBeFound("updateDatetime.doesNotContain=" + DEFAULT_UPDATE_DATETIME);

        // Get all the locationResponseList where updateDatetime does not contain UPDATED_UPDATE_DATETIME
        defaultLocationResponseShouldBeFound("updateDatetime.doesNotContain=" + UPDATED_UPDATE_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationResponsesByLocationRequestIsEqualToSomething() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);
        LocationRequest locationRequest = LocationRequestResourceIT.createEntity(em);
        em.persist(locationRequest);
        em.flush();
        locationResponse.setLocationRequest(locationRequest);
        locationRequest.setLocationResponse(locationResponse);
        locationResponseRepository.saveAndFlush(locationResponse);
        Long locationRequestId = locationRequest.getId();

        // Get all the locationResponseList where locationRequest equals to locationRequestId
        defaultLocationResponseShouldBeFound("locationRequestId.equals=" + locationRequestId);

        // Get all the locationResponseList where locationRequest equals to (locationRequestId + 1)
        defaultLocationResponseShouldNotBeFound("locationRequestId.equals=" + (locationRequestId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLocationResponseShouldBeFound(String filter) throws Exception {
        restLocationResponseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(locationResponse.getId().intValue())))
            .andExpect(jsonPath("$.[*].responseDatetime").value(hasItem(sameInstant(DEFAULT_RESPONSE_DATETIME))))
            .andExpect(jsonPath("$.[*].tankNumber").value(hasItem(DEFAULT_TANK_NUMBER)))
            .andExpect(jsonPath("$.[*].tankType").value(hasItem(DEFAULT_TANK_TYPE)))
            .andExpect(jsonPath("$.[*].cargoId").value(hasItem(DEFAULT_CARGO_ID)))
            .andExpect(jsonPath("$.[*].cargoName").value(hasItem(DEFAULT_CARGO_NAME)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)))
            .andExpect(jsonPath("$.[*].receiverId").value(hasItem(DEFAULT_RECEIVER_ID)))
            .andExpect(jsonPath("$.[*].tankIndex").value(hasItem(DEFAULT_TANK_INDEX)))
            .andExpect(jsonPath("$.[*].locationStationId").value(hasItem(DEFAULT_LOCATION_STATION_ID)))
            .andExpect(jsonPath("$.[*].locationStationName").value(hasItem(DEFAULT_LOCATION_STATION_NAME)))
            .andExpect(jsonPath("$.[*].locationDatetime").value(hasItem(DEFAULT_LOCATION_DATETIME)))
            .andExpect(jsonPath("$.[*].locationOperation").value(hasItem(DEFAULT_LOCATION_OPERATION)))
            .andExpect(jsonPath("$.[*].stateFromStationId").value(hasItem(DEFAULT_STATE_FROM_STATION_ID)))
            .andExpect(jsonPath("$.[*].stateFromStationName").value(hasItem(DEFAULT_STATE_FROM_STATION_NAME)))
            .andExpect(jsonPath("$.[*].stateToStationId").value(hasItem(DEFAULT_STATE_TO_STATION_ID)))
            .andExpect(jsonPath("$.[*].stateToStationName").value(hasItem(DEFAULT_STATE_TO_STATION_NAME)))
            .andExpect(jsonPath("$.[*].stateSendDatetime").value(hasItem(DEFAULT_STATE_SEND_DATETIME)))
            .andExpect(jsonPath("$.[*].stateSenderId").value(hasItem(DEFAULT_STATE_SENDER_ID)))
            .andExpect(jsonPath("$.[*].planedServiceDatetime").value(hasItem(DEFAULT_PLANED_SERVICE_DATETIME)))
            .andExpect(jsonPath("$.[*].tankOwner").value(hasItem(DEFAULT_TANK_OWNER)))
            .andExpect(jsonPath("$.[*].tankModel").value(hasItem(DEFAULT_TANK_MODEL)))
            .andExpect(jsonPath("$.[*].defectRegion").value(hasItem(DEFAULT_DEFECT_REGION)))
            .andExpect(jsonPath("$.[*].defectStation").value(hasItem(DEFAULT_DEFECT_STATION)))
            .andExpect(jsonPath("$.[*].defectDatetime").value(hasItem(DEFAULT_DEFECT_DATETIME)))
            .andExpect(jsonPath("$.[*].defectDetails").value(hasItem(DEFAULT_DEFECT_DETAILS)))
            .andExpect(jsonPath("$.[*].repairRegion").value(hasItem(DEFAULT_REPAIR_REGION)))
            .andExpect(jsonPath("$.[*].repairStation").value(hasItem(DEFAULT_REPAIR_STATION)))
            .andExpect(jsonPath("$.[*].repairDatetime").value(hasItem(DEFAULT_REPAIR_DATETIME)))
            .andExpect(jsonPath("$.[*].updateDatetime").value(hasItem(DEFAULT_UPDATE_DATETIME)));

        // Check, that the count call also returns 1
        restLocationResponseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLocationResponseShouldNotBeFound(String filter) throws Exception {
        restLocationResponseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLocationResponseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLocationResponse() throws Exception {
        // Get the locationResponse
        restLocationResponseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLocationResponse() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        int databaseSizeBeforeUpdate = locationResponseRepository.findAll().size();

        // Update the locationResponse
        LocationResponse updatedLocationResponse = locationResponseRepository.findById(locationResponse.getId()).get();
        // Disconnect from session so that the updates on updatedLocationResponse are not directly saved in db
        em.detach(updatedLocationResponse);
        updatedLocationResponse
            .responseDatetime(UPDATED_RESPONSE_DATETIME)
            .tankNumber(UPDATED_TANK_NUMBER)
            .tankType(UPDATED_TANK_TYPE)
            .cargoId(UPDATED_CARGO_ID)
            .cargoName(UPDATED_CARGO_NAME)
            .weight(UPDATED_WEIGHT)
            .receiverId(UPDATED_RECEIVER_ID)
            .tankIndex(UPDATED_TANK_INDEX)
            .locationStationId(UPDATED_LOCATION_STATION_ID)
            .locationStationName(UPDATED_LOCATION_STATION_NAME)
            .locationDatetime(UPDATED_LOCATION_DATETIME)
            .locationOperation(UPDATED_LOCATION_OPERATION)
            .stateFromStationId(UPDATED_STATE_FROM_STATION_ID)
            .stateFromStationName(UPDATED_STATE_FROM_STATION_NAME)
            .stateToStationId(UPDATED_STATE_TO_STATION_ID)
            .stateToStationName(UPDATED_STATE_TO_STATION_NAME)
            .stateSendDatetime(UPDATED_STATE_SEND_DATETIME)
            .stateSenderId(UPDATED_STATE_SENDER_ID)
            .planedServiceDatetime(UPDATED_PLANED_SERVICE_DATETIME)
            .tankOwner(UPDATED_TANK_OWNER)
            .tankModel(UPDATED_TANK_MODEL)
            .defectRegion(UPDATED_DEFECT_REGION)
            .defectStation(UPDATED_DEFECT_STATION)
            .defectDatetime(UPDATED_DEFECT_DATETIME)
            .defectDetails(UPDATED_DEFECT_DETAILS)
            .repairRegion(UPDATED_REPAIR_REGION)
            .repairStation(UPDATED_REPAIR_STATION)
            .repairDatetime(UPDATED_REPAIR_DATETIME)
            .updateDatetime(UPDATED_UPDATE_DATETIME);

        restLocationResponseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLocationResponse.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLocationResponse))
            )
            .andExpect(status().isOk());

        // Validate the LocationResponse in the database
        List<LocationResponse> locationResponseList = locationResponseRepository.findAll();
        assertThat(locationResponseList).hasSize(databaseSizeBeforeUpdate);
        LocationResponse testLocationResponse = locationResponseList.get(locationResponseList.size() - 1);
        assertThat(testLocationResponse.getResponseDatetime()).isEqualTo(UPDATED_RESPONSE_DATETIME);
        assertThat(testLocationResponse.getTankNumber()).isEqualTo(UPDATED_TANK_NUMBER);
        assertThat(testLocationResponse.getTankType()).isEqualTo(UPDATED_TANK_TYPE);
        assertThat(testLocationResponse.getCargoId()).isEqualTo(UPDATED_CARGO_ID);
        assertThat(testLocationResponse.getCargoName()).isEqualTo(UPDATED_CARGO_NAME);
        assertThat(testLocationResponse.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testLocationResponse.getReceiverId()).isEqualTo(UPDATED_RECEIVER_ID);
        assertThat(testLocationResponse.getTankIndex()).isEqualTo(UPDATED_TANK_INDEX);
        assertThat(testLocationResponse.getLocationStationId()).isEqualTo(UPDATED_LOCATION_STATION_ID);
        assertThat(testLocationResponse.getLocationStationName()).isEqualTo(UPDATED_LOCATION_STATION_NAME);
        assertThat(testLocationResponse.getLocationDatetime()).isEqualTo(UPDATED_LOCATION_DATETIME);
        assertThat(testLocationResponse.getLocationOperation()).isEqualTo(UPDATED_LOCATION_OPERATION);
        assertThat(testLocationResponse.getStateFromStationId()).isEqualTo(UPDATED_STATE_FROM_STATION_ID);
        assertThat(testLocationResponse.getStateFromStationName()).isEqualTo(UPDATED_STATE_FROM_STATION_NAME);
        assertThat(testLocationResponse.getStateToStationId()).isEqualTo(UPDATED_STATE_TO_STATION_ID);
        assertThat(testLocationResponse.getStateToStationName()).isEqualTo(UPDATED_STATE_TO_STATION_NAME);
        assertThat(testLocationResponse.getStateSendDatetime()).isEqualTo(UPDATED_STATE_SEND_DATETIME);
        assertThat(testLocationResponse.getStateSenderId()).isEqualTo(UPDATED_STATE_SENDER_ID);
        assertThat(testLocationResponse.getPlanedServiceDatetime()).isEqualTo(UPDATED_PLANED_SERVICE_DATETIME);
        assertThat(testLocationResponse.getTankOwner()).isEqualTo(UPDATED_TANK_OWNER);
        assertThat(testLocationResponse.getTankModel()).isEqualTo(UPDATED_TANK_MODEL);
        assertThat(testLocationResponse.getDefectRegion()).isEqualTo(UPDATED_DEFECT_REGION);
        assertThat(testLocationResponse.getDefectStation()).isEqualTo(UPDATED_DEFECT_STATION);
        assertThat(testLocationResponse.getDefectDatetime()).isEqualTo(UPDATED_DEFECT_DATETIME);
        assertThat(testLocationResponse.getDefectDetails()).isEqualTo(UPDATED_DEFECT_DETAILS);
        assertThat(testLocationResponse.getRepairRegion()).isEqualTo(UPDATED_REPAIR_REGION);
        assertThat(testLocationResponse.getRepairStation()).isEqualTo(UPDATED_REPAIR_STATION);
        assertThat(testLocationResponse.getRepairDatetime()).isEqualTo(UPDATED_REPAIR_DATETIME);
        assertThat(testLocationResponse.getUpdateDatetime()).isEqualTo(UPDATED_UPDATE_DATETIME);
    }

    @Test
    @Transactional
    void putNonExistingLocationResponse() throws Exception {
        int databaseSizeBeforeUpdate = locationResponseRepository.findAll().size();
        locationResponse.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationResponseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, locationResponse.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationResponse))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationResponse in the database
        List<LocationResponse> locationResponseList = locationResponseRepository.findAll();
        assertThat(locationResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLocationResponse() throws Exception {
        int databaseSizeBeforeUpdate = locationResponseRepository.findAll().size();
        locationResponse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationResponseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationResponse))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationResponse in the database
        List<LocationResponse> locationResponseList = locationResponseRepository.findAll();
        assertThat(locationResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLocationResponse() throws Exception {
        int databaseSizeBeforeUpdate = locationResponseRepository.findAll().size();
        locationResponse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationResponseMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationResponse))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LocationResponse in the database
        List<LocationResponse> locationResponseList = locationResponseRepository.findAll();
        assertThat(locationResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLocationResponseWithPatch() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        int databaseSizeBeforeUpdate = locationResponseRepository.findAll().size();

        // Update the locationResponse using partial update
        LocationResponse partialUpdatedLocationResponse = new LocationResponse();
        partialUpdatedLocationResponse.setId(locationResponse.getId());

        partialUpdatedLocationResponse
            .tankType(UPDATED_TANK_TYPE)
            .cargoId(UPDATED_CARGO_ID)
            .tankIndex(UPDATED_TANK_INDEX)
            .stateFromStationName(UPDATED_STATE_FROM_STATION_NAME)
            .planedServiceDatetime(UPDATED_PLANED_SERVICE_DATETIME)
            .tankOwner(UPDATED_TANK_OWNER)
            .defectRegion(UPDATED_DEFECT_REGION)
            .defectDatetime(UPDATED_DEFECT_DATETIME)
            .defectDetails(UPDATED_DEFECT_DETAILS)
            .repairRegion(UPDATED_REPAIR_REGION)
            .updateDatetime(UPDATED_UPDATE_DATETIME);

        restLocationResponseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocationResponse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocationResponse))
            )
            .andExpect(status().isOk());

        // Validate the LocationResponse in the database
        List<LocationResponse> locationResponseList = locationResponseRepository.findAll();
        assertThat(locationResponseList).hasSize(databaseSizeBeforeUpdate);
        LocationResponse testLocationResponse = locationResponseList.get(locationResponseList.size() - 1);
        assertThat(testLocationResponse.getResponseDatetime()).isEqualTo(DEFAULT_RESPONSE_DATETIME);
        assertThat(testLocationResponse.getTankNumber()).isEqualTo(DEFAULT_TANK_NUMBER);
        assertThat(testLocationResponse.getTankType()).isEqualTo(UPDATED_TANK_TYPE);
        assertThat(testLocationResponse.getCargoId()).isEqualTo(UPDATED_CARGO_ID);
        assertThat(testLocationResponse.getCargoName()).isEqualTo(DEFAULT_CARGO_NAME);
        assertThat(testLocationResponse.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testLocationResponse.getReceiverId()).isEqualTo(DEFAULT_RECEIVER_ID);
        assertThat(testLocationResponse.getTankIndex()).isEqualTo(UPDATED_TANK_INDEX);
        assertThat(testLocationResponse.getLocationStationId()).isEqualTo(DEFAULT_LOCATION_STATION_ID);
        assertThat(testLocationResponse.getLocationStationName()).isEqualTo(DEFAULT_LOCATION_STATION_NAME);
        assertThat(testLocationResponse.getLocationDatetime()).isEqualTo(DEFAULT_LOCATION_DATETIME);
        assertThat(testLocationResponse.getLocationOperation()).isEqualTo(DEFAULT_LOCATION_OPERATION);
        assertThat(testLocationResponse.getStateFromStationId()).isEqualTo(DEFAULT_STATE_FROM_STATION_ID);
        assertThat(testLocationResponse.getStateFromStationName()).isEqualTo(UPDATED_STATE_FROM_STATION_NAME);
        assertThat(testLocationResponse.getStateToStationId()).isEqualTo(DEFAULT_STATE_TO_STATION_ID);
        assertThat(testLocationResponse.getStateToStationName()).isEqualTo(DEFAULT_STATE_TO_STATION_NAME);
        assertThat(testLocationResponse.getStateSendDatetime()).isEqualTo(DEFAULT_STATE_SEND_DATETIME);
        assertThat(testLocationResponse.getStateSenderId()).isEqualTo(DEFAULT_STATE_SENDER_ID);
        assertThat(testLocationResponse.getPlanedServiceDatetime()).isEqualTo(UPDATED_PLANED_SERVICE_DATETIME);
        assertThat(testLocationResponse.getTankOwner()).isEqualTo(UPDATED_TANK_OWNER);
        assertThat(testLocationResponse.getTankModel()).isEqualTo(DEFAULT_TANK_MODEL);
        assertThat(testLocationResponse.getDefectRegion()).isEqualTo(UPDATED_DEFECT_REGION);
        assertThat(testLocationResponse.getDefectStation()).isEqualTo(DEFAULT_DEFECT_STATION);
        assertThat(testLocationResponse.getDefectDatetime()).isEqualTo(UPDATED_DEFECT_DATETIME);
        assertThat(testLocationResponse.getDefectDetails()).isEqualTo(UPDATED_DEFECT_DETAILS);
        assertThat(testLocationResponse.getRepairRegion()).isEqualTo(UPDATED_REPAIR_REGION);
        assertThat(testLocationResponse.getRepairStation()).isEqualTo(DEFAULT_REPAIR_STATION);
        assertThat(testLocationResponse.getRepairDatetime()).isEqualTo(DEFAULT_REPAIR_DATETIME);
        assertThat(testLocationResponse.getUpdateDatetime()).isEqualTo(UPDATED_UPDATE_DATETIME);
    }

    @Test
    @Transactional
    void fullUpdateLocationResponseWithPatch() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        int databaseSizeBeforeUpdate = locationResponseRepository.findAll().size();

        // Update the locationResponse using partial update
        LocationResponse partialUpdatedLocationResponse = new LocationResponse();
        partialUpdatedLocationResponse.setId(locationResponse.getId());

        partialUpdatedLocationResponse
            .responseDatetime(UPDATED_RESPONSE_DATETIME)
            .tankNumber(UPDATED_TANK_NUMBER)
            .tankType(UPDATED_TANK_TYPE)
            .cargoId(UPDATED_CARGO_ID)
            .cargoName(UPDATED_CARGO_NAME)
            .weight(UPDATED_WEIGHT)
            .receiverId(UPDATED_RECEIVER_ID)
            .tankIndex(UPDATED_TANK_INDEX)
            .locationStationId(UPDATED_LOCATION_STATION_ID)
            .locationStationName(UPDATED_LOCATION_STATION_NAME)
            .locationDatetime(UPDATED_LOCATION_DATETIME)
            .locationOperation(UPDATED_LOCATION_OPERATION)
            .stateFromStationId(UPDATED_STATE_FROM_STATION_ID)
            .stateFromStationName(UPDATED_STATE_FROM_STATION_NAME)
            .stateToStationId(UPDATED_STATE_TO_STATION_ID)
            .stateToStationName(UPDATED_STATE_TO_STATION_NAME)
            .stateSendDatetime(UPDATED_STATE_SEND_DATETIME)
            .stateSenderId(UPDATED_STATE_SENDER_ID)
            .planedServiceDatetime(UPDATED_PLANED_SERVICE_DATETIME)
            .tankOwner(UPDATED_TANK_OWNER)
            .tankModel(UPDATED_TANK_MODEL)
            .defectRegion(UPDATED_DEFECT_REGION)
            .defectStation(UPDATED_DEFECT_STATION)
            .defectDatetime(UPDATED_DEFECT_DATETIME)
            .defectDetails(UPDATED_DEFECT_DETAILS)
            .repairRegion(UPDATED_REPAIR_REGION)
            .repairStation(UPDATED_REPAIR_STATION)
            .repairDatetime(UPDATED_REPAIR_DATETIME)
            .updateDatetime(UPDATED_UPDATE_DATETIME);

        restLocationResponseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocationResponse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocationResponse))
            )
            .andExpect(status().isOk());

        // Validate the LocationResponse in the database
        List<LocationResponse> locationResponseList = locationResponseRepository.findAll();
        assertThat(locationResponseList).hasSize(databaseSizeBeforeUpdate);
        LocationResponse testLocationResponse = locationResponseList.get(locationResponseList.size() - 1);
        assertThat(testLocationResponse.getResponseDatetime()).isEqualTo(UPDATED_RESPONSE_DATETIME);
        assertThat(testLocationResponse.getTankNumber()).isEqualTo(UPDATED_TANK_NUMBER);
        assertThat(testLocationResponse.getTankType()).isEqualTo(UPDATED_TANK_TYPE);
        assertThat(testLocationResponse.getCargoId()).isEqualTo(UPDATED_CARGO_ID);
        assertThat(testLocationResponse.getCargoName()).isEqualTo(UPDATED_CARGO_NAME);
        assertThat(testLocationResponse.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testLocationResponse.getReceiverId()).isEqualTo(UPDATED_RECEIVER_ID);
        assertThat(testLocationResponse.getTankIndex()).isEqualTo(UPDATED_TANK_INDEX);
        assertThat(testLocationResponse.getLocationStationId()).isEqualTo(UPDATED_LOCATION_STATION_ID);
        assertThat(testLocationResponse.getLocationStationName()).isEqualTo(UPDATED_LOCATION_STATION_NAME);
        assertThat(testLocationResponse.getLocationDatetime()).isEqualTo(UPDATED_LOCATION_DATETIME);
        assertThat(testLocationResponse.getLocationOperation()).isEqualTo(UPDATED_LOCATION_OPERATION);
        assertThat(testLocationResponse.getStateFromStationId()).isEqualTo(UPDATED_STATE_FROM_STATION_ID);
        assertThat(testLocationResponse.getStateFromStationName()).isEqualTo(UPDATED_STATE_FROM_STATION_NAME);
        assertThat(testLocationResponse.getStateToStationId()).isEqualTo(UPDATED_STATE_TO_STATION_ID);
        assertThat(testLocationResponse.getStateToStationName()).isEqualTo(UPDATED_STATE_TO_STATION_NAME);
        assertThat(testLocationResponse.getStateSendDatetime()).isEqualTo(UPDATED_STATE_SEND_DATETIME);
        assertThat(testLocationResponse.getStateSenderId()).isEqualTo(UPDATED_STATE_SENDER_ID);
        assertThat(testLocationResponse.getPlanedServiceDatetime()).isEqualTo(UPDATED_PLANED_SERVICE_DATETIME);
        assertThat(testLocationResponse.getTankOwner()).isEqualTo(UPDATED_TANK_OWNER);
        assertThat(testLocationResponse.getTankModel()).isEqualTo(UPDATED_TANK_MODEL);
        assertThat(testLocationResponse.getDefectRegion()).isEqualTo(UPDATED_DEFECT_REGION);
        assertThat(testLocationResponse.getDefectStation()).isEqualTo(UPDATED_DEFECT_STATION);
        assertThat(testLocationResponse.getDefectDatetime()).isEqualTo(UPDATED_DEFECT_DATETIME);
        assertThat(testLocationResponse.getDefectDetails()).isEqualTo(UPDATED_DEFECT_DETAILS);
        assertThat(testLocationResponse.getRepairRegion()).isEqualTo(UPDATED_REPAIR_REGION);
        assertThat(testLocationResponse.getRepairStation()).isEqualTo(UPDATED_REPAIR_STATION);
        assertThat(testLocationResponse.getRepairDatetime()).isEqualTo(UPDATED_REPAIR_DATETIME);
        assertThat(testLocationResponse.getUpdateDatetime()).isEqualTo(UPDATED_UPDATE_DATETIME);
    }

    @Test
    @Transactional
    void patchNonExistingLocationResponse() throws Exception {
        int databaseSizeBeforeUpdate = locationResponseRepository.findAll().size();
        locationResponse.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationResponseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, locationResponse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationResponse))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationResponse in the database
        List<LocationResponse> locationResponseList = locationResponseRepository.findAll();
        assertThat(locationResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLocationResponse() throws Exception {
        int databaseSizeBeforeUpdate = locationResponseRepository.findAll().size();
        locationResponse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationResponseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationResponse))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationResponse in the database
        List<LocationResponse> locationResponseList = locationResponseRepository.findAll();
        assertThat(locationResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLocationResponse() throws Exception {
        int databaseSizeBeforeUpdate = locationResponseRepository.findAll().size();
        locationResponse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationResponseMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationResponse))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LocationResponse in the database
        List<LocationResponse> locationResponseList = locationResponseRepository.findAll();
        assertThat(locationResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLocationResponse() throws Exception {
        // Initialize the database
        locationResponseRepository.saveAndFlush(locationResponse);

        int databaseSizeBeforeDelete = locationResponseRepository.findAll().size();

        // Delete the locationResponse
        restLocationResponseMockMvc
            .perform(delete(ENTITY_API_URL_ID, locationResponse.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LocationResponse> locationResponseList = locationResponseRepository.findAll();
        assertThat(locationResponseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
