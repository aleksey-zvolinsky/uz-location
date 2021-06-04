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
import location.uz.domain.MileageResponse;
import location.uz.repository.MileageResponseRepository;
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

    private static final LocalDate DEFAULT_RESPONSE_DATETIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RESPONSE_DATETIME = LocalDate.now(ZoneId.systemDefault());

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
            .andExpect(jsonPath("$.[*].responseDatetime").value(hasItem(DEFAULT_RESPONSE_DATETIME.toString())))
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
            .andExpect(jsonPath("$.responseDatetime").value(DEFAULT_RESPONSE_DATETIME.toString()))
            .andExpect(jsonPath("$.tankNumber").value(DEFAULT_TANK_NUMBER))
            .andExpect(jsonPath("$.mileageCurrent").value(DEFAULT_MILEAGE_CURRENT))
            .andExpect(jsonPath("$.mileageDatetime").value(DEFAULT_MILEAGE_DATETIME))
            .andExpect(jsonPath("$.mileageRemain").value(DEFAULT_MILEAGE_REMAIN))
            .andExpect(jsonPath("$.mileageUpdateDatetime").value(DEFAULT_MILEAGE_UPDATE_DATETIME));
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

        partialUpdatedMileageResponse.mileageCurrent(UPDATED_MILEAGE_CURRENT).mileageUpdateDatetime(UPDATED_MILEAGE_UPDATE_DATETIME);

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
        assertThat(testMileageResponse.getTankNumber()).isEqualTo(DEFAULT_TANK_NUMBER);
        assertThat(testMileageResponse.getMileageCurrent()).isEqualTo(UPDATED_MILEAGE_CURRENT);
        assertThat(testMileageResponse.getMileageDatetime()).isEqualTo(DEFAULT_MILEAGE_DATETIME);
        assertThat(testMileageResponse.getMileageRemain()).isEqualTo(DEFAULT_MILEAGE_REMAIN);
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
