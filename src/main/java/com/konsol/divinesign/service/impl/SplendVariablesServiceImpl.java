package com.konsol.divinesign.service.impl;

import com.konsol.divinesign.domain.SplendVariables;
import com.konsol.divinesign.repository.SplendVariablesRepository;
import com.konsol.divinesign.service.SplendVariablesService;
import com.konsol.divinesign.service.dto.SplendVersesDTO;
import com.konsol.divinesign.service.mapper.SplendVariablesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link SplendVariables}.
 */
@Service
public class SplendVariablesServiceImpl implements SplendVariablesService {

    private static final Logger LOG = LoggerFactory.getLogger(SplendVariablesServiceImpl.class);

    private final SplendVariablesRepository splendVariablesRepository;

    private final SplendVariablesMapper splendVariablesMapper;

    public SplendVariablesServiceImpl(SplendVariablesRepository splendVariablesRepository, SplendVariablesMapper splendVariablesMapper) {
        this.splendVariablesRepository = splendVariablesRepository;
        this.splendVariablesMapper = splendVariablesMapper;
    }

    @Override
    public SplendVersesDTO save(SplendVersesDTO splendVersesDTO) {
        LOG.debug("Request to save SplendVerses : {}", splendVersesDTO);
        SplendVariables splendVariables = splendVariablesMapper.toEntity(splendVersesDTO);
        splendVariables = splendVariablesRepository.save(splendVariables);
        return splendVariablesMapper.toDto(splendVariables);
    }

    @Override
    public SplendVersesDTO update(SplendVersesDTO splendVersesDTO) {
        LOG.debug("Request to update SplendVerses : {}", splendVersesDTO);
        SplendVariables splendVariables = splendVariablesMapper.toEntity(splendVersesDTO);
        splendVariables = splendVariablesRepository.save(splendVariables);
        return splendVariablesMapper.toDto(splendVariables);
    }

    @Override
    public Optional<SplendVersesDTO> partialUpdate(SplendVersesDTO splendVersesDTO) {
        LOG.debug("Request to partially update SplendVerses : {}", splendVersesDTO);

        return splendVariablesRepository
            .findById(splendVersesDTO.getId())
            .map(existingSplendVerses -> {
                splendVariablesMapper.partialUpdate(existingSplendVerses, splendVersesDTO);

                return existingSplendVerses;
            })
            .map(splendVariablesRepository::save)
            .map(splendVariablesMapper::toDto);
    }

    @Override
    public Page<SplendVersesDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all SplendVerses");
        return splendVariablesRepository.findAll(pageable).map(splendVariablesMapper::toDto);
    }

    @Override
    public Optional<SplendVersesDTO> findOne(String id) {
        LOG.debug("Request to get SplendVerses : {}", id);
        return splendVariablesRepository.findById(id).map(splendVariablesMapper::toDto);
    }

    @Override
    public void delete(String id) {
        LOG.debug("Request to delete SplendVerses : {}", id);
        splendVariablesRepository.deleteById(id);
    }
}
