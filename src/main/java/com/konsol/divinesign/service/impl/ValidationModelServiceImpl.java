package com.konsol.divinesign.service.impl;

import com.konsol.divinesign.domain.ValidationModel;
import com.konsol.divinesign.repository.ValidationModelRepository;
import com.konsol.divinesign.service.ValidationModelService;
import com.konsol.divinesign.service.dto.ValidationModelDTO;
import com.konsol.divinesign.service.mapper.ValidationModelMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.konsol.divinesign.domain.ValidationModel}.
 */
@Service
public class ValidationModelServiceImpl implements ValidationModelService {

    private static final Logger LOG = LoggerFactory.getLogger(ValidationModelServiceImpl.class);

    private final ValidationModelRepository validationModelRepository;

    private final ValidationModelMapper validationModelMapper;

    public ValidationModelServiceImpl(ValidationModelRepository validationModelRepository, ValidationModelMapper validationModelMapper) {
        this.validationModelRepository = validationModelRepository;
        this.validationModelMapper = validationModelMapper;
    }

    @Override
    public ValidationModelDTO save(ValidationModelDTO validationModelDTO) {
        LOG.debug("Request to save ValidationModel : {}", validationModelDTO);
        ValidationModel validationModel = validationModelMapper.toEntity(validationModelDTO);
        validationModel = validationModelRepository.save(validationModel);
        return validationModelMapper.toDto(validationModel);
    }

    @Override
    public ValidationModelDTO update(ValidationModelDTO validationModelDTO) {
        LOG.debug("Request to update ValidationModel : {}", validationModelDTO);
        ValidationModel validationModel = validationModelMapper.toEntity(validationModelDTO);
        validationModel = validationModelRepository.save(validationModel);
        return validationModelMapper.toDto(validationModel);
    }

    @Override
    public Optional<ValidationModelDTO> partialUpdate(ValidationModelDTO validationModelDTO) {
        LOG.debug("Request to partially update ValidationModel : {}", validationModelDTO);

        return validationModelRepository
            .findById(validationModelDTO.getId())
            .map(existingValidationModel -> {
                validationModelMapper.partialUpdate(existingValidationModel, validationModelDTO);

                return existingValidationModel;
            })
            .map(validationModelRepository::save)
            .map(validationModelMapper::toDto);
    }

    @Override
    public Page<ValidationModelDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ValidationModels");
        return validationModelRepository.findAll(pageable).map(validationModelMapper::toDto);
    }

    @Override
    public Optional<ValidationModelDTO> findOne(String id) {
        LOG.debug("Request to get ValidationModel : {}", id);
        return validationModelRepository.findById(id).map(validationModelMapper::toDto);
    }

    @Override
    public void delete(String id) {
        LOG.debug("Request to delete ValidationModel : {}", id);
        validationModelRepository.deleteById(id);
    }
}
