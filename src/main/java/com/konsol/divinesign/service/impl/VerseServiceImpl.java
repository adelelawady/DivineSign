package com.konsol.divinesign.service.impl;

import com.konsol.divinesign.domain.Verse;
import com.konsol.divinesign.repository.VerseRepository;
import com.konsol.divinesign.service.VerseService;
import com.konsol.divinesign.service.dto.VerseDTO;
import com.konsol.divinesign.service.mapper.VerseMapper;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    @Override
    public synchronized List<Verse> searchVersesByWord(String word) {
        return verseRepository.findDiacriticVerseContaining(word);
    }

    @Override
    public int findWordOccurrencesInVerse(Verse verse, String word) {
        return countOccurrences(removeDiacritics(verse.getDiacriticVerse()), word);
    }

    @Override
    public int findWordOccurrencesInVerseList(List<Verse> verse, String word) {
        int wordOccurrencesInVerseList = 0;
        for (Verse v : verse) {
            wordOccurrencesInVerseList += findWordOccurrencesInVerse(v, word);
        }
        return wordOccurrencesInVerseList;
    }

    private String removeDiacritics(String arabicText) {
        // Arabic diacritic characters (harakat)
        String diacritics =
            "\u0610\u0611\u0612\u0613\u0614\u0615\u0616\u0617" +
            "\u0618\u0619\u061A\u061B\u061C\u061D\u061E\u061F" +
            "\u0620\u064B\u064C\u064D\u064E\u064F\u0650\u0651" +
            "\u0652\u0653\u0654\u0655\u0656\u0657\u0658\u0659" +
            "\u065A\u065B\u065C\u065D\u065E\u065F\u0670\u0671" +
            "\u0672\u0673\u0674\u0675\u0676\u0677\u0678\u0679" +
            "\u067A\u067B\u067C\u067D\u067E\u067F\u0680\u0681" +
            "\u0682\u0683\u0684\u0685\u0686\u0687\u0688\u0689" +
            "\u068A\u068B\u068C\u068D\u068E\u068F\u0690\u0691" +
            "\u0692\u0693\u0694\u0695\u0696\u0697\u0698\u0699" +
            "\u06A0\u06A1\u06A2\u06A3\u06A4\u06A5\u06A6\u06A7" +
            "\u06A8\u06A9\u06AA\u06AB\u06AC\u06AD\u06AE\u06AF" +
            "\u06B0\u06B1\u06B2\u06B3\u06B4\u06B5\u06B6\u06B7" +
            "\u06B8\u06B9\u06BA\u06BB\u06BC\u06BD\u06BE\u06BF" +
            "\u06C0\u06C1\u06C2\u06C3\u06C4\u06C5\u06C6\u06C7" +
            "\u06C8\u06C9\u06CA\u06CB\u06CC\u06CD\u06CE\u06CF" +
            "\u06D0\u06D1\u06D2\u06D3\u06D4\u06D5\u06D6\u06D7" +
            "\u06D8\u06D9\u06DA\u06DB\u06DC\u06DD\u06DE\u06DF";

        // Use StringBuilder for efficiency
        StringBuilder result = new StringBuilder();

        // Iterate through each character in the input string
        for (char ch : arabicText.toCharArray()) {
            // If the character is not a diacritic, append it to the result
            if (diacritics.indexOf(ch) == -1) {
                result.append(ch);
            }
        }

        return result.toString();
    }

    private int countOccurrences(String text, String regex) {
        // Define the regex pattern to match the word "شهر"

        // Compile the pattern
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        // Count occurrences
        int count = 0;
        while (matcher.find()) {
            count++;
        }

        return count;
    }
}
