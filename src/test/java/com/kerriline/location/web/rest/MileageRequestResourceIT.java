package com.kerriline.location.web.rest;

import static com.kerriline.location.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.kerriline.location.IntegrationTest;
import com.kerriline.location.domain.MileageRequest;
import com.kerriline.location.domain.MileageResponse;
import com.kerriline.location.domain.Tank;
import com.kerriline.location.repository.MileageRequestRepository;
import com.kerriline.location.service.criteria.MileageRequestCriteria;
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
 * Integration tests for the {@link MileageRequestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MileageRequestResourceIT {

    private static final ZonedDateTime DEFAULT_REQUEST_DATETIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_REQUEST_DATETIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_REQUEST_DATETIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_TANK_NUMBERS = "AAAAAAAAAA";
    private static final String UPDATED_TANK_NUMBERS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/mileage-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MileageRequestRepository mileageRequestRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMileageRequestMockMvc;

    private MileageRequest mileageRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MileageRequest createEntity(EntityManager em) {
        MileageRequest mileageRequest = new MileageRequest().requestDatetime(DEFAULT_REQUEST_DATETIME).tankNumbers(DEFAULT_TANK_NUMBERS);
        return mileageRequest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MileageRequest createUpdatedEntity(EntityManager em) {
        MileageRequest mileageRequest = new MileageRequest().requestDatetime(UPDATED_REQUEST_DATETIME).tankNumbers(UPDATED_TANK_NUMBERS);
        return mileageRequest;
    }

    @BeforeEach
    public void initTest() {
        mileageRequest = createEntity(em);
    }

    @Test
    @Transactional
    void createMileageRequest() throws Exception {
        int databaseSizeBeforeCreate = mileageRequestRepository.findAll().size();
        // Create the MileageRequest
        restMileageRequestMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mileageRequest))
            )
            .andExpect(status().isCreated());

        // Validate the MileageRequest in the database
        List<MileageRequest> mileageRequestList = mileageRequestRepository.findAll();
        assertThat(mileageRequestList).hasSize(databaseSizeBeforeCreate + 1);
        MileageRequest testMileageRequest = mileageRequestList.get(mileageRequestList.size() - 1);
        assertThat(testMileageRequest.getRequestDatetime()).isEqualTo(DEFAULT_REQUEST_DATETIME);
        assertThat(testMileageRequest.getTankNumbers()).isEqualTo(DEFAULT_TANK_NUMBERS);
    }

    @Test
    @Transactional
    void createMileageRequestWithExistingId() throws Exception {
        // Create the MileageRequest with an existing ID
        mileageRequest.setId(1L);

        int databaseSizeBeforeCreate = mileageRequestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMileageRequestMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mileageRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the MileageRequest in the database
        List<MileageRequest> mileageRequestList = mileageRequestRepository.findAll();
        assertThat(mileageRequestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMileageRequests() throws Exception {
        // Initialize the database
        mileageRequestRepository.saveAndFlush(mileageRequest);

        // Get all the mileageRequestList
        restMileageRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mileageRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestDatetime").value(hasItem(sameInstant(DEFAULT_REQUEST_DATETIME))))
            .andExpect(jsonPath("$.[*].tankNumbers").value(hasItem(DEFAULT_TANK_NUMBERS)));
    }

    @Test
    @Transactional
    void getMileageRequest() throws Exception {
        // Initialize the database
        mileageRequestRepository.saveAndFlush(mileageRequest);

        // Get the mileageRequest
        restMileageRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, mileageRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mileageRequest.getId().intValue()))
            .andExpect(jsonPath("$.requestDatetime").value(sameInstant(DEFAULT_REQUEST_DATETIME)))
            .andExpect(jsonPath("$.tankNumbers").value(DEFAULT_TANK_NUMBERS));
    }

    @Test
    @Transactional
    void getMileageRequestsByIdFiltering() throws Exception {
        // Initialize the database
        mileageRequestRepository.saveAndFlush(mileageRequest);

        Long id = mileageRequest.getId();

        defaultMileageRequestShouldBeFound("id.equals=" + id);
        defaultMileageRequestShouldNotBeFound("id.notEquals=" + id);

        defaultMileageRequestShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMileageRequestShouldNotBeFound("id.greaterThan=" + id);

        defaultMileageRequestShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMileageRequestShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMileageRequestsByRequestDatetimeIsEqualToSomething() throws Exception {
        // Initialize the database
        mileageRequestRepository.saveAndFlush(mileageRequest);

        // Get all the mileageRequestList where requestDatetime equals to DEFAULT_REQUEST_DATETIME
        defaultMileageRequestShouldBeFound("requestDatetime.equals=" + DEFAULT_REQUEST_DATETIME);

        // Get all the mileageRequestList where requestDatetime equals to UPDATED_REQUEST_DATETIME
        defaultMileageRequestShouldNotBeFound("requestDatetime.equals=" + UPDATED_REQUEST_DATETIME);
    }

    @Test
    @Transactional
    void getAllMileageRequestsByRequestDatetimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mileageRequestRepository.saveAndFlush(mileageRequest);

        // Get all the mileageRequestList where requestDatetime not equals to DEFAULT_REQUEST_DATETIME
        defaultMileageRequestShouldNotBeFound("requestDatetime.notEquals=" + DEFAULT_REQUEST_DATETIME);

        // Get all the mileageRequestList where requestDatetime not equals to UPDATED_REQUEST_DATETIME
        defaultMileageRequestShouldBeFound("requestDatetime.notEquals=" + UPDATED_REQUEST_DATETIME);
    }

    @Test
    @Transactional
    void getAllMileageRequestsByRequestDatetimeIsInShouldWork() throws Exception {
        // Initialize the database
        mileageRequestRepository.saveAndFlush(mileageRequest);

        // Get all the mileageRequestList where requestDatetime in DEFAULT_REQUEST_DATETIME or UPDATED_REQUEST_DATETIME
        defaultMileageRequestShouldBeFound("requestDatetime.in=" + DEFAULT_REQUEST_DATETIME + "," + UPDATED_REQUEST_DATETIME);

        // Get all the mileageRequestList where requestDatetime equals to UPDATED_REQUEST_DATETIME
        defaultMileageRequestShouldNotBeFound("requestDatetime.in=" + UPDATED_REQUEST_DATETIME);
    }

    @Test
    @Transactional
    void getAllMileageRequestsByRequestDatetimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        mileageRequestRepository.saveAndFlush(mileageRequest);

        // Get all the mileageRequestList where requestDatetime is not null
        defaultMileageRequestShouldBeFound("requestDatetime.specified=true");

        // Get all the mileageRequestList where requestDatetime is null
        defaultMileageRequestShouldNotBeFound("requestDatetime.specified=false");
    }

    @Test
    @Transactional
    void getAllMileageRequestsByRequestDatetimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mileageRequestRepository.saveAndFlush(mileageRequest);

        // Get all the mileageRequestList where requestDatetime is greater than or equal to DEFAULT_REQUEST_DATETIME
        defaultMileageRequestShouldBeFound("requestDatetime.greaterThanOrEqual=" + DEFAULT_REQUEST_DATETIME);

        // Get all the mileageRequestList where requestDatetime is greater than or equal to UPDATED_REQUEST_DATETIME
        defaultMileageRequestShouldNotBeFound("requestDatetime.greaterThanOrEqual=" + UPDATED_REQUEST_DATETIME);
    }

    @Test
    @Transactional
    void getAllMileageRequestsByRequestDatetimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mileageRequestRepository.saveAndFlush(mileageRequest);

        // Get all the mileageRequestList where requestDatetime is less than or equal to DEFAULT_REQUEST_DATETIME
        defaultMileageRequestShouldBeFound("requestDatetime.lessThanOrEqual=" + DEFAULT_REQUEST_DATETIME);

        // Get all the mileageRequestList where requestDatetime is less than or equal to SMALLER_REQUEST_DATETIME
        defaultMileageRequestShouldNotBeFound("requestDatetime.lessThanOrEqual=" + SMALLER_REQUEST_DATETIME);
    }

    @Test
    @Transactional
    void getAllMileageRequestsByRequestDatetimeIsLessThanSomething() throws Exception {
        // Initialize the database
        mileageRequestRepository.saveAndFlush(mileageRequest);

        // Get all the mileageRequestList where requestDatetime is less than DEFAULT_REQUEST_DATETIME
        defaultMileageRequestShouldNotBeFound("requestDatetime.lessThan=" + DEFAULT_REQUEST_DATETIME);

        // Get all the mileageRequestList where requestDatetime is less than UPDATED_REQUEST_DATETIME
        defaultMileageRequestShouldBeFound("requestDatetime.lessThan=" + UPDATED_REQUEST_DATETIME);
    }

    @Test
    @Transactional
    void getAllMileageRequestsByRequestDatetimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mileageRequestRepository.saveAndFlush(mileageRequest);

        // Get all the mileageRequestList where requestDatetime is greater than DEFAULT_REQUEST_DATETIME
        defaultMileageRequestShouldNotBeFound("requestDatetime.greaterThan=" + DEFAULT_REQUEST_DATETIME);

        // Get all the mileageRequestList where requestDatetime is greater than SMALLER_REQUEST_DATETIME
        defaultMileageRequestShouldBeFound("requestDatetime.greaterThan=" + SMALLER_REQUEST_DATETIME);
    }

    @Test
    @Transactional
    void getAllMileageRequestsByTankNumbersIsEqualToSomething() throws Exception {
        // Initialize the database
        mileageRequestRepository.saveAndFlush(mileageRequest);

        // Get all the mileageRequestList where tankNumbers equals to DEFAULT_TANK_NUMBERS
        defaultMileageRequestShouldBeFound("tankNumbers.equals=" + DEFAULT_TANK_NUMBERS);

        // Get all the mileageRequestList where tankNumbers equals to UPDATED_TANK_NUMBERS
        defaultMileageRequestShouldNotBeFound("tankNumbers.equals=" + UPDATED_TANK_NUMBERS);
    }

    @Test
    @Transactional
    void getAllMileageRequestsByTankNumbersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mileageRequestRepository.saveAndFlush(mileageRequest);

        // Get all the mileageRequestList where tankNumbers not equals to DEFAULT_TANK_NUMBERS
        defaultMileageRequestShouldNotBeFound("tankNumbers.notEquals=" + DEFAULT_TANK_NUMBERS);

        // Get all the mileageRequestList where tankNumbers not equals to UPDATED_TANK_NUMBERS
        defaultMileageRequestShouldBeFound("tankNumbers.notEquals=" + UPDATED_TANK_NUMBERS);
    }

    @Test
    @Transactional
    void getAllMileageRequestsByTankNumbersIsInShouldWork() throws Exception {
        // Initialize the database
        mileageRequestRepository.saveAndFlush(mileageRequest);

        // Get all the mileageRequestList where tankNumbers in DEFAULT_TANK_NUMBERS or UPDATED_TANK_NUMBERS
        defaultMileageRequestShouldBeFound("tankNumbers.in=" + DEFAULT_TANK_NUMBERS + "," + UPDATED_TANK_NUMBERS);

        // Get all the mileageRequestList where tankNumbers equals to UPDATED_TANK_NUMBERS
        defaultMileageRequestShouldNotBeFound("tankNumbers.in=" + UPDATED_TANK_NUMBERS);
    }

    @Test
    @Transactional
    void getAllMileageRequestsByTankNumbersIsNullOrNotNull() throws Exception {
        // Initialize the database
        mileageRequestRepository.saveAndFlush(mileageRequest);

        // Get all the mileageRequestList where tankNumbers is not null
        defaultMileageRequestShouldBeFound("tankNumbers.specified=true");

        // Get all the mileageRequestList where tankNumbers is null
        defaultMileageRequestShouldNotBeFound("tankNumbers.specified=false");
    }

    @Test
    @Transactional
    void getAllMileageRequestsByTankNumbersContainsSomething() throws Exception {
        // Initialize the database
        mileageRequestRepository.saveAndFlush(mileageRequest);

        // Get all the mileageRequestList where tankNumbers contains DEFAULT_TANK_NUMBERS
        defaultMileageRequestShouldBeFound("tankNumbers.contains=" + DEFAULT_TANK_NUMBERS);

        // Get all the mileageRequestList where tankNumbers contains UPDATED_TANK_NUMBERS
        defaultMileageRequestShouldNotBeFound("tankNumbers.contains=" + UPDATED_TANK_NUMBERS);
    }

    @Test
    @Transactional
    void getAllMileageRequestsByTankNumbersNotContainsSomething() throws Exception {
        // Initialize the database
        mileageRequestRepository.saveAndFlush(mileageRequest);

        // Get all the mileageRequestList where tankNumbers does not contain DEFAULT_TANK_NUMBERS
        defaultMileageRequestShouldNotBeFound("tankNumbers.doesNotContain=" + DEFAULT_TANK_NUMBERS);

        // Get all the mileageRequestList where tankNumbers does not contain UPDATED_TANK_NUMBERS
        defaultMileageRequestShouldBeFound("tankNumbers.doesNotContain=" + UPDATED_TANK_NUMBERS);
    }

    @Test
    @Transactional
    void getAllMileageRequestsByMileageResponseIsEqualToSomething() throws Exception {
        // Initialize the database
        mileageRequestRepository.saveAndFlush(mileageRequest);
        MileageResponse mileageResponse = MileageResponseResourceIT.createEntity(em);
        em.persist(mileageResponse);
        em.flush();
        mileageRequest.setMileageResponse(mileageResponse);
        mileageRequestRepository.saveAndFlush(mileageRequest);
        Long mileageResponseId = mileageResponse.getId();

        // Get all the mileageRequestList where mileageResponse equals to mileageResponseId
        defaultMileageRequestShouldBeFound("mileageResponseId.equals=" + mileageResponseId);

        // Get all the mileageRequestList where mileageResponse equals to (mileageResponseId + 1)
        defaultMileageRequestShouldNotBeFound("mileageResponseId.equals=" + (mileageResponseId + 1));
    }

    @Test
    @Transactional
    void getAllMileageRequestsByTankIsEqualToSomething() throws Exception {
        // Initialize the database
        mileageRequestRepository.saveAndFlush(mileageRequest);
        Tank tank = TankResourceIT.createEntity(em);
        em.persist(tank);
        em.flush();
        mileageRequest.setTank(tank);
        mileageRequestRepository.saveAndFlush(mileageRequest);
        Long tankId = tank.getId();

        // Get all the mileageRequestList where tank equals to tankId
        defaultMileageRequestShouldBeFound("tankId.equals=" + tankId);

        // Get all the mileageRequestList where tank equals to (tankId + 1)
        defaultMileageRequestShouldNotBeFound("tankId.equals=" + (tankId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMileageRequestShouldBeFound(String filter) throws Exception {
        restMileageRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mileageRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestDatetime").value(hasItem(sameInstant(DEFAULT_REQUEST_DATETIME))))
            .andExpect(jsonPath("$.[*].tankNumbers").value(hasItem(DEFAULT_TANK_NUMBERS)));

        // Check, that the count call also returns 1
        restMileageRequestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMileageRequestShouldNotBeFound(String filter) throws Exception {
        restMileageRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMileageRequestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMileageRequest() throws Exception {
        // Get the mileageRequest
        restMileageRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMileageRequest() throws Exception {
        // Initialize the database
        mileageRequestRepository.saveAndFlush(mileageRequest);

        int databaseSizeBeforeUpdate = mileageRequestRepository.findAll().size();

        // Update the mileageRequest
        MileageRequest updatedMileageRequest = mileageRequestRepository.findById(mileageRequest.getId()).get();
        // Disconnect from session so that the updates on updatedMileageRequest are not directly saved in db
        em.detach(updatedMileageRequest);
        updatedMileageRequest.requestDatetime(UPDATED_REQUEST_DATETIME).tankNumbers(UPDATED_TANK_NUMBERS);

        restMileageRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMileageRequest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMileageRequest))
            )
            .andExpect(status().isOk());

        // Validate the MileageRequest in the database
        List<MileageRequest> mileageRequestList = mileageRequestRepository.findAll();
        assertThat(mileageRequestList).hasSize(databaseSizeBeforeUpdate);
        MileageRequest testMileageRequest = mileageRequestList.get(mileageRequestList.size() - 1);
        assertThat(testMileageRequest.getRequestDatetime()).isEqualTo(UPDATED_REQUEST_DATETIME);
        assertThat(testMileageRequest.getTankNumbers()).isEqualTo(UPDATED_TANK_NUMBERS);
    }

    @Test
    @Transactional
    void putNonExistingMileageRequest() throws Exception {
        int databaseSizeBeforeUpdate = mileageRequestRepository.findAll().size();
        mileageRequest.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMileageRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mileageRequest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mileageRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the MileageRequest in the database
        List<MileageRequest> mileageRequestList = mileageRequestRepository.findAll();
        assertThat(mileageRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMileageRequest() throws Exception {
        int databaseSizeBeforeUpdate = mileageRequestRepository.findAll().size();
        mileageRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMileageRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mileageRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the MileageRequest in the database
        List<MileageRequest> mileageRequestList = mileageRequestRepository.findAll();
        assertThat(mileageRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMileageRequest() throws Exception {
        int databaseSizeBeforeUpdate = mileageRequestRepository.findAll().size();
        mileageRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMileageRequestMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mileageRequest)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MileageRequest in the database
        List<MileageRequest> mileageRequestList = mileageRequestRepository.findAll();
        assertThat(mileageRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMileageRequestWithPatch() throws Exception {
        // Initialize the database
        mileageRequestRepository.saveAndFlush(mileageRequest);

        int databaseSizeBeforeUpdate = mileageRequestRepository.findAll().size();

        // Update the mileageRequest using partial update
        MileageRequest partialUpdatedMileageRequest = new MileageRequest();
        partialUpdatedMileageRequest.setId(mileageRequest.getId());

        partialUpdatedMileageRequest.tankNumbers(UPDATED_TANK_NUMBERS);

        restMileageRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMileageRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMileageRequest))
            )
            .andExpect(status().isOk());

        // Validate the MileageRequest in the database
        List<MileageRequest> mileageRequestList = mileageRequestRepository.findAll();
        assertThat(mileageRequestList).hasSize(databaseSizeBeforeUpdate);
        MileageRequest testMileageRequest = mileageRequestList.get(mileageRequestList.size() - 1);
        assertThat(testMileageRequest.getRequestDatetime()).isEqualTo(DEFAULT_REQUEST_DATETIME);
        assertThat(testMileageRequest.getTankNumbers()).isEqualTo(UPDATED_TANK_NUMBERS);
    }

    @Test
    @Transactional
    void fullUpdateMileageRequestWithPatch() throws Exception {
        // Initialize the database
        mileageRequestRepository.saveAndFlush(mileageRequest);

        int databaseSizeBeforeUpdate = mileageRequestRepository.findAll().size();

        // Update the mileageRequest using partial update
        MileageRequest partialUpdatedMileageRequest = new MileageRequest();
        partialUpdatedMileageRequest.setId(mileageRequest.getId());

        partialUpdatedMileageRequest.requestDatetime(UPDATED_REQUEST_DATETIME).tankNumbers(UPDATED_TANK_NUMBERS);

        restMileageRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMileageRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMileageRequest))
            )
            .andExpect(status().isOk());

        // Validate the MileageRequest in the database
        List<MileageRequest> mileageRequestList = mileageRequestRepository.findAll();
        assertThat(mileageRequestList).hasSize(databaseSizeBeforeUpdate);
        MileageRequest testMileageRequest = mileageRequestList.get(mileageRequestList.size() - 1);
        assertThat(testMileageRequest.getRequestDatetime()).isEqualTo(UPDATED_REQUEST_DATETIME);
        assertThat(testMileageRequest.getTankNumbers()).isEqualTo(UPDATED_TANK_NUMBERS);
    }

    @Test
    @Transactional
    void patchNonExistingMileageRequest() throws Exception {
        int databaseSizeBeforeUpdate = mileageRequestRepository.findAll().size();
        mileageRequest.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMileageRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mileageRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mileageRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the MileageRequest in the database
        List<MileageRequest> mileageRequestList = mileageRequestRepository.findAll();
        assertThat(mileageRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMileageRequest() throws Exception {
        int databaseSizeBeforeUpdate = mileageRequestRepository.findAll().size();
        mileageRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMileageRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mileageRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the MileageRequest in the database
        List<MileageRequest> mileageRequestList = mileageRequestRepository.findAll();
        assertThat(mileageRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMileageRequest() throws Exception {
        int databaseSizeBeforeUpdate = mileageRequestRepository.findAll().size();
        mileageRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMileageRequestMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(mileageRequest))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MileageRequest in the database
        List<MileageRequest> mileageRequestList = mileageRequestRepository.findAll();
        assertThat(mileageRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMileageRequest() throws Exception {
        // Initialize the database
        mileageRequestRepository.saveAndFlush(mileageRequest);

        int databaseSizeBeforeDelete = mileageRequestRepository.findAll().size();

        // Delete the mileageRequest
        restMileageRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, mileageRequest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MileageRequest> mileageRequestList = mileageRequestRepository.findAll();
        assertThat(mileageRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
