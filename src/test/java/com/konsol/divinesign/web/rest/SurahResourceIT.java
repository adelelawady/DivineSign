package com.konsol.divinesign.web.rest;

import static com.konsol.divinesign.domain.SurahAsserts.*;
import static com.konsol.divinesign.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.konsol.divinesign.IntegrationTest;
import com.konsol.divinesign.domain.Surah;
import com.konsol.divinesign.repository.SurahRepository;
import com.konsol.divinesign.service.dto.SurahDTO;
import com.konsol.divinesign.service.mapper.SurahMapper;
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
 * Integration tests for the {@link SurahResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SurahResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TRANSLITERATION = "AAAAAAAAAA";
    private static final String UPDATED_TRANSLITERATION = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_TOTAL_VERSES = 1;
    private static final Integer UPDATED_TOTAL_VERSES = 2;

    private static final String ENTITY_API_URL = "/api/surahs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SurahRepository surahRepository;

    @Autowired
    private SurahMapper surahMapper;

    @Autowired
    private MockMvc restSurahMockMvc;

    private Surah surah;

    private Surah insertedSurah;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Surah createEntity() {
        return new Surah().name(DEFAULT_NAME).transliteration(DEFAULT_TRANSLITERATION).type(DEFAULT_TYPE).totalVerses(DEFAULT_TOTAL_VERSES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Surah createUpdatedEntity() {
        return new Surah().name(UPDATED_NAME).transliteration(UPDATED_TRANSLITERATION).type(UPDATED_TYPE).totalVerses(UPDATED_TOTAL_VERSES);
    }

    @BeforeEach
    public void initTest() {
        surah = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSurah != null) {
            surahRepository.delete(insertedSurah);
            insertedSurah = null;
        }
    }

    @Test
    void createSurah() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Surah
        SurahDTO surahDTO = surahMapper.toDto(surah);
        var returnedSurahDTO = om.readValue(
            restSurahMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(surahDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SurahDTO.class
        );

        // Validate the Surah in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSurah = surahMapper.toEntity(returnedSurahDTO);
        assertSurahUpdatableFieldsEquals(returnedSurah, getPersistedSurah(returnedSurah));

        insertedSurah = returnedSurah;
    }

    @Test
    void createSurahWithExistingId() throws Exception {
        // Create the Surah with an existing ID
        surah.setId("existing_id");
        SurahDTO surahDTO = surahMapper.toDto(surah);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSurahMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(surahDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Surah in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllSurahs() throws Exception {
        // Initialize the database
        insertedSurah = surahRepository.save(surah);

        // Get all the surahList
        restSurahMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(surah.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].transliteration").value(hasItem(DEFAULT_TRANSLITERATION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].totalVerses").value(hasItem(DEFAULT_TOTAL_VERSES)));
    }

    @Test
    void getSurah() throws Exception {
        // Initialize the database
        insertedSurah = surahRepository.save(surah);

        // Get the surah
        restSurahMockMvc
            .perform(get(ENTITY_API_URL_ID, surah.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(surah.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.transliteration").value(DEFAULT_TRANSLITERATION))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.totalVerses").value(DEFAULT_TOTAL_VERSES));
    }

    @Test
    void getNonExistingSurah() throws Exception {
        // Get the surah
        restSurahMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingSurah() throws Exception {
        // Initialize the database
        insertedSurah = surahRepository.save(surah);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the surah
        Surah updatedSurah = surahRepository.findById(surah.getId()).orElseThrow();
        updatedSurah.name(UPDATED_NAME).transliteration(UPDATED_TRANSLITERATION).type(UPDATED_TYPE).totalVerses(UPDATED_TOTAL_VERSES);
        SurahDTO surahDTO = surahMapper.toDto(updatedSurah);

        restSurahMockMvc
            .perform(
                put(ENTITY_API_URL_ID, surahDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(surahDTO))
            )
            .andExpect(status().isOk());

        // Validate the Surah in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSurahToMatchAllProperties(updatedSurah);
    }

    @Test
    void putNonExistingSurah() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        surah.setId(UUID.randomUUID().toString());

        // Create the Surah
        SurahDTO surahDTO = surahMapper.toDto(surah);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSurahMockMvc
            .perform(
                put(ENTITY_API_URL_ID, surahDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(surahDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Surah in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSurah() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        surah.setId(UUID.randomUUID().toString());

        // Create the Surah
        SurahDTO surahDTO = surahMapper.toDto(surah);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurahMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(surahDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Surah in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSurah() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        surah.setId(UUID.randomUUID().toString());

        // Create the Surah
        SurahDTO surahDTO = surahMapper.toDto(surah);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurahMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(surahDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Surah in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSurahWithPatch() throws Exception {
        // Initialize the database
        insertedSurah = surahRepository.save(surah);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the surah using partial update
        Surah partialUpdatedSurah = new Surah();
        partialUpdatedSurah.setId(surah.getId());

        partialUpdatedSurah.name(UPDATED_NAME).transliteration(UPDATED_TRANSLITERATION).totalVerses(UPDATED_TOTAL_VERSES);

        restSurahMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSurah.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSurah))
            )
            .andExpect(status().isOk());

        // Validate the Surah in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSurahUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSurah, surah), getPersistedSurah(surah));
    }

    @Test
    void fullUpdateSurahWithPatch() throws Exception {
        // Initialize the database
        insertedSurah = surahRepository.save(surah);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the surah using partial update
        Surah partialUpdatedSurah = new Surah();
        partialUpdatedSurah.setId(surah.getId());

        partialUpdatedSurah
            .name(UPDATED_NAME)
            .transliteration(UPDATED_TRANSLITERATION)
            .type(UPDATED_TYPE)
            .totalVerses(UPDATED_TOTAL_VERSES);

        restSurahMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSurah.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSurah))
            )
            .andExpect(status().isOk());

        // Validate the Surah in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSurahUpdatableFieldsEquals(partialUpdatedSurah, getPersistedSurah(partialUpdatedSurah));
    }

    @Test
    void patchNonExistingSurah() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        surah.setId(UUID.randomUUID().toString());

        // Create the Surah
        SurahDTO surahDTO = surahMapper.toDto(surah);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSurahMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, surahDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(surahDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Surah in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSurah() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        surah.setId(UUID.randomUUID().toString());

        // Create the Surah
        SurahDTO surahDTO = surahMapper.toDto(surah);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurahMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(surahDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Surah in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSurah() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        surah.setId(UUID.randomUUID().toString());

        // Create the Surah
        SurahDTO surahDTO = surahMapper.toDto(surah);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurahMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(surahDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Surah in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSurah() throws Exception {
        // Initialize the database
        insertedSurah = surahRepository.save(surah);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the surah
        restSurahMockMvc
            .perform(delete(ENTITY_API_URL_ID, surah.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return surahRepository.count();
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

    protected Surah getPersistedSurah(Surah surah) {
        return surahRepository.findById(surah.getId()).orElseThrow();
    }

    protected void assertPersistedSurahToMatchAllProperties(Surah expectedSurah) {
        assertSurahAllPropertiesEquals(expectedSurah, getPersistedSurah(expectedSurah));
    }

    protected void assertPersistedSurahToMatchUpdatableProperties(Surah expectedSurah) {
        assertSurahAllUpdatablePropertiesEquals(expectedSurah, getPersistedSurah(expectedSurah));
    }
}
