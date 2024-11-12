package com.konsol.divinesign.service.impl;

import com.konsol.divinesign.domain.Tag;
import com.konsol.divinesign.repository.TagRepository;
import com.konsol.divinesign.service.TagService;
import com.konsol.divinesign.service.dto.TagDTO;
import com.konsol.divinesign.service.mapper.TagMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.konsol.divinesign.domain.Tag}.
 */
@Service
public class TagServiceImpl implements TagService {

    private static final Logger LOG = LoggerFactory.getLogger(TagServiceImpl.class);

    private final TagRepository tagRepository;

    private final TagMapper tagMapper;

    public TagServiceImpl(TagRepository tagRepository, TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
    }

    @Override
    public TagDTO save(TagDTO tagDTO) {
        LOG.debug("Request to save Tag : {}", tagDTO);
        Tag tag = tagMapper.toEntity(tagDTO);
        tag = tagRepository.save(tag);
        return tagMapper.toDto(tag);
    }

    @Override
    public TagDTO update(TagDTO tagDTO) {
        LOG.debug("Request to update Tag : {}", tagDTO);
        Tag tag = tagMapper.toEntity(tagDTO);
        tag = tagRepository.save(tag);
        return tagMapper.toDto(tag);
    }

    @Override
    public Optional<TagDTO> partialUpdate(TagDTO tagDTO) {
        LOG.debug("Request to partially update Tag : {}", tagDTO);

        return tagRepository
            .findById(tagDTO.getId())
            .map(existingTag -> {
                tagMapper.partialUpdate(existingTag, tagDTO);

                return existingTag;
            })
            .map(tagRepository::save)
            .map(tagMapper::toDto);
    }

    @Override
    public Page<TagDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Tags");
        return tagRepository.findAll(pageable).map(tagMapper::toDto);
    }

    @Override
    public Optional<TagDTO> findOne(String id) {
        LOG.debug("Request to get Tag : {}", id);
        return tagRepository.findById(id).map(tagMapper::toDto);
    }

    @Override
    public void delete(String id) {
        LOG.debug("Request to delete Tag : {}", id);
        tagRepository.deleteById(id);
    }

    @Override
    public Tag getOrCreateTag(String tag) {
        Optional<Tag> foundTag = tagRepository.findByTitle(tag);
        if (foundTag.isPresent()) {
            return foundTag.orElseThrow();
        } else {
            Tag newTag = new Tag().title(tag);
            return tagRepository.save(newTag);
        }
    }
}
