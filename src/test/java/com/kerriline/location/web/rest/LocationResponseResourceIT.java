package com.kerriline.location.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.kerriline.location.IntegrationTest;
import com.kerriline.location.domain.LocationResponse;
import com.kerriline.location.repository.LocationResponseRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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

    private static final LocalDate DEFAULT_RESPONSE_DATETIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RESPONSE_DATETIME = LocalDate.now(ZoneId.systemDefault());

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
            .andExpect(jsonPath("$.[*].responseDatetime").value(hasItem(DEFAULT_RESPONSE_DATETIME.toString())))
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
            .andExpect(jsonPath("$.responseDatetime").value(DEFAULT_RESPONSE_DATETIME.toString()))
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
            .tankNumber(UPDATED_TANK_NUMBER)
            .tankType(UPDATED_TANK_TYPE)
            .receiverId(UPDATED_RECEIVER_ID)
            .tankIndex(UPDATED_TANK_INDEX)
            .stateFromStationName(UPDATED_STATE_FROM_STATION_NAME)
            .stateToStationId(UPDATED_STATE_TO_STATION_ID)
            .stateSenderId(UPDATED_STATE_SENDER_ID)
            .planedServiceDatetime(UPDATED_PLANED_SERVICE_DATETIME)
            .tankOwner(UPDATED_TANK_OWNER)
            .tankModel(UPDATED_TANK_MODEL)
            .defectDetails(UPDATED_DEFECT_DETAILS)
            .repairStation(UPDATED_REPAIR_STATION)
            .repairDatetime(UPDATED_REPAIR_DATETIME);

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
        assertThat(testLocationResponse.getTankNumber()).isEqualTo(UPDATED_TANK_NUMBER);
        assertThat(testLocationResponse.getTankType()).isEqualTo(UPDATED_TANK_TYPE);
        assertThat(testLocationResponse.getCargoId()).isEqualTo(DEFAULT_CARGO_ID);
        assertThat(testLocationResponse.getCargoName()).isEqualTo(DEFAULT_CARGO_NAME);
        assertThat(testLocationResponse.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testLocationResponse.getReceiverId()).isEqualTo(UPDATED_RECEIVER_ID);
        assertThat(testLocationResponse.getTankIndex()).isEqualTo(UPDATED_TANK_INDEX);
        assertThat(testLocationResponse.getLocationStationId()).isEqualTo(DEFAULT_LOCATION_STATION_ID);
        assertThat(testLocationResponse.getLocationStationName()).isEqualTo(DEFAULT_LOCATION_STATION_NAME);
        assertThat(testLocationResponse.getLocationDatetime()).isEqualTo(DEFAULT_LOCATION_DATETIME);
        assertThat(testLocationResponse.getLocationOperation()).isEqualTo(DEFAULT_LOCATION_OPERATION);
        assertThat(testLocationResponse.getStateFromStationId()).isEqualTo(DEFAULT_STATE_FROM_STATION_ID);
        assertThat(testLocationResponse.getStateFromStationName()).isEqualTo(UPDATED_STATE_FROM_STATION_NAME);
        assertThat(testLocationResponse.getStateToStationId()).isEqualTo(UPDATED_STATE_TO_STATION_ID);
        assertThat(testLocationResponse.getStateToStationName()).isEqualTo(DEFAULT_STATE_TO_STATION_NAME);
        assertThat(testLocationResponse.getStateSendDatetime()).isEqualTo(DEFAULT_STATE_SEND_DATETIME);
        assertThat(testLocationResponse.getStateSenderId()).isEqualTo(UPDATED_STATE_SENDER_ID);
        assertThat(testLocationResponse.getPlanedServiceDatetime()).isEqualTo(UPDATED_PLANED_SERVICE_DATETIME);
        assertThat(testLocationResponse.getTankOwner()).isEqualTo(UPDATED_TANK_OWNER);
        assertThat(testLocationResponse.getTankModel()).isEqualTo(UPDATED_TANK_MODEL);
        assertThat(testLocationResponse.getDefectRegion()).isEqualTo(DEFAULT_DEFECT_REGION);
        assertThat(testLocationResponse.getDefectStation()).isEqualTo(DEFAULT_DEFECT_STATION);
        assertThat(testLocationResponse.getDefectDatetime()).isEqualTo(DEFAULT_DEFECT_DATETIME);
        assertThat(testLocationResponse.getDefectDetails()).isEqualTo(UPDATED_DEFECT_DETAILS);
        assertThat(testLocationResponse.getRepairRegion()).isEqualTo(DEFAULT_REPAIR_REGION);
        assertThat(testLocationResponse.getRepairStation()).isEqualTo(UPDATED_REPAIR_STATION);
        assertThat(testLocationResponse.getRepairDatetime()).isEqualTo(UPDATED_REPAIR_DATETIME);
        assertThat(testLocationResponse.getUpdateDatetime()).isEqualTo(DEFAULT_UPDATE_DATETIME);
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
