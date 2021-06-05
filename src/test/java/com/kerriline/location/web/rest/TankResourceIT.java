package com.kerriline.location.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.kerriline.location.IntegrationTest;
import com.kerriline.location.domain.Tank;
import com.kerriline.location.repository.TankRepository;
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
 * Integration tests for the {@link TankResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TankResourceIT {

    private static final String DEFAULT_TANK_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TANK_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_OWNER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_OWNER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CLIENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tanks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TankRepository tankRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTankMockMvc;

    private Tank tank;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tank createEntity(EntityManager em) {
        Tank tank = new Tank().tankNumber(DEFAULT_TANK_NUMBER).ownerName(DEFAULT_OWNER_NAME).clientName(DEFAULT_CLIENT_NAME);
        return tank;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tank createUpdatedEntity(EntityManager em) {
        Tank tank = new Tank().tankNumber(UPDATED_TANK_NUMBER).ownerName(UPDATED_OWNER_NAME).clientName(UPDATED_CLIENT_NAME);
        return tank;
    }

    @BeforeEach
    public void initTest() {
        tank = createEntity(em);
    }

    @Test
    @Transactional
    void createTank() throws Exception {
        int databaseSizeBeforeCreate = tankRepository.findAll().size();
        // Create the Tank
        restTankMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tank)))
            .andExpect(status().isCreated());

        // Validate the Tank in the database
        List<Tank> tankList = tankRepository.findAll();
        assertThat(tankList).hasSize(databaseSizeBeforeCreate + 1);
        Tank testTank = tankList.get(tankList.size() - 1);
        assertThat(testTank.getTankNumber()).isEqualTo(DEFAULT_TANK_NUMBER);
        assertThat(testTank.getOwnerName()).isEqualTo(DEFAULT_OWNER_NAME);
        assertThat(testTank.getClientName()).isEqualTo(DEFAULT_CLIENT_NAME);
    }

    @Test
    @Transactional
    void createTankWithExistingId() throws Exception {
        // Create the Tank with an existing ID
        tank.setId(1L);

        int databaseSizeBeforeCreate = tankRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTankMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tank)))
            .andExpect(status().isBadRequest());

        // Validate the Tank in the database
        List<Tank> tankList = tankRepository.findAll();
        assertThat(tankList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTankNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = tankRepository.findAll().size();
        // set the field null
        tank.setTankNumber(null);

        // Create the Tank, which fails.

        restTankMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tank)))
            .andExpect(status().isBadRequest());

        List<Tank> tankList = tankRepository.findAll();
        assertThat(tankList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOwnerNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = tankRepository.findAll().size();
        // set the field null
        tank.setOwnerName(null);

        // Create the Tank, which fails.

        restTankMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tank)))
            .andExpect(status().isBadRequest());

        List<Tank> tankList = tankRepository.findAll();
        assertThat(tankList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkClientNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = tankRepository.findAll().size();
        // set the field null
        tank.setClientName(null);

        // Create the Tank, which fails.

        restTankMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tank)))
            .andExpect(status().isBadRequest());

        List<Tank> tankList = tankRepository.findAll();
        assertThat(tankList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTanks() throws Exception {
        // Initialize the database
        tankRepository.saveAndFlush(tank);

        // Get all the tankList
        restTankMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tank.getId().intValue())))
            .andExpect(jsonPath("$.[*].tankNumber").value(hasItem(DEFAULT_TANK_NUMBER)))
            .andExpect(jsonPath("$.[*].ownerName").value(hasItem(DEFAULT_OWNER_NAME)))
            .andExpect(jsonPath("$.[*].clientName").value(hasItem(DEFAULT_CLIENT_NAME)));
    }

    @Test
    @Transactional
    void getTank() throws Exception {
        // Initialize the database
        tankRepository.saveAndFlush(tank);

        // Get the tank
        restTankMockMvc
            .perform(get(ENTITY_API_URL_ID, tank.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tank.getId().intValue()))
            .andExpect(jsonPath("$.tankNumber").value(DEFAULT_TANK_NUMBER))
            .andExpect(jsonPath("$.ownerName").value(DEFAULT_OWNER_NAME))
            .andExpect(jsonPath("$.clientName").value(DEFAULT_CLIENT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingTank() throws Exception {
        // Get the tank
        restTankMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTank() throws Exception {
        // Initialize the database
        tankRepository.saveAndFlush(tank);

        int databaseSizeBeforeUpdate = tankRepository.findAll().size();

        // Update the tank
        Tank updatedTank = tankRepository.findById(tank.getId()).get();
        // Disconnect from session so that the updates on updatedTank are not directly saved in db
        em.detach(updatedTank);
        updatedTank.tankNumber(UPDATED_TANK_NUMBER).ownerName(UPDATED_OWNER_NAME).clientName(UPDATED_CLIENT_NAME);

        restTankMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTank.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTank))
            )
            .andExpect(status().isOk());

        // Validate the Tank in the database
        List<Tank> tankList = tankRepository.findAll();
        assertThat(tankList).hasSize(databaseSizeBeforeUpdate);
        Tank testTank = tankList.get(tankList.size() - 1);
        assertThat(testTank.getTankNumber()).isEqualTo(UPDATED_TANK_NUMBER);
        assertThat(testTank.getOwnerName()).isEqualTo(UPDATED_OWNER_NAME);
        assertThat(testTank.getClientName()).isEqualTo(UPDATED_CLIENT_NAME);
    }

    @Test
    @Transactional
    void putNonExistingTank() throws Exception {
        int databaseSizeBeforeUpdate = tankRepository.findAll().size();
        tank.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTankMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tank.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tank))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tank in the database
        List<Tank> tankList = tankRepository.findAll();
        assertThat(tankList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTank() throws Exception {
        int databaseSizeBeforeUpdate = tankRepository.findAll().size();
        tank.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTankMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tank))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tank in the database
        List<Tank> tankList = tankRepository.findAll();
        assertThat(tankList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTank() throws Exception {
        int databaseSizeBeforeUpdate = tankRepository.findAll().size();
        tank.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTankMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tank)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tank in the database
        List<Tank> tankList = tankRepository.findAll();
        assertThat(tankList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTankWithPatch() throws Exception {
        // Initialize the database
        tankRepository.saveAndFlush(tank);

        int databaseSizeBeforeUpdate = tankRepository.findAll().size();

        // Update the tank using partial update
        Tank partialUpdatedTank = new Tank();
        partialUpdatedTank.setId(tank.getId());

        partialUpdatedTank.ownerName(UPDATED_OWNER_NAME);

        restTankMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTank.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTank))
            )
            .andExpect(status().isOk());

        // Validate the Tank in the database
        List<Tank> tankList = tankRepository.findAll();
        assertThat(tankList).hasSize(databaseSizeBeforeUpdate);
        Tank testTank = tankList.get(tankList.size() - 1);
        assertThat(testTank.getTankNumber()).isEqualTo(DEFAULT_TANK_NUMBER);
        assertThat(testTank.getOwnerName()).isEqualTo(UPDATED_OWNER_NAME);
        assertThat(testTank.getClientName()).isEqualTo(DEFAULT_CLIENT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateTankWithPatch() throws Exception {
        // Initialize the database
        tankRepository.saveAndFlush(tank);

        int databaseSizeBeforeUpdate = tankRepository.findAll().size();

        // Update the tank using partial update
        Tank partialUpdatedTank = new Tank();
        partialUpdatedTank.setId(tank.getId());

        partialUpdatedTank.tankNumber(UPDATED_TANK_NUMBER).ownerName(UPDATED_OWNER_NAME).clientName(UPDATED_CLIENT_NAME);

        restTankMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTank.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTank))
            )
            .andExpect(status().isOk());

        // Validate the Tank in the database
        List<Tank> tankList = tankRepository.findAll();
        assertThat(tankList).hasSize(databaseSizeBeforeUpdate);
        Tank testTank = tankList.get(tankList.size() - 1);
        assertThat(testTank.getTankNumber()).isEqualTo(UPDATED_TANK_NUMBER);
        assertThat(testTank.getOwnerName()).isEqualTo(UPDATED_OWNER_NAME);
        assertThat(testTank.getClientName()).isEqualTo(UPDATED_CLIENT_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingTank() throws Exception {
        int databaseSizeBeforeUpdate = tankRepository.findAll().size();
        tank.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTankMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tank.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tank))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tank in the database
        List<Tank> tankList = tankRepository.findAll();
        assertThat(tankList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTank() throws Exception {
        int databaseSizeBeforeUpdate = tankRepository.findAll().size();
        tank.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTankMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tank))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tank in the database
        List<Tank> tankList = tankRepository.findAll();
        assertThat(tankList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTank() throws Exception {
        int databaseSizeBeforeUpdate = tankRepository.findAll().size();
        tank.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTankMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tank)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tank in the database
        List<Tank> tankList = tankRepository.findAll();
        assertThat(tankList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTank() throws Exception {
        // Initialize the database
        tankRepository.saveAndFlush(tank);

        int databaseSizeBeforeDelete = tankRepository.findAll().size();

        // Delete the tank
        restTankMockMvc
            .perform(delete(ENTITY_API_URL_ID, tank.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tank> tankList = tankRepository.findAll();
        assertThat(tankList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
