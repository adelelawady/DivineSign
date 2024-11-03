package com.konsol.divinesign.web.rest;

import static com.konsol.divinesign.domain.ValidationModelAsserts.*;
import static com.konsol.divinesign.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.konsol.divinesign.IntegrationTest;
import com.konsol.divinesign.domain.ValidationModel;
import com.konsol.divinesign.repository.ValidationModelRepository;
import com.konsol.divinesign.service.dto.ValidationModelDTO;
import com.konsol.divinesign.service.mapper.ValidationModelMapper;
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
 * Integration tests for the {@link ValidationModelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ValidationModelResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/validation-models";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ValidationModelRepository validationModelRepository;

    @Autowired
    private ValidationModelMapper validationModelMapper;

    @Autowired
    private MockMvc restValidationModelMockMvc;

    private ValidationModel validationModel;

    private ValidationModel insertedValidationModel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ValidationModel createEntity() {
        return new ValidationModel().name(DEFAULT_NAME).type(DEFAULT_TYPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ValidationModel createUpdatedEntity() {
        return new ValidationModel().name(UPDATED_NAME).type(UPDATED_TYPE);
    }

    @BeforeEach
    public void initTest() {
        validationModel = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedValidationModel != null) {
            validationModelRepository.delete(insertedValidationModel);
            insertedValidationModel = null;
        }
    }

    @Test
    void createValidationModel() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ValidationModel
        ValidationModelDTO validationModelDTO = validationModelMapper.toDto(validationModel);
        var returnedValidationModelDTO = om.readValue(
            restValidationModelMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(validationModelDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ValidationModelDTO.class
        );

        // Validate the ValidationModel in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedValidationModel = validationModelMapper.toEntity(returnedValidationModelDTO);
        assertValidationModelUpdatableFieldsEquals(returnedValidationModel, getPersistedValidationModel(returnedValidationModel));

        insertedValidationModel = returnedValidationModel;
    }

    @Test
    void createValidationModelWithExistingId() throws Exception {
        // Create the ValidationModel with an existing ID
        validationModel.setId("existing_id");
        ValidationModelDTO validationModelDTO = validationModelMapper.toDto(validationModel);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restValidationModelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(validationModelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ValidationModel in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllValidationModels() throws Exception {
        // Initialize the database
        insertedValidationModel = validationModelRepository.save(validationModel);

        // Get all the validationModelList
        restValidationModelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(validationModel.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }

    @Test
    void getValidationModel() throws Exception {
        // Initialize the database
        insertedValidationModel = validationModelRepository.save(validationModel);

        // Get the validationModel
        restValidationModelMockMvc
            .perform(get(ENTITY_API_URL_ID, validationModel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(validationModel.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    void getNonExistingValidationModel() throws Exception {
        // Get the validationModel
        restValidationModelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingValidationModel() throws Exception {
        // Initialize the database
        insertedValidationModel = validationModelRepository.save(validationModel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the validationModel
        ValidationModel updatedValidationModel = validationModelRepository.findById(validationModel.getId()).orElseThrow();
        updatedValidationModel.name(UPDATED_NAME).type(UPDATED_TYPE);
        ValidationModelDTO validationModelDTO = validationModelMapper.toDto(updatedValidationModel);

        restValidationModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, validationModelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(validationModelDTO))
            )
            .andExpect(status().isOk());

        // Validate the ValidationModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedValidationModelToMatchAllProperties(updatedValidationModel);
    }

    @Test
    void putNonExistingValidationModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        validationModel.setId(UUID.randomUUID().toString());

        // Create the ValidationModel
        ValidationModelDTO validationModelDTO = validationModelMapper.toDto(validationModel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restValidationModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, validationModelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(validationModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ValidationModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchValidationModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        validationModel.setId(UUID.randomUUID().toString());

        // Create the ValidationModel
        ValidationModelDTO validationModelDTO = validationModelMapper.toDto(validationModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValidationModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(validationModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ValidationModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamValidationModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        validationModel.setId(UUID.randomUUID().toString());

        // Create the ValidationModel
        ValidationModelDTO validationModelDTO = validationModelMapper.toDto(validationModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValidationModelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(validationModelDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ValidationModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateValidationModelWithPatch() throws Exception {
        // Initialize the database
        insertedValidationModel = validationModelRepository.save(validationModel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the validationModel using partial update
        ValidationModel partialUpdatedValidationModel = new ValidationModel();
        partialUpdatedValidationModel.setId(validationModel.getId());

        partialUpdatedValidationModel.name(UPDATED_NAME);

        restValidationModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedValidationModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedValidationModel))
            )
            .andExpect(status().isOk());

        // Validate the ValidationModel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertValidationModelUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedValidationModel, validationModel),
            getPersistedValidationModel(validationModel)
        );
    }

    @Test
    void fullUpdateValidationModelWithPatch() throws Exception {
        // Initialize the database
        insertedValidationModel = validationModelRepository.save(validationModel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the validationModel using partial update
        ValidationModel partialUpdatedValidationModel = new ValidationModel();
        partialUpdatedValidationModel.setId(validationModel.getId());

        partialUpdatedValidationModel.name(UPDATED_NAME).type(UPDATED_TYPE);

        restValidationModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedValidationModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedValidationModel))
            )
            .andExpect(status().isOk());

        // Validate the ValidationModel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertValidationModelUpdatableFieldsEquals(
            partialUpdatedValidationModel,
            getPersistedValidationModel(partialUpdatedValidationModel)
        );
    }

    @Test
    void patchNonExistingValidationModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        validationModel.setId(UUID.randomUUID().toString());

        // Create the ValidationModel
        ValidationModelDTO validationModelDTO = validationModelMapper.toDto(validationModel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restValidationModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, validationModelDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(validationModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ValidationModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchValidationModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        validationModel.setId(UUID.randomUUID().toString());

        // Create the ValidationModel
        ValidationModelDTO validationModelDTO = validationModelMapper.toDto(validationModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValidationModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(validationModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ValidationModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamValidationModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        validationModel.setId(UUID.randomUUID().toString());

        // Create the ValidationModel
        ValidationModelDTO validationModelDTO = validationModelMapper.toDto(validationModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValidationModelMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(validationModelDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ValidationModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteValidationModel() throws Exception {
        // Initialize the database
        insertedValidationModel = validationModelRepository.save(validationModel);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the validationModel
        restValidationModelMockMvc
            .perform(delete(ENTITY_API_URL_ID, validationModel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return validationModelRepository.count();
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

    protected ValidationModel getPersistedValidationModel(ValidationModel validationModel) {
        return validationModelRepository.findById(validationModel.getId()).orElseThrow();
    }

    protected void assertPersistedValidationModelToMatchAllProperties(ValidationModel expectedValidationModel) {
        assertValidationModelAllPropertiesEquals(expectedValidationModel, getPersistedValidationModel(expectedValidationModel));
    }

    protected void assertPersistedValidationModelToMatchUpdatableProperties(ValidationModel expectedValidationModel) {
        assertValidationModelAllUpdatablePropertiesEquals(expectedValidationModel, getPersistedValidationModel(expectedValidationModel));
    }
}
