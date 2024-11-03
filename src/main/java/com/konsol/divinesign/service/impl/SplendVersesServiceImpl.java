package com.konsol.divinesign.service.impl;

import com.konsol.divinesign.domain.SplendVerses;
import com.konsol.divinesign.repository.SplendVersesRepository;
import com.konsol.divinesign.service.SplendVersesService;
import com.konsol.divinesign.service.dto.SplendVersesDTO;
import com.konsol.divinesign.service.mapper.SplendVersesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.konsol.divinesign.domain.SplendVerses}.
 */
@Service
public class SplendVersesServiceImpl implements SplendVersesService {

    private static final Logger LOG = LoggerFactory.getLogger(SplendVersesServiceImpl.class);

    private final SplendVersesRepository splendVersesRepository;

    private final SplendVersesMapper splendVersesMapper;

    public SplendVersesServiceImpl(SplendVersesRepository splendVersesRepository, SplendVersesMapper splendVersesMapper) {
        this.splendVersesRepository = splendVersesRepository;
        this.splendVersesMapper = splendVersesMapper;
    }

    @Override
    public SplendVersesDTO save(SplendVersesDTO splendVersesDTO) {
        LOG.debug("Request to save SplendVerses : {}", splendVersesDTO);
        SplendVerses splendVerses = splendVersesMapper.toEntity(splendVersesDTO);
        splendVerses = splendVersesRepository.save(splendVerses);
        return splendVersesMapper.toDto(splendVerses);
    }

    @Override
    public SplendVersesDTO update(SplendVersesDTO splendVersesDTO) {
        LOG.debug("Request to update SplendVerses : {}", splendVersesDTO);
        SplendVerses splendVerses = splendVersesMapper.toEntity(splendVersesDTO);
        splendVerses = splendVersesRepository.save(splendVerses);
        return splendVersesMapper.toDto(splendVerses);
    }

    @Override
    public Optional<SplendVersesDTO> partialUpdate(SplendVersesDTO splendVersesDTO) {
        LOG.debug("Request to partially update SplendVerses : {}", splendVersesDTO);

        return splendVersesRepository
            .findById(splendVersesDTO.getId())
            .map(existingSplendVerses -> {
                splendVersesMapper.partialUpdate(existingSplendVerses, splendVersesDTO);

                return existingSplendVerses;
            })
            .map(splendVersesRepository::save)
            .map(splendVersesMapper::toDto);
    }

    @Override
    public Page<SplendVersesDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all SplendVerses");
        return splendVersesRepository.findAll(pageable).map(splendVersesMapper::toDto);
    }

    @Override
    public Optional<SplendVersesDTO> findOne(String id) {
        LOG.debug("Request to get SplendVerses : {}", id);
        return splendVersesRepository.findById(id).map(splendVersesMapper::toDto);
    }

    @Override
    public void delete(String id) {
        LOG.debug("Request to delete SplendVerses : {}", id);
        splendVersesRepository.deleteById(id);
    }
}
