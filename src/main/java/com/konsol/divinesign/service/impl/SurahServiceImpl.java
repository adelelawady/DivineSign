package com.konsol.divinesign.service.impl;

import com.konsol.divinesign.domain.Surah;
import com.konsol.divinesign.repository.SurahRepository;
import com.konsol.divinesign.service.SurahService;
import com.konsol.divinesign.service.dto.SurahDTO;
import com.konsol.divinesign.service.mapper.SurahMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.konsol.divinesign.domain.Surah}.
 */
@Service
public class SurahServiceImpl implements SurahService {

    private static final Logger LOG = LoggerFactory.getLogger(SurahServiceImpl.class);

    private final SurahRepository surahRepository;

    private final SurahMapper surahMapper;

    public SurahServiceImpl(SurahRepository surahRepository, SurahMapper surahMapper) {
        this.surahRepository = surahRepository;
        this.surahMapper = surahMapper;
    }

    @Override
    public SurahDTO save(SurahDTO surahDTO) {
        LOG.debug("Request to save Surah : {}", surahDTO);
        Surah surah = surahMapper.toEntity(surahDTO);
        surah = surahRepository.save(surah);
        return surahMapper.toDto(surah);
    }

    @Override
    public SurahDTO update(SurahDTO surahDTO) {
        LOG.debug("Request to update Surah : {}", surahDTO);
        Surah surah = surahMapper.toEntity(surahDTO);
        surah = surahRepository.save(surah);
        return surahMapper.toDto(surah);
    }

    @Override
    public Optional<SurahDTO> partialUpdate(SurahDTO surahDTO) {
        LOG.debug("Request to partially update Surah : {}", surahDTO);

        return surahRepository
            .findById(surahDTO.getId())
            .map(existingSurah -> {
                surahMapper.partialUpdate(existingSurah, surahDTO);

                return existingSurah;
            })
            .map(surahRepository::save)
            .map(surahMapper::toDto);
    }

    @Override
    public Page<SurahDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Surahs");
        return surahRepository.findAll(pageable).map(surahMapper::toDto);
    }

    @Override
    public Optional<SurahDTO> findOne(String id) {
        LOG.debug("Request to get Surah : {}", id);
        return surahRepository.findById(id).map(surahMapper::toDto);
    }

    @Override
    public void delete(String id) {
        LOG.debug("Request to delete Surah : {}", id);
        surahRepository.deleteById(id);
    }
}
