package location.uz.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import location.uz.IntegrationTest;
import location.uz.domain.LocationRequest;
import location.uz.repository.LocationRequestRepository;
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

    private static final LocalDate DEFAULT_REQUEST_DATETIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REQUEST_DATETIME = LocalDate.now(ZoneId.systemDefault());

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
            .andExpect(jsonPath("$.[*].requestDatetime").value(hasItem(DEFAULT_REQUEST_DATETIME.toString())))
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
            .andExpect(jsonPath("$.requestDatetime").value(DEFAULT_REQUEST_DATETIME.toString()))
            .andExpect(jsonPath("$.tankNumbers").value(DEFAULT_TANK_NUMBERS));
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
