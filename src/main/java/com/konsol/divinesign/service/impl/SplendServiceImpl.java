package com.konsol.divinesign.service.impl;

import com.konsol.divinesign.domain.Splend;
import com.konsol.divinesign.repository.SplendRepository;
import com.konsol.divinesign.service.SplendService;
import com.konsol.divinesign.service.dto.SplendDTO;
import com.konsol.divinesign.service.mapper.SplendMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.konsol.divinesign.domain.Splend}.
 */
@Service
public class SplendServiceImpl implements SplendService {

    private static final Logger LOG = LoggerFactory.getLogger(SplendServiceImpl.class);

    private final SplendRepository splendRepository;

    private final SplendMapper splendMapper;

    public SplendServiceImpl(SplendRepository splendRepository, SplendMapper splendMapper) {
        this.splendRepository = splendRepository;
        this.splendMapper = splendMapper;
    }

    @Override
    public SplendDTO save(SplendDTO splendDTO) {
        LOG.debug("Request to save Splend : {}", splendDTO);
        Splend splend = splendMapper.toEntity(splendDTO);
        splend = splendRepository.save(splend);
        return splendMapper.toDto(splend);
    }

    @Override
    public SplendDTO update(SplendDTO splendDTO) {
        LOG.debug("Request to update Splend : {}", splendDTO);
        Splend splend = splendMapper.toEntity(splendDTO);
        splend = splendRepository.save(splend);
        return splendMapper.toDto(splend);
    }

    @Override
    public Optional<SplendDTO> partialUpdate(SplendDTO splendDTO) {
        LOG.debug("Request to partially update Splend : {}", splendDTO);

        return splendRepository
            .findById(splendDTO.getId())
            .map(existingSplend -> {
                splendMapper.partialUpdate(existingSplend, splendDTO);

                return existingSplend;
            })
            .map(splendRepository::save)
            .map(splendMapper::toDto);
    }

    @Override
    public Page<SplendDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Splends");
        return splendRepository.findAll(pageable).map(splendMapper::toDto);
    }

    public Page<SplendDTO> findAllWithEagerRelationships(Pageable pageable) {
        return splendRepository.findAllWithEagerRelationships(pageable).map(splendMapper::toDto);
    }

    @Override
    public Optional<SplendDTO> findOne(String id) {
        LOG.debug("Request to get Splend : {}", id);
        return splendRepository.findOneWithEagerRelationships(id).map(splendMapper::toDto);
    }

    @Override
    public void delete(String id) {
        LOG.debug("Request to delete Splend : {}", id);
        splendRepository.deleteById(id);
    }
}
