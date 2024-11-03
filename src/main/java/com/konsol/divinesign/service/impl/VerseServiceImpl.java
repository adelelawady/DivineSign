package com.konsol.divinesign.service.impl;

import com.konsol.divinesign.domain.Verse;
import com.konsol.divinesign.repository.VerseRepository;
import com.konsol.divinesign.service.VerseService;
import com.konsol.divinesign.service.dto.VerseDTO;
import com.konsol.divinesign.service.mapper.VerseMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.konsol.divinesign.domain.Verse}.
 */
@Service
public class VerseServiceImpl implements VerseService {

    private static final Logger LOG = LoggerFactory.getLogger(VerseServiceImpl.class);

    private final VerseRepository verseRepository;

    private final VerseMapper verseMapper;

    public VerseServiceImpl(VerseRepository verseRepository, VerseMapper verseMapper) {
        this.verseRepository = verseRepository;
        this.verseMapper = verseMapper;
    }

    @Override
    public VerseDTO save(VerseDTO verseDTO) {
        LOG.debug("Request to save Verse : {}", verseDTO);
        Verse verse = verseMapper.toEntity(verseDTO);
        verse = verseRepository.save(verse);
        return verseMapper.toDto(verse);
    }

    @Override
    public VerseDTO update(VerseDTO verseDTO) {
        LOG.debug("Request to update Verse : {}", verseDTO);
        Verse verse = verseMapper.toEntity(verseDTO);
        verse = verseRepository.save(verse);
        return verseMapper.toDto(verse);
    }

    @Override
    public Optional<VerseDTO> partialUpdate(VerseDTO verseDTO) {
        LOG.debug("Request to partially update Verse : {}", verseDTO);

        return verseRepository
            .findById(verseDTO.getId())
            .map(existingVerse -> {
                verseMapper.partialUpdate(existingVerse, verseDTO);

                return existingVerse;
            })
            .map(verseRepository::save)
            .map(verseMapper::toDto);
    }

    @Override
    public Page<VerseDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Verses");
        return verseRepository.findAll(pageable).map(verseMapper::toDto);
    }

    @Override
    public Optional<VerseDTO> findOne(String id) {
        LOG.debug("Request to get Verse : {}", id);
        return verseRepository.findById(id).map(verseMapper::toDto);
    }

    @Override
    public void delete(String id) {
        LOG.debug("Request to delete Verse : {}", id);
        verseRepository.deleteById(id);
    }
}
