package com.konsol.divinesign.web.rest;

import static com.konsol.divinesign.domain.SplendVersesAsserts.*;
import static com.konsol.divinesign.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.konsol.divinesign.IntegrationTest;
import com.konsol.divinesign.domain.SplendVerses;
import com.konsol.divinesign.repository.SplendVersesRepository;
import com.konsol.divinesign.service.dto.SplendVersesDTO;
import com.konsol.divinesign.service.mapper.SplendVersesMapper;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link SplendVersesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SplendVersesResourceIT {

    private static final String DEFAULT_WORD = "AAAAAAAAAA";
    private static final String UPDATED_WORD = "BBBBBBBBBB";

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_CONDITION = "AAAAAAAAAA";
    private static final String UPDATED_CONDITION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/splend-verses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SplendVersesRepository splendVersesRepository;

    @Autowired
    private SplendVersesMapper splendVersesMapper;

    @Autowired
    private MockMvc restSplendVersesMockMvc;

    private SplendVerses splendVerses;

    private SplendVerses insertedSplendVerses;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SplendVerses createEntity() {
        return new SplendVerses().word(DEFAULT_WORD).number(DEFAULT_NUMBER).condition(DEFAULT_CONDITION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SplendVerses createUpdatedEntity() {
        return new SplendVerses().word(UPDATED_WORD).number(UPDATED_NUMBER).condition(UPDATED_CONDITION);
    }

    @BeforeEach
    public void initTest() {
        splendVerses = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSplendVerses != null) {
            splendVersesRepository.delete(insertedSplendVerses);
            insertedSplendVerses = null;
        }
    }

    @Test
    void createSplendVerses() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SplendVerses
        SplendVersesDTO splendVersesDTO = splendVersesMapper.toDto(splendVerses);
        var returnedSplendVersesDTO = om.readValue(
            restSplendVersesMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(splendVersesDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SplendVersesDTO.class
        );

        // Validate the SplendVerses in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSplendVerses = splendVersesMapper.toEntity(returnedSplendVersesDTO);
        assertSplendVersesUpdatableFieldsEquals(returnedSplendVerses, getPersistedSplendVerses(returnedSplendVerses));

        insertedSplendVerses = returnedSplendVerses;
    }

    @Test
    void createSplendVersesWithExistingId() throws Exception {
        // Create the SplendVerses with an existing ID
        splendVerses.setId("existing_id");
        SplendVersesDTO splendVersesDTO = splendVersesMapper.toDto(splendVerses);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSplendVersesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(splendVersesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SplendVerses in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllSplendVerses() throws Exception {
        // Initialize the database
        insertedSplendVerses = splendVersesRepository.save(splendVerses);

        // Get all the splendVersesList
        restSplendVersesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(splendVerses.getId())))
            .andExpect(jsonPath("$.[*].word").value(hasItem(DEFAULT_WORD)))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].condition").value(hasItem(DEFAULT_CONDITION)));
    }

    @Test
    void getSplendVerses() throws Exception {
        // Initialize the database
        insertedSplendVerses = splendVersesRepository.save(splendVerses);

        // Get the splendVerses
        restSplendVersesMockMvc
            .perform(get(ENTITY_API_URL_ID, splendVerses.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(splendVerses.getId()))
            .andExpect(jsonPath("$.word").value(DEFAULT_WORD))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.condition").value(DEFAULT_CONDITION));
    }

    @Test
    void getNonExistingSplendVerses() throws Exception {
        // Get the splendVerses
        restSplendVersesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingSplendVerses() throws Exception {
        // Initialize the database
        insertedSplendVerses = splendVersesRepository.save(splendVerses);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the splendVerses
        SplendVerses updatedSplendVerses = splendVersesRepository.findById(splendVerses.getId()).orElseThrow();
        updatedSplendVerses.word(UPDATED_WORD).number(UPDATED_NUMBER).condition(UPDATED_CONDITION);
        SplendVersesDTO splendVersesDTO = splendVersesMapper.toDto(updatedSplendVerses);

        restSplendVersesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, splendVersesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(splendVersesDTO))
            )
            .andExpect(status().isOk());

        // Validate the SplendVerses in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSplendVersesToMatchAllProperties(updatedSplendVerses);
    }

    @Test
    void putNonExistingSplendVerses() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        splendVerses.setId(UUID.randomUUID().toString());

        // Create the SplendVerses
        SplendVersesDTO splendVersesDTO = splendVersesMapper.toDto(splendVerses);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSplendVersesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, splendVersesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(splendVersesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SplendVerses in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSplendVerses() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        splendVerses.setId(UUID.randomUUID().toString());

        // Create the SplendVerses
        SplendVersesDTO splendVersesDTO = splendVersesMapper.toDto(splendVerses);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSplendVersesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(splendVersesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SplendVerses in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSplendVerses() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        splendVerses.setId(UUID.randomUUID().toString());

        // Create the SplendVerses
        SplendVersesDTO splendVersesDTO = splendVersesMapper.toDto(splendVerses);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSplendVersesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(splendVersesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SplendVerses in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSplendVersesWithPatch() throws Exception {
        // Initialize the database
        insertedSplendVerses = splendVersesRepository.save(splendVerses);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the splendVerses using partial update
        SplendVerses partialUpdatedSplendVerses = new SplendVerses();
        partialUpdatedSplendVerses.setId(splendVerses.getId());

        partialUpdatedSplendVerses.number(UPDATED_NUMBER).condition(UPDATED_CONDITION);

        restSplendVersesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSplendVerses.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSplendVerses))
            )
            .andExpect(status().isOk());

        // Validate the SplendVerses in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSplendVersesUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSplendVerses, splendVerses),
            getPersistedSplendVerses(splendVerses)
        );
    }

    @Test
    void fullUpdateSplendVersesWithPatch() throws Exception {
        // Initialize the database
        insertedSplendVerses = splendVersesRepository.save(splendVerses);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the splendVerses using partial update
        SplendVerses partialUpdatedSplendVerses = new SplendVerses();
        partialUpdatedSplendVerses.setId(splendVerses.getId());

        partialUpdatedSplendVerses.word(UPDATED_WORD).number(UPDATED_NUMBER).condition(UPDATED_CONDITION);

        restSplendVersesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSplendVerses.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSplendVerses))
            )
            .andExpect(status().isOk());

        // Validate the SplendVerses in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSplendVersesUpdatableFieldsEquals(partialUpdatedSplendVerses, getPersistedSplendVerses(partialUpdatedSplendVerses));
    }

    @Test
    void patchNonExistingSplendVerses() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        splendVerses.setId(UUID.randomUUID().toString());

        // Create the SplendVerses
        SplendVersesDTO splendVersesDTO = splendVersesMapper.toDto(splendVerses);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSplendVersesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, splendVersesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(splendVersesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SplendVerses in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSplendVerses() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        splendVerses.setId(UUID.randomUUID().toString());

        // Create the SplendVerses
        SplendVersesDTO splendVersesDTO = splendVersesMapper.toDto(splendVerses);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSplendVersesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(splendVersesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SplendVerses in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSplendVerses() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        splendVerses.setId(UUID.randomUUID().toString());

        // Create the SplendVerses
        SplendVersesDTO splendVersesDTO = splendVersesMapper.toDto(splendVerses);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSplendVersesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(splendVersesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SplendVerses in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSplendVerses() throws Exception {
        // Initialize the database
        insertedSplendVerses = splendVersesRepository.save(splendVerses);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the splendVerses
        restSplendVersesMockMvc
            .perform(delete(ENTITY_API_URL_ID, splendVerses.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return splendVersesRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected SplendVerses getPersistedSplendVerses(SplendVerses splendVerses) {
        return splendVersesRepository.findById(splendVerses.getId()).orElseThrow();
    }

    protected void assertPersistedSplendVersesToMatchAllProperties(SplendVerses expectedSplendVerses) {
        assertSplendVersesAllPropertiesEquals(expectedSplendVerses, getPersistedSplendVerses(expectedSplendVerses));
    }

    protected void assertPersistedSplendVersesToMatchUpdatableProperties(SplendVerses expectedSplendVerses) {
        assertSplendVersesAllUpdatablePropertiesEquals(expectedSplendVerses, getPersistedSplendVerses(expectedSplendVerses));
    }
}
