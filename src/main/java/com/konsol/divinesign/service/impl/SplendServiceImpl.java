package com.konsol.divinesign.service.impl;

import com.konsol.divinesign.domain.*;
import com.konsol.divinesign.repository.CommentRepository;
import com.konsol.divinesign.repository.SplendRepository;
import com.konsol.divinesign.repository.SplendVariablesRepository;
import com.konsol.divinesign.service.SplendService;
import com.konsol.divinesign.service.TagService;
import com.konsol.divinesign.service.UserService;
import com.konsol.divinesign.service.VerseService;
import com.konsol.divinesign.service.api.dto.*;
import com.konsol.divinesign.service.dto.SplendDTO;
import com.konsol.divinesign.service.mapper.SplendMapper;
import com.konsol.divinesign.service.mapper.api.SplendPayloadMapper;
import com.konsol.divinesign.service.mapper.api.SplendVariablePayloadMapper;
import com.konsol.divinesign.service.mapper.api.VersePayloadMapper;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
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

    private final SplendPayloadMapper splendPayloadMapper;

    private final UserService userService;

    private final VerseService verseService;

    private final VersePayloadMapper versePayloadMapper;

    private final SplendVariablesRepository splendVariablesRepository;

    private final SplendVariablePayloadMapper splendVariablePayloadMapper;

    private final TagService tagService;
    private final CommentRepository commentRepository;

    public SplendServiceImpl(
        SplendRepository splendRepository,
        SplendMapper splendMapper,
        SplendPayloadMapper splendPayloadMapper,
        UserService userService,
        VerseService verseService,
        VersePayloadMapper versePayloadMapper,
        SplendVariablesRepository splendVariablesRepository,
        SplendVariablePayloadMapper splendVariablePayloadMapper,
        TagService tagService,
        CommentRepository commentRepository
    ) {
        this.splendRepository = splendRepository;
        this.splendMapper = splendMapper;
        this.splendPayloadMapper = splendPayloadMapper;
        this.userService = userService;
        this.verseService = verseService;
        this.versePayloadMapper = versePayloadMapper;
        this.splendVariablesRepository = splendVariablesRepository;
        this.splendVariablePayloadMapper = splendVariablePayloadMapper;
        this.tagService = tagService;
        this.commentRepository = commentRepository;
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
    public Optional<Splend> findOneDomainOwner(String id) {
        /**
         * Make sure he is the owner of the splend
         */
        User user = userService.getCurrentUser();
        Optional<Splend> splend = splendRepository.findById(id);
        if (splend.isPresent()) {
            Splend splend1 = splend.orElseThrow();
            if (splend1.getUser().getId().equals(user.getId())) {
                return splend;
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Splend> findOneDomainPublic(String id) {
        /**
         * Make sure he is the owner of the splend
         */
        return splendRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        LOG.debug("Request to delete Splend : {}", id);
        splendRepository.deleteById(id);
    }

    /**
     * @param createSplendPayload the payload to create a new splend
     * @return the created splend
     */
    @Override
    public SplendViewPayload createSplend(CreateSplend createSplendPayload) {
        /*
         * create new splend
         */
        Splend splend = new Splend().title(createSplendPayload.getTitle()).content(createSplendPayload.getContent());

        /*
         * add tags to splend
         */
        if (createSplendPayload.getTags() != null) {
            splend.setTags(new LinkedHashSet<>());
            createSplendPayload
                .getTags()
                .forEach(tag -> {
                    splend.getTags().add(tagService.getOrCreateTag(tag));
                });
        }

        /*
         * add user to splend
         */
        Splend savedSplend = splendRepository.save(splend);

        savedSplend.setUser(userService.getCurrentUser());

        savedSplend = splendRepository.save(savedSplend);

        return splendPayloadMapper.toDto(savedSplend);
    }

    @Override
    public SplendVariablePayload createSplendVariable(String splendId, CreateSplendVariablePayload createSplendVariablePayload) {
        Optional<Splend> splend = findOneDomainOwner(splendId);

        if (splend.isPresent()) {
            SplendVariables splendVariables = new SplendVariables();
            VerseSearchResultPayload verseSearchResultPayloadX = new VerseSearchResultPayload();
            List<Verse> foundVersesX = verseService.searchVersesByWord(createSplendVariablePayload.getSearch().getWordQuery());
            verseSearchResultPayloadX.setVersesCount(BigDecimal.valueOf(foundVersesX.size()));
            verseSearchResultPayloadX.setVerses(foundVersesX.stream().map(versePayloadMapper::toDto).toList());
            verseSearchResultPayloadX.setWordCount(
                BigDecimal.valueOf(
                    verseService.findWordOccurrencesInVerseList(foundVersesX, createSplendVariablePayload.getSearch().getRegexQuery())
                )
            );

            splendVariables.setVariables(new LinkedHashSet<>());

            VariablePayload variablePayload = new VariablePayload();
            if (createSplendVariablePayload.getVariableName() != null && createSplendVariablePayload.getVariableValue() != null) {
                variablePayload.setName(createSplendVariablePayload.getVariableName());
                variablePayload.setValue(createSplendVariablePayload.getVariableValue().toString());
            }

            splendVariables.setSplend(splend.get());
            splendVariables.setRegex(createSplendVariablePayload.getSearch().getRegexQuery());
            splendVariables.setWord(createSplendVariablePayload.getSearch().getWordQuery());
            splendVariables.setRegexCount(String.valueOf(verseSearchResultPayloadX.getWordCount()));
            splendVariables.setWordVerseCount(String.valueOf(verseSearchResultPayloadX.getVersesCount()));
            splendVariables.setVerses(new HashSet<>(foundVersesX));
            splendVariables = splendVariablesRepository.save(splendVariables);

            Splend splend1 = splend.get();
            splend1.getVariables().add(splendVariables);
            splendRepository.save(splend1);
            if (createSplendVariablePayload.getVariableName() != null && createSplendVariablePayload.getVariableValue() != null) {
                variablePayload.setVeriableId(splendVariables.getId());
                splendVariables.getVariables().add(variablePayload);
            }

            return splendVariablePayloadMapper.toDto(splendVariables);
        } else {
            throw new RuntimeException("Splend not found");
        }
    }

    @Override
    public List<SplendVariablePayload> getSplendVariablesByOwner(String splendId) {
        /**
         * Make sure he is the owner of the splend
         */
        User user = userService.getCurrentUser();
        Optional<Splend> splend = splendRepository.findById(splendId);
        if (splend.isPresent() && splend.get().getUser().getId().equals(user.getId())) {
            return splendVariablesRepository.findBySplendId(splendId).stream().map(splendVariablePayloadMapper::toDto).toList();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public SplendVariablePayload addVariableNameToVariable(String id, String variableId, VariablePayload variablePayload) {
        Optional<Splend> splend = findOneDomainOwner(id);

        if (splend.isPresent()) {
            Optional<SplendVariables> splendVariables = splendVariablesRepository.findByIdAndSplendId(variableId, id);

            if (splendVariables.isPresent()) {
                variablePayload.setVeriableId(variableId);
                splendVariables.get().getVariables().add(variablePayload);
                splendVariablesRepository.save(splendVariables.get());
                return splendVariablePayloadMapper.toDto(splendVariables.get());
            } else {
                throw new RuntimeException("Variable not found");
            }
        } else {
            throw new RuntimeException("Splend not found");
        }
    }

    @Override
    public SplendVariablePayload getSplendVariable(String id) {
        Optional<SplendVariables> splendVariables = splendVariablesRepository.findById(id);
        Optional<Splend> splend = findOneDomainOwner(splendVariables.get().getSplend().getId());

        if (splend.isPresent()) {
            if (splendVariables.isPresent()) {
                return splendVariablePayloadMapper.toDto(splendVariables.get());
            } else {
                throw new RuntimeException("Variable not found");
            }
        } else {
            throw new RuntimeException("Splend not found");
        }
    }

    @Override
    public void deleteSplendVariable(String id) {
        getSplendVariable(id);
        Optional<SplendVariables> splendVariables = splendVariablesRepository.findById(id);
        SplendVariables splendVariablesX = splendVariables.orElseThrow();
        Optional<Splend> splend = findOneDomainOwner(splendVariablesX.getSplend().getId());
        Splend splend1 = splend.orElseThrow();
        splend1.getVariables().removeIf(splendVariables1 -> splendVariables1.getId().equals(id));
        splendRepository.save(splend1);
        splendVariablesRepository.deleteById(id);
    }

    @Override
    public void deleteSplendVariableNameFromVariable(String id, VariablePayload variablePayload) {
        Optional<SplendVariables> splendVariables = splendVariablesRepository.findById(id);
        Optional<Splend> splend = findOneDomainOwner(splendVariables.orElseThrow().getSplend().getId());

        if (splend.isPresent()) {
            SplendVariables foundSplendVariables = splendVariables.get();
            foundSplendVariables.getVariables().removeIf(variable -> variable.getName().equals(variablePayload.getName()));
            splendVariablesRepository.save(foundSplendVariables);
        } else {
            throw new RuntimeException("Splend not found");
        }
    }

    @Override
    public void likeSplend(String splendId) {
        Optional<Splend> splend = findOneDomainPublic(splendId);

        if (splend.isEmpty()) {
            throw new RuntimeException("Splend not found");
        }

        User user = userService.getCurrentUser();

        Splend splendFound = splend.get();
        splendFound.getLikedUsers().add(user);
        splendFound.setLikes(splendFound.getLikedUsers().size());
        splendRepository.save(splendFound);
    }

    @Override
    public List<Comment> findCommentsBySplendId(String splendId) {
        return commentRepository.findAllBySplendId(splendId);
    }

    @Override
    public Comment createSplendComment(String splendId, CommentViewPayload commentViewPayload) {
        Optional<Splend> splend = findOneDomainPublic(splendId);

        if (splend.isEmpty()) {
            throw new RuntimeException("Splend not found");
        }

        User user = userService.getCurrentUser();
        Comment comment = new Comment();

        comment.setContent(commentViewPayload.getContent());
        comment.setUser(user);
        if (commentViewPayload.getParentComment() == null) {
            comment.setSplend(splend.get());
            comment.setLikes(0);
            comment = commentRepository.save(comment);
        } else {
            Optional<Comment> parentComment = commentRepository.findById(commentViewPayload.getParentComment());
            if (parentComment.isPresent()) {
                comment.setParents(new HashSet<>());
                comment.getParents().add(parentComment.get());
                comment.setSplend(null);
                comment.setLikes(0);
                comment = commentRepository.save(comment);
                Comment parentComment1 = parentComment.get();
                parentComment1.getComments().add(comment);
                commentRepository.save(parentComment1);
            }
        }

        Splend splend1 = splend.get();
        splend1.setCommentsCount(splend1.getCommentsCount() + 1);
        splendRepository.save(splend1);
        return commentRepository.save(comment);
    }

    @Override
    public List<Verse> getSplendVariableVerses(String id, String variableId) {
        Optional<SplendVariables> splendVariables = splendVariablesRepository.findById(variableId);
        Optional<Splend> splend = findOneDomainPublic(id);

        if (splend.isPresent()) {
            if (splendVariables.isPresent()) {
                return splendVariables.orElseThrow().getVerses().stream().toList();
            } else {
                throw new RuntimeException("Variable not found");
            }
        } else {
            throw new RuntimeException("Splend not found");
        }
    }
}
