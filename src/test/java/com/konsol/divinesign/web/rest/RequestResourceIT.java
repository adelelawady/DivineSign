package com.konsol.divinesign.web.rest;

import static com.konsol.divinesign.domain.RequestAsserts.*;
import static com.konsol.divinesign.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.konsol.divinesign.IntegrationTest;
import com.konsol.divinesign.domain.Request;
import com.konsol.divinesign.domain.enumeration.RequestStatus;
import com.konsol.divinesign.repository.RequestRepository;
import com.konsol.divinesign.repository.UserRepository;
import com.konsol.divinesign.service.dto.RequestDTO;
import com.konsol.divinesign.service.mapper.RequestMapper;
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
 * Integration tests for the {@link RequestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RequestResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_VERIFIED = false;
    private static final Boolean UPDATED_VERIFIED = true;

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final RequestStatus DEFAULT_STATUS = RequestStatus.PENDING;
    private static final RequestStatus UPDATED_STATUS = RequestStatus.APPROVED;

    private static final String ENTITY_API_URL = "/api/requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestMapper requestMapper;

    @Autowired
    private MockMvc restRequestMockMvc;

    private Request request;

    private Request insertedRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Request createEntity() {
        return new Request()
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .verified(DEFAULT_VERIFIED)
            .deleted(DEFAULT_DELETED)
            .status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Request createUpdatedEntity() {
        return new Request()
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .verified(UPDATED_VERIFIED)
            .deleted(UPDATED_DELETED)
            .status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        request = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedRequest != null) {
            requestRepository.delete(insertedRequest);
            insertedRequest = null;
        }
        userRepository.deleteAll();
    }

    @Test
    void createRequest() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Request
        RequestDTO requestDTO = requestMapper.toDto(request);
        var returnedRequestDTO = om.readValue(
            restRequestMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(requestDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RequestDTO.class
        );

        // Validate the Request in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRequest = requestMapper.toEntity(returnedRequestDTO);
        assertRequestUpdatableFieldsEquals(returnedRequest, getPersistedRequest(returnedRequest));

        insertedRequest = returnedRequest;
    }

    @Test
    void createRequestWithExistingId() throws Exception {
        // Create the Request with an existing ID
        request.setId("existing_id");
        RequestDTO requestDTO = requestMapper.toDto(request);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(requestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Request in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        request.setTitle(null);

        // Create the Request, which fails.
        RequestDTO requestDTO = requestMapper.toDto(request);

        restRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(requestDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkVerifiedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        request.setVerified(null);

        // Create the Request, which fails.
        RequestDTO requestDTO = requestMapper.toDto(request);

        restRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(requestDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllRequests() throws Exception {
        // Initialize the database
        insertedRequest = requestRepository.save(request);

        // Get all the requestList
        restRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(request.getId())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].verified").value(hasItem(DEFAULT_VERIFIED.booleanValue())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    void getRequest() throws Exception {
        // Initialize the database
        insertedRequest = requestRepository.save(request);

        // Get the request
        restRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, request.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(request.getId()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.verified").value(DEFAULT_VERIFIED.booleanValue()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    void getNonExistingRequest() throws Exception {
        // Get the request
        restRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingRequest() throws Exception {
        // Initialize the database
        insertedRequest = requestRepository.save(request);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the request
        Request updatedRequest = requestRepository.findById(request.getId()).orElseThrow();
        updatedRequest
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .verified(UPDATED_VERIFIED)
            .deleted(UPDATED_DELETED)
            .status(UPDATED_STATUS);
        RequestDTO requestDTO = requestMapper.toDto(updatedRequest);

        restRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, requestDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(requestDTO))
            )
            .andExpect(status().isOk());

        // Validate the Request in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRequestToMatchAllProperties(updatedRequest);
    }

    @Test
    void putNonExistingRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        request.setId(UUID.randomUUID().toString());

        // Create the Request
        RequestDTO requestDTO = requestMapper.toDto(request);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, requestDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(requestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Request in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        request.setId(UUID.randomUUID().toString());

        // Create the Request
        RequestDTO requestDTO = requestMapper.toDto(request);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(requestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Request in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        request.setId(UUID.randomUUID().toString());

        // Create the Request
        RequestDTO requestDTO = requestMapper.toDto(request);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(requestDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Request in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateRequestWithPatch() throws Exception {
        // Initialize the database
        insertedRequest = requestRepository.save(request);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the request using partial update
        Request partialUpdatedRequest = new Request();
        partialUpdatedRequest.setId(request.getId());

        partialUpdatedRequest
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .verified(UPDATED_VERIFIED)
            .deleted(UPDATED_DELETED)
            .status(UPDATED_STATUS);

        restRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRequest))
            )
            .andExpect(status().isOk());

        // Validate the Request in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRequestUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedRequest, request), getPersistedRequest(request));
    }

    @Test
    void fullUpdateRequestWithPatch() throws Exception {
        // Initialize the database
        insertedRequest = requestRepository.save(request);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the request using partial update
        Request partialUpdatedRequest = new Request();
        partialUpdatedRequest.setId(request.getId());

        partialUpdatedRequest
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .verified(UPDATED_VERIFIED)
            .deleted(UPDATED_DELETED)
            .status(UPDATED_STATUS);

        restRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRequest))
            )
            .andExpect(status().isOk());

        // Validate the Request in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRequestUpdatableFieldsEquals(partialUpdatedRequest, getPersistedRequest(partialUpdatedRequest));
    }

    @Test
    void patchNonExistingRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        request.setId(UUID.randomUUID().toString());

        // Create the Request
        RequestDTO requestDTO = requestMapper.toDto(request);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, requestDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(requestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Request in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        request.setId(UUID.randomUUID().toString());

        // Create the Request
        RequestDTO requestDTO = requestMapper.toDto(request);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(requestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Request in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        request.setId(UUID.randomUUID().toString());

        // Create the Request
        RequestDTO requestDTO = requestMapper.toDto(request);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(requestDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Request in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteRequest() throws Exception {
        // Initialize the database
        insertedRequest = requestRepository.save(request);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the request
        restRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, request.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return requestRepository.count();
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

    protected Request getPersistedRequest(Request request) {
        return requestRepository.findById(request.getId()).orElseThrow();
    }

    protected void assertPersistedRequestToMatchAllProperties(Request expectedRequest) {
        assertRequestAllPropertiesEquals(expectedRequest, getPersistedRequest(expectedRequest));
    }

    protected void assertPersistedRequestToMatchUpdatableProperties(Request expectedRequest) {
        assertRequestAllUpdatablePropertiesEquals(expectedRequest, getPersistedRequest(expectedRequest));
    }
}
