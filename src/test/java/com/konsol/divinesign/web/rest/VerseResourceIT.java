package com.konsol.divinesign.web.rest;

import static com.konsol.divinesign.domain.VerseAsserts.*;
import static com.konsol.divinesign.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.konsol.divinesign.IntegrationTest;
import com.konsol.divinesign.domain.Verse;
import com.konsol.divinesign.repository.VerseRepository;
import com.konsol.divinesign.service.dto.VerseDTO;
import com.konsol.divinesign.service.mapper.VerseMapper;
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
 * Integration tests for the {@link VerseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VerseResourceIT {

    private static final String DEFAULT_VERSE = "AAAAAAAAAA";
    private static final String UPDATED_VERSE = "BBBBBBBBBB";

    private static final String DEFAULT_DIACRITIC_VERSE = "AAAAAAAAAA";
    private static final String UPDATED_DIACRITIC_VERSE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/verses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VerseRepository verseRepository;

    @Autowired
    private VerseMapper verseMapper;

    @Autowired
    private MockMvc restVerseMockMvc;

    private Verse verse;

    private Verse insertedVerse;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Verse createEntity() {
        return new Verse().verse(DEFAULT_VERSE).diacriticVerse(DEFAULT_DIACRITIC_VERSE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Verse createUpdatedEntity() {
        return new Verse().verse(UPDATED_VERSE).diacriticVerse(UPDATED_DIACRITIC_VERSE);
    }

    @BeforeEach
    public void initTest() {
        verse = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedVerse != null) {
            verseRepository.delete(insertedVerse);
            insertedVerse = null;
        }
    }

    @Test
    void createVerse() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Verse
        VerseDTO verseDTO = verseMapper.toDto(verse);
        var returnedVerseDTO = om.readValue(
            restVerseMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(verseDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VerseDTO.class
        );

        // Validate the Verse in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedVerse = verseMapper.toEntity(returnedVerseDTO);
        assertVerseUpdatableFieldsEquals(returnedVerse, getPersistedVerse(returnedVerse));

        insertedVerse = returnedVerse;
    }

    @Test
    void createVerseWithExistingId() throws Exception {
        // Create the Verse with an existing ID
        verse.setId("existing_id");
        VerseDTO verseDTO = verseMapper.toDto(verse);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVerseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(verseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Verse in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllVerses() throws Exception {
        // Initialize the database
        insertedVerse = verseRepository.save(verse);

        // Get all the verseList
        restVerseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(verse.getId())))
            .andExpect(jsonPath("$.[*].verse").value(hasItem(DEFAULT_VERSE)))
            .andExpect(jsonPath("$.[*].diacriticVerse").value(hasItem(DEFAULT_DIACRITIC_VERSE)));
    }

    @Test
    void getVerse() throws Exception {
        // Initialize the database
        insertedVerse = verseRepository.save(verse);

        // Get the verse
        restVerseMockMvc
            .perform(get(ENTITY_API_URL_ID, verse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(verse.getId()))
            .andExpect(jsonPath("$.verse").value(DEFAULT_VERSE))
            .andExpect(jsonPath("$.diacriticVerse").value(DEFAULT_DIACRITIC_VERSE));
    }

    @Test
    void getNonExistingVerse() throws Exception {
        // Get the verse
        restVerseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingVerse() throws Exception {
        // Initialize the database
        insertedVerse = verseRepository.save(verse);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the verse
        Verse updatedVerse = verseRepository.findById(verse.getId()).orElseThrow();
        updatedVerse.verse(UPDATED_VERSE).diacriticVerse(UPDATED_DIACRITIC_VERSE);
        VerseDTO verseDTO = verseMapper.toDto(updatedVerse);

        restVerseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, verseDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(verseDTO))
            )
            .andExpect(status().isOk());

        // Validate the Verse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVerseToMatchAllProperties(updatedVerse);
    }

    @Test
    void putNonExistingVerse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        verse.setId(UUID.randomUUID().toString());

        // Create the Verse
        VerseDTO verseDTO = verseMapper.toDto(verse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVerseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, verseDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(verseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Verse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchVerse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        verse.setId(UUID.randomUUID().toString());

        // Create the Verse
        VerseDTO verseDTO = verseMapper.toDto(verse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVerseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(verseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Verse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamVerse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        verse.setId(UUID.randomUUID().toString());

        // Create the Verse
        VerseDTO verseDTO = verseMapper.toDto(verse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVerseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(verseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Verse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateVerseWithPatch() throws Exception {
        // Initialize the database
        insertedVerse = verseRepository.save(verse);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the verse using partial update
        Verse partialUpdatedVerse = new Verse();
        partialUpdatedVerse.setId(verse.getId());

        partialUpdatedVerse.verse(UPDATED_VERSE).diacriticVerse(UPDATED_DIACRITIC_VERSE);

        restVerseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVerse.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVerse))
            )
            .andExpect(status().isOk());

        // Validate the Verse in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVerseUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedVerse, verse), getPersistedVerse(verse));
    }

    @Test
    void fullUpdateVerseWithPatch() throws Exception {
        // Initialize the database
        insertedVerse = verseRepository.save(verse);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the verse using partial update
        Verse partialUpdatedVerse = new Verse();
        partialUpdatedVerse.setId(verse.getId());

        partialUpdatedVerse.verse(UPDATED_VERSE).diacriticVerse(UPDATED_DIACRITIC_VERSE);

        restVerseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVerse.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVerse))
            )
            .andExpect(status().isOk());

        // Validate the Verse in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVerseUpdatableFieldsEquals(partialUpdatedVerse, getPersistedVerse(partialUpdatedVerse));
    }

    @Test
    void patchNonExistingVerse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        verse.setId(UUID.randomUUID().toString());

        // Create the Verse
        VerseDTO verseDTO = verseMapper.toDto(verse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVerseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, verseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(verseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Verse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchVerse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        verse.setId(UUID.randomUUID().toString());

        // Create the Verse
        VerseDTO verseDTO = verseMapper.toDto(verse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVerseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(verseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Verse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamVerse() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        verse.setId(UUID.randomUUID().toString());

        // Create the Verse
        VerseDTO verseDTO = verseMapper.toDto(verse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVerseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(verseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Verse in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteVerse() throws Exception {
        // Initialize the database
        insertedVerse = verseRepository.save(verse);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the verse
        restVerseMockMvc
            .perform(delete(ENTITY_API_URL_ID, verse.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return verseRepository.count();
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

    protected Verse getPersistedVerse(Verse verse) {
        return verseRepository.findById(verse.getId()).orElseThrow();
    }

    protected void assertPersistedVerseToMatchAllProperties(Verse expectedVerse) {
        assertVerseAllPropertiesEquals(expectedVerse, getPersistedVerse(expectedVerse));
    }

    protected void assertPersistedVerseToMatchUpdatableProperties(Verse expectedVerse) {
        assertVerseAllUpdatablePropertiesEquals(expectedVerse, getPersistedVerse(expectedVerse));
    }
}
