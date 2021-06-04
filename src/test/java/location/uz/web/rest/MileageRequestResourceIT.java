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
import location.uz.domain.MileageRequest;
import location.uz.repository.MileageRequestRepository;
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

    private static final LocalDate DEFAULT_REQUEST_DATETIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REQUEST_DATETIME = LocalDate.now(ZoneId.systemDefault());

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
            .andExpect(jsonPath("$.[*].requestDatetime").value(hasItem(DEFAULT_REQUEST_DATETIME.toString())))
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
            .andExpect(jsonPath("$.requestDatetime").value(DEFAULT_REQUEST_DATETIME.toString()))
            .andExpect(jsonPath("$.tankNumbers").value(DEFAULT_TANK_NUMBERS));
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

        partialUpdatedMileageRequest.requestDatetime(UPDATED_REQUEST_DATETIME);

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
        assertThat(testMileageRequest.getTankNumbers()).isEqualTo(DEFAULT_TANK_NUMBERS);
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
