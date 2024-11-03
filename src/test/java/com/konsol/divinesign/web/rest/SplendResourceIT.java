package com.konsol.divinesign.web.rest;

import static com.konsol.divinesign.domain.SplendAsserts.*;
import static com.konsol.divinesign.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.konsol.divinesign.IntegrationTest;
import com.konsol.divinesign.domain.Splend;
import com.konsol.divinesign.repository.SplendRepository;
import com.konsol.divinesign.repository.UserRepository;
import com.konsol.divinesign.service.SplendService;
import com.konsol.divinesign.service.dto.SplendDTO;
import com.konsol.divinesign.service.mapper.SplendMapper;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link SplendResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SplendResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Integer DEFAULT_LIKES = 1;
    private static final Integer UPDATED_LIKES = 2;

    private static final Integer DEFAULT_DISLIKES = 1;
    private static final Integer UPDATED_DISLIKES = 2;

    private static final Boolean DEFAULT_VERIFIED = false;
    private static final Boolean UPDATED_VERIFIED = true;

    private static final String ENTITY_API_URL = "/api/splends";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SplendRepository splendRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private SplendRepository splendRepositoryMock;

    @Autowired
    private SplendMapper splendMapper;

    @Mock
    private SplendService splendServiceMock;

    @Autowired
    private MockMvc restSplendMockMvc;

    private Splend splend;

    private Splend insertedSplend;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Splend createEntity() {
        return new Splend()
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .likes(DEFAULT_LIKES)
            .dislikes(DEFAULT_DISLIKES)
            .verified(DEFAULT_VERIFIED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Splend createUpdatedEntity() {
        return new Splend()
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .likes(UPDATED_LIKES)
            .dislikes(UPDATED_DISLIKES)
            .verified(UPDATED_VERIFIED);
    }

    @BeforeEach
    public void initTest() {
        splend = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSplend != null) {
            splendRepository.delete(insertedSplend);
            insertedSplend = null;
        }
        userRepository.deleteAll();
    }

    @Test
    void createSplend() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Splend
        SplendDTO splendDTO = splendMapper.toDto(splend);
        var returnedSplendDTO = om.readValue(
            restSplendMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(splendDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SplendDTO.class
        );

        // Validate the Splend in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSplend = splendMapper.toEntity(returnedSplendDTO);
        assertSplendUpdatableFieldsEquals(returnedSplend, getPersistedSplend(returnedSplend));

        insertedSplend = returnedSplend;
    }

    @Test
    void createSplendWithExistingId() throws Exception {
        // Create the Splend with an existing ID
        splend.setId("existing_id");
        SplendDTO splendDTO = splendMapper.toDto(splend);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSplendMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(splendDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Splend in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        splend.setTitle(null);

        // Create the Splend, which fails.
        SplendDTO splendDTO = splendMapper.toDto(splend);

        restSplendMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(splendDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkLikesIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        splend.setLikes(null);

        // Create the Splend, which fails.
        SplendDTO splendDTO = splendMapper.toDto(splend);

        restSplendMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(splendDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkDislikesIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        splend.setDislikes(null);

        // Create the Splend, which fails.
        SplendDTO splendDTO = splendMapper.toDto(splend);

        restSplendMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(splendDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkVerifiedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        splend.setVerified(null);

        // Create the Splend, which fails.
        SplendDTO splendDTO = splendMapper.toDto(splend);

        restSplendMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(splendDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllSplends() throws Exception {
        // Initialize the database
        insertedSplend = splendRepository.save(splend);

        // Get all the splendList
        restSplendMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(splend.getId())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].likes").value(hasItem(DEFAULT_LIKES)))
            .andExpect(jsonPath("$.[*].dislikes").value(hasItem(DEFAULT_DISLIKES)))
            .andExpect(jsonPath("$.[*].verified").value(hasItem(DEFAULT_VERIFIED.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSplendsWithEagerRelationshipsIsEnabled() throws Exception {
        when(splendServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSplendMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(splendServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSplendsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(splendServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSplendMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(splendRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getSplend() throws Exception {
        // Initialize the database
        insertedSplend = splendRepository.save(splend);

        // Get the splend
        restSplendMockMvc
            .perform(get(ENTITY_API_URL_ID, splend.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(splend.getId()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.likes").value(DEFAULT_LIKES))
            .andExpect(jsonPath("$.dislikes").value(DEFAULT_DISLIKES))
            .andExpect(jsonPath("$.verified").value(DEFAULT_VERIFIED.booleanValue()));
    }

    @Test
    void getNonExistingSplend() throws Exception {
        // Get the splend
        restSplendMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingSplend() throws Exception {
        // Initialize the database
        insertedSplend = splendRepository.save(splend);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the splend
        Splend updatedSplend = splendRepository.findById(splend.getId()).orElseThrow();
        updatedSplend
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .likes(UPDATED_LIKES)
            .dislikes(UPDATED_DISLIKES)
            .verified(UPDATED_VERIFIED);
        SplendDTO splendDTO = splendMapper.toDto(updatedSplend);

        restSplendMockMvc
            .perform(
                put(ENTITY_API_URL_ID, splendDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(splendDTO))
            )
            .andExpect(status().isOk());

        // Validate the Splend in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSplendToMatchAllProperties(updatedSplend);
    }

    @Test
    void putNonExistingSplend() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        splend.setId(UUID.randomUUID().toString());

        // Create the Splend
        SplendDTO splendDTO = splendMapper.toDto(splend);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSplendMockMvc
            .perform(
                put(ENTITY_API_URL_ID, splendDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(splendDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Splend in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSplend() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        splend.setId(UUID.randomUUID().toString());

        // Create the Splend
        SplendDTO splendDTO = splendMapper.toDto(splend);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSplendMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(splendDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Splend in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSplend() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        splend.setId(UUID.randomUUID().toString());

        // Create the Splend
        SplendDTO splendDTO = splendMapper.toDto(splend);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSplendMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(splendDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Splend in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSplendWithPatch() throws Exception {
        // Initialize the database
        insertedSplend = splendRepository.save(splend);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the splend using partial update
        Splend partialUpdatedSplend = new Splend();
        partialUpdatedSplend.setId(splend.getId());

        partialUpdatedSplend.content(UPDATED_CONTENT).likes(UPDATED_LIKES);

        restSplendMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSplend.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSplend))
            )
            .andExpect(status().isOk());

        // Validate the Splend in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSplendUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSplend, splend), getPersistedSplend(splend));
    }

    @Test
    void fullUpdateSplendWithPatch() throws Exception {
        // Initialize the database
        insertedSplend = splendRepository.save(splend);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the splend using partial update
        Splend partialUpdatedSplend = new Splend();
        partialUpdatedSplend.setId(splend.getId());

        partialUpdatedSplend
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .likes(UPDATED_LIKES)
            .dislikes(UPDATED_DISLIKES)
            .verified(UPDATED_VERIFIED);

        restSplendMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSplend.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSplend))
            )
            .andExpect(status().isOk());

        // Validate the Splend in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSplendUpdatableFieldsEquals(partialUpdatedSplend, getPersistedSplend(partialUpdatedSplend));
    }

    @Test
    void patchNonExistingSplend() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        splend.setId(UUID.randomUUID().toString());

        // Create the Splend
        SplendDTO splendDTO = splendMapper.toDto(splend);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSplendMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, splendDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(splendDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Splend in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSplend() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        splend.setId(UUID.randomUUID().toString());

        // Create the Splend
        SplendDTO splendDTO = splendMapper.toDto(splend);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSplendMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(splendDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Splend in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSplend() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        splend.setId(UUID.randomUUID().toString());

        // Create the Splend
        SplendDTO splendDTO = splendMapper.toDto(splend);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSplendMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(splendDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Splend in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSplend() throws Exception {
        // Initialize the database
        insertedSplend = splendRepository.save(splend);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the splend
        restSplendMockMvc
            .perform(delete(ENTITY_API_URL_ID, splend.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return splendRepository.count();
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

    protected Splend getPersistedSplend(Splend splend) {
        return splendRepository.findById(splend.getId()).orElseThrow();
    }

    protected void assertPersistedSplendToMatchAllProperties(Splend expectedSplend) {
        assertSplendAllPropertiesEquals(expectedSplend, getPersistedSplend(expectedSplend));
    }

    protected void assertPersistedSplendToMatchUpdatableProperties(Splend expectedSplend) {
        assertSplendAllUpdatablePropertiesEquals(expectedSplend, getPersistedSplend(expectedSplend));
    }
}
