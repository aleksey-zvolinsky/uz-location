package com.kerriline.location.web.rest;

import static com.kerriline.location.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.kerriline.location.IntegrationTest;
import com.kerriline.location.domain.LocationRequest;
import com.kerriline.location.domain.LocationResponse;
import com.kerriline.location.domain.Tank;
import com.kerriline.location.repository.LocationRequestRepository;
import com.kerriline.location.service.criteria.LocationRequestCriteria;
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
 * Integration tests for the {@link LocationRequestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LocationRequestResourceIT {

    private static final ZonedDateTime DEFAULT_REQUEST_DATETIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_REQUEST_DATETIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_REQUEST_DATETIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_TANK_NUMBERS = "AAAAAAAAAA";
    private static final String UPDATED_TANK_NUMBERS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/location-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LocationRequestRepository locationRequestRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocationRequestMockMvc;

    private LocationRequest locationRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocationRequest createEntity(EntityManager em) {
        LocationRequest locationRequest = new LocationRequest().requestDatetime(DEFAULT_REQUEST_DATETIME).tankNumbers(DEFAULT_TANK_NUMBERS);
        return locationRequest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocationRequest createUpdatedEntity(EntityManager em) {
        LocationRequest locationRequest = new LocationRequest().requestDatetime(UPDATED_REQUEST_DATETIME).tankNumbers(UPDATED_TANK_NUMBERS);
        return locationRequest;
    }

    @BeforeEach
    public void initTest() {
        locationRequest = createEntity(em);
    }

    @Test
    @Transactional
    void createLocationRequest() throws Exception {
        int databaseSizeBeforeCreate = locationRequestRepository.findAll().size();
        // Create the LocationRequest
        restLocationRequestMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationRequest))
            )
            .andExpect(status().isCreated());

        // Validate the LocationRequest in the database
        List<LocationRequest> locationRequestList = locationRequestRepository.findAll();
        assertThat(locationRequestList).hasSize(databaseSizeBeforeCreate + 1);
        LocationRequest testLocationRequest = locationRequestList.get(locationRequestList.size() - 1);
        assertThat(testLocationRequest.getRequestDatetime()).isEqualTo(DEFAULT_REQUEST_DATETIME);
        assertThat(testLocationRequest.getTankNumbers()).isEqualTo(DEFAULT_TANK_NUMBERS);
    }

    @Test
    @Transactional
    void createLocationRequestWithExistingId() throws Exception {
        // Create the LocationRequest with an existing ID
        locationRequest.setId(1L);

        int databaseSizeBeforeCreate = locationRequestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocationRequestMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationRequest in the database
        List<LocationRequest> locationRequestList = locationRequestRepository.findAll();
        assertThat(locationRequestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLocationRequests() throws Exception {
        // Initialize the database
        locationRequestRepository.saveAndFlush(locationRequest);

        // Get all the locationRequestList
        restLocationRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(locationRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestDatetime").value(hasItem(sameInstant(DEFAULT_REQUEST_DATETIME))))
            .andExpect(jsonPath("$.[*].tankNumbers").value(hasItem(DEFAULT_TANK_NUMBERS)));
    }

    @Test
    @Transactional
    void getLocationRequest() throws Exception {
        // Initialize the database
        locationRequestRepository.saveAndFlush(locationRequest);

        // Get the locationRequest
        restLocationRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, locationRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(locationRequest.getId().intValue()))
            .andExpect(jsonPath("$.requestDatetime").value(sameInstant(DEFAULT_REQUEST_DATETIME)))
            .andExpect(jsonPath("$.tankNumbers").value(DEFAULT_TANK_NUMBERS));
    }

    @Test
    @Transactional
    void getLocationRequestsByIdFiltering() throws Exception {
        // Initialize the database
        locationRequestRepository.saveAndFlush(locationRequest);

        Long id = locationRequest.getId();

        defaultLocationRequestShouldBeFound("id.equals=" + id);
        defaultLocationRequestShouldNotBeFound("id.notEquals=" + id);

        defaultLocationRequestShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLocationRequestShouldNotBeFound("id.greaterThan=" + id);

        defaultLocationRequestShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLocationRequestShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLocationRequestsByRequestDatetimeIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRequestRepository.saveAndFlush(locationRequest);

        // Get all the locationRequestList where requestDatetime equals to DEFAULT_REQUEST_DATETIME
        defaultLocationRequestShouldBeFound("requestDatetime.equals=" + DEFAULT_REQUEST_DATETIME);

        // Get all the locationRequestList where requestDatetime equals to UPDATED_REQUEST_DATETIME
        defaultLocationRequestShouldNotBeFound("requestDatetime.equals=" + UPDATED_REQUEST_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationRequestsByRequestDatetimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationRequestRepository.saveAndFlush(locationRequest);

        // Get all the locationRequestList where requestDatetime not equals to DEFAULT_REQUEST_DATETIME
        defaultLocationRequestShouldNotBeFound("requestDatetime.notEquals=" + DEFAULT_REQUEST_DATETIME);

        // Get all the locationRequestList where requestDatetime not equals to UPDATED_REQUEST_DATETIME
        defaultLocationRequestShouldBeFound("requestDatetime.notEquals=" + UPDATED_REQUEST_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationRequestsByRequestDatetimeIsInShouldWork() throws Exception {
        // Initialize the database
        locationRequestRepository.saveAndFlush(locationRequest);

        // Get all the locationRequestList where requestDatetime in DEFAULT_REQUEST_DATETIME or UPDATED_REQUEST_DATETIME
        defaultLocationRequestShouldBeFound("requestDatetime.in=" + DEFAULT_REQUEST_DATETIME + "," + UPDATED_REQUEST_DATETIME);

        // Get all the locationRequestList where requestDatetime equals to UPDATED_REQUEST_DATETIME
        defaultLocationRequestShouldNotBeFound("requestDatetime.in=" + UPDATED_REQUEST_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationRequestsByRequestDatetimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRequestRepository.saveAndFlush(locationRequest);

        // Get all the locationRequestList where requestDatetime is not null
        defaultLocationRequestShouldBeFound("requestDatetime.specified=true");

        // Get all the locationRequestList where requestDatetime is null
        defaultLocationRequestShouldNotBeFound("requestDatetime.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationRequestsByRequestDatetimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        locationRequestRepository.saveAndFlush(locationRequest);

        // Get all the locationRequestList where requestDatetime is greater than or equal to DEFAULT_REQUEST_DATETIME
        defaultLocationRequestShouldBeFound("requestDatetime.greaterThanOrEqual=" + DEFAULT_REQUEST_DATETIME);

        // Get all the locationRequestList where requestDatetime is greater than or equal to UPDATED_REQUEST_DATETIME
        defaultLocationRequestShouldNotBeFound("requestDatetime.greaterThanOrEqual=" + UPDATED_REQUEST_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationRequestsByRequestDatetimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        locationRequestRepository.saveAndFlush(locationRequest);

        // Get all the locationRequestList where requestDatetime is less than or equal to DEFAULT_REQUEST_DATETIME
        defaultLocationRequestShouldBeFound("requestDatetime.lessThanOrEqual=" + DEFAULT_REQUEST_DATETIME);

        // Get all the locationRequestList where requestDatetime is less than or equal to SMALLER_REQUEST_DATETIME
        defaultLocationRequestShouldNotBeFound("requestDatetime.lessThanOrEqual=" + SMALLER_REQUEST_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationRequestsByRequestDatetimeIsLessThanSomething() throws Exception {
        // Initialize the database
        locationRequestRepository.saveAndFlush(locationRequest);

        // Get all the locationRequestList where requestDatetime is less than DEFAULT_REQUEST_DATETIME
        defaultLocationRequestShouldNotBeFound("requestDatetime.lessThan=" + DEFAULT_REQUEST_DATETIME);

        // Get all the locationRequestList where requestDatetime is less than UPDATED_REQUEST_DATETIME
        defaultLocationRequestShouldBeFound("requestDatetime.lessThan=" + UPDATED_REQUEST_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationRequestsByRequestDatetimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        locationRequestRepository.saveAndFlush(locationRequest);

        // Get all the locationRequestList where requestDatetime is greater than DEFAULT_REQUEST_DATETIME
        defaultLocationRequestShouldNotBeFound("requestDatetime.greaterThan=" + DEFAULT_REQUEST_DATETIME);

        // Get all the locationRequestList where requestDatetime is greater than SMALLER_REQUEST_DATETIME
        defaultLocationRequestShouldBeFound("requestDatetime.greaterThan=" + SMALLER_REQUEST_DATETIME);
    }

    @Test
    @Transactional
    void getAllLocationRequestsByTankNumbersIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRequestRepository.saveAndFlush(locationRequest);

        // Get all the locationRequestList where tankNumbers equals to DEFAULT_TANK_NUMBERS
        defaultLocationRequestShouldBeFound("tankNumbers.equals=" + DEFAULT_TANK_NUMBERS);

        // Get all the locationRequestList where tankNumbers equals to UPDATED_TANK_NUMBERS
        defaultLocationRequestShouldNotBeFound("tankNumbers.equals=" + UPDATED_TANK_NUMBERS);
    }

    @Test
    @Transactional
    void getAllLocationRequestsByTankNumbersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        locationRequestRepository.saveAndFlush(locationRequest);

        // Get all the locationRequestList where tankNumbers not equals to DEFAULT_TANK_NUMBERS
        defaultLocationRequestShouldNotBeFound("tankNumbers.notEquals=" + DEFAULT_TANK_NUMBERS);

        // Get all the locationRequestList where tankNumbers not equals to UPDATED_TANK_NUMBERS
        defaultLocationRequestShouldBeFound("tankNumbers.notEquals=" + UPDATED_TANK_NUMBERS);
    }

    @Test
    @Transactional
    void getAllLocationRequestsByTankNumbersIsInShouldWork() throws Exception {
        // Initialize the database
        locationRequestRepository.saveAndFlush(locationRequest);

        // Get all the locationRequestList where tankNumbers in DEFAULT_TANK_NUMBERS or UPDATED_TANK_NUMBERS
        defaultLocationRequestShouldBeFound("tankNumbers.in=" + DEFAULT_TANK_NUMBERS + "," + UPDATED_TANK_NUMBERS);

        // Get all the locationRequestList where tankNumbers equals to UPDATED_TANK_NUMBERS
        defaultLocationRequestShouldNotBeFound("tankNumbers.in=" + UPDATED_TANK_NUMBERS);
    }

    @Test
    @Transactional
    void getAllLocationRequestsByTankNumbersIsNullOrNotNull() throws Exception {
        // Initialize the database
        locationRequestRepository.saveAndFlush(locationRequest);

        // Get all the locationRequestList where tankNumbers is not null
        defaultLocationRequestShouldBeFound("tankNumbers.specified=true");

        // Get all the locationRequestList where tankNumbers is null
        defaultLocationRequestShouldNotBeFound("tankNumbers.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationRequestsByTankNumbersContainsSomething() throws Exception {
        // Initialize the database
        locationRequestRepository.saveAndFlush(locationRequest);

        // Get all the locationRequestList where tankNumbers contains DEFAULT_TANK_NUMBERS
        defaultLocationRequestShouldBeFound("tankNumbers.contains=" + DEFAULT_TANK_NUMBERS);

        // Get all the locationRequestList where tankNumbers contains UPDATED_TANK_NUMBERS
        defaultLocationRequestShouldNotBeFound("tankNumbers.contains=" + UPDATED_TANK_NUMBERS);
    }

    @Test
    @Transactional
    void getAllLocationRequestsByTankNumbersNotContainsSomething() throws Exception {
        // Initialize the database
        locationRequestRepository.saveAndFlush(locationRequest);

        // Get all the locationRequestList where tankNumbers does not contain DEFAULT_TANK_NUMBERS
        defaultLocationRequestShouldNotBeFound("tankNumbers.doesNotContain=" + DEFAULT_TANK_NUMBERS);

        // Get all the locationRequestList where tankNumbers does not contain UPDATED_TANK_NUMBERS
        defaultLocationRequestShouldBeFound("tankNumbers.doesNotContain=" + UPDATED_TANK_NUMBERS);
    }

    @Test
    @Transactional
    void getAllLocationRequestsByLocationResponseIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRequestRepository.saveAndFlush(locationRequest);
        LocationResponse locationResponse = LocationResponseResourceIT.createEntity(em);
        em.persist(locationResponse);
        em.flush();
        locationRequest.setLocationResponse(locationResponse);
        locationRequestRepository.saveAndFlush(locationRequest);
        Long locationResponseId = locationResponse.getId();

        // Get all the locationRequestList where locationResponse equals to locationResponseId
        defaultLocationRequestShouldBeFound("locationResponseId.equals=" + locationResponseId);

        // Get all the locationRequestList where locationResponse equals to (locationResponseId + 1)
        defaultLocationRequestShouldNotBeFound("locationResponseId.equals=" + (locationResponseId + 1));
    }

    @Test
    @Transactional
    void getAllLocationRequestsByTankIsEqualToSomething() throws Exception {
        // Initialize the database
        locationRequestRepository.saveAndFlush(locationRequest);
        Tank tank = TankResourceIT.createEntity(em);
        em.persist(tank);
        em.flush();
        locationRequest.setTank(tank);
        locationRequestRepository.saveAndFlush(locationRequest);
        Long tankId = tank.getId();

        // Get all the locationRequestList where tank equals to tankId
        defaultLocationRequestShouldBeFound("tankId.equals=" + tankId);

        // Get all the locationRequestList where tank equals to (tankId + 1)
        defaultLocationRequestShouldNotBeFound("tankId.equals=" + (tankId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLocationRequestShouldBeFound(String filter) throws Exception {
        restLocationRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(locationRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestDatetime").value(hasItem(sameInstant(DEFAULT_REQUEST_DATETIME))))
            .andExpect(jsonPath("$.[*].tankNumbers").value(hasItem(DEFAULT_TANK_NUMBERS)));

        // Check, that the count call also returns 1
        restLocationRequestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLocationRequestShouldNotBeFound(String filter) throws Exception {
        restLocationRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLocationRequestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLocationRequest() throws Exception {
        // Get the locationRequest
        restLocationRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLocationRequest() throws Exception {
        // Initialize the database
        locationRequestRepository.saveAndFlush(locationRequest);

        int databaseSizeBeforeUpdate = locationRequestRepository.findAll().size();

        // Update the locationRequest
        LocationRequest updatedLocationRequest = locationRequestRepository.findById(locationRequest.getId()).get();
        // Disconnect from session so that the updates on updatedLocationRequest are not directly saved in db
        em.detach(updatedLocationRequest);
        updatedLocationRequest.requestDatetime(UPDATED_REQUEST_DATETIME).tankNumbers(UPDATED_TANK_NUMBERS);

        restLocationRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLocationRequest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLocationRequest))
            )
            .andExpect(status().isOk());

        // Validate the LocationRequest in the database
        List<LocationRequest> locationRequestList = locationRequestRepository.findAll();
        assertThat(locationRequestList).hasSize(databaseSizeBeforeUpdate);
        LocationRequest testLocationRequest = locationRequestList.get(locationRequestList.size() - 1);
        assertThat(testLocationRequest.getRequestDatetime()).isEqualTo(UPDATED_REQUEST_DATETIME);
        assertThat(testLocationRequest.getTankNumbers()).isEqualTo(UPDATED_TANK_NUMBERS);
    }

    @Test
    @Transactional
    void putNonExistingLocationRequest() throws Exception {
        int databaseSizeBeforeUpdate = locationRequestRepository.findAll().size();
        locationRequest.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, locationRequest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationRequest in the database
        List<LocationRequest> locationRequestList = locationRequestRepository.findAll();
        assertThat(locationRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLocationRequest() throws Exception {
        int databaseSizeBeforeUpdate = locationRequestRepository.findAll().size();
        locationRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationRequest in the database
        List<LocationRequest> locationRequestList = locationRequestRepository.findAll();
        assertThat(locationRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLocationRequest() throws Exception {
        int databaseSizeBeforeUpdate = locationRequestRepository.findAll().size();
        locationRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationRequestMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationRequest))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LocationRequest in the database
        List<LocationRequest> locationRequestList = locationRequestRepository.findAll();
        assertThat(locationRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLocationRequestWithPatch() throws Exception {
        // Initialize the database
        locationRequestRepository.saveAndFlush(locationRequest);

        int databaseSizeBeforeUpdate = locationRequestRepository.findAll().size();

        // Update the locationRequest using partial update
        LocationRequest partialUpdatedLocationRequest = new LocationRequest();
        partialUpdatedLocationRequest.setId(locationRequest.getId());

        partialUpdatedLocationRequest.tankNumbers(UPDATED_TANK_NUMBERS);

        restLocationRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocationRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocationRequest))
            )
            .andExpect(status().isOk());

        // Validate the LocationRequest in the database
        List<LocationRequest> locationRequestList = locationRequestRepository.findAll();
        assertThat(locationRequestList).hasSize(databaseSizeBeforeUpdate);
        LocationRequest testLocationRequest = locationRequestList.get(locationRequestList.size() - 1);
        assertThat(testLocationRequest.getRequestDatetime()).isEqualTo(DEFAULT_REQUEST_DATETIME);
        assertThat(testLocationRequest.getTankNumbers()).isEqualTo(UPDATED_TANK_NUMBERS);
    }

    @Test
    @Transactional
    void fullUpdateLocationRequestWithPatch() throws Exception {
        // Initialize the database
        locationRequestRepository.saveAndFlush(locationRequest);

        int databaseSizeBeforeUpdate = locationRequestRepository.findAll().size();

        // Update the locationRequest using partial update
        LocationRequest partialUpdatedLocationRequest = new LocationRequest();
        partialUpdatedLocationRequest.setId(locationRequest.getId());

        partialUpdatedLocationRequest.requestDatetime(UPDATED_REQUEST_DATETIME).tankNumbers(UPDATED_TANK_NUMBERS);

        restLocationRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocationRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocationRequest))
            )
            .andExpect(status().isOk());

        // Validate the LocationRequest in the database
        List<LocationRequest> locationRequestList = locationRequestRepository.findAll();
        assertThat(locationRequestList).hasSize(databaseSizeBeforeUpdate);
        LocationRequest testLocationRequest = locationRequestList.get(locationRequestList.size() - 1);
        assertThat(testLocationRequest.getRequestDatetime()).isEqualTo(UPDATED_REQUEST_DATETIME);
        assertThat(testLocationRequest.getTankNumbers()).isEqualTo(UPDATED_TANK_NUMBERS);
    }

    @Test
    @Transactional
    void patchNonExistingLocationRequest() throws Exception {
        int databaseSizeBeforeUpdate = locationRequestRepository.findAll().size();
        locationRequest.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, locationRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationRequest in the database
        List<LocationRequest> locationRequestList = locationRequestRepository.findAll();
        assertThat(locationRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLocationRequest() throws Exception {
        int databaseSizeBeforeUpdate = locationRequestRepository.findAll().size();
        locationRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationRequest in the database
        List<LocationRequest> locationRequestList = locationRequestRepository.findAll();
        assertThat(locationRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLocationRequest() throws Exception {
        int databaseSizeBeforeUpdate = locationRequestRepository.findAll().size();
        locationRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationRequestMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationRequest))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LocationRequest in the database
        List<LocationRequest> locationRequestList = locationRequestRepository.findAll();
        assertThat(locationRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLocationRequest() throws Exception {
        // Initialize the database
        locationRequestRepository.saveAndFlush(locationRequest);

        int databaseSizeBeforeDelete = locationRequestRepository.findAll().size();

        // Delete the locationRequest
        restLocationRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, locationRequest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LocationRequest> locationRequestList = locationRequestRepository.findAll();
        assertThat(locationRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
