package com.konsol.divinesign.service.test;

import com.konsol.divinesign.domain.Verse;
import com.konsol.divinesign.repository.VerseRepository;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class tester implements CommandLineRunner {

    @Autowired
    VerseRepository verseRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void run(String... args) throws Exception {
        // String regexPattern = "(^|[^\\p{IsAlphabetic}\\p{IsDigit}])" + "يوم" + "($|[^\\p{IsAlphabetic}\\p{IsDigit}])";
        String regexPattern = "(?<!\\S)" + "دنيا" + "(?!\\S)";
        //String regexPattern = "\\b" + "يوم" + "\\b";
        List<Verse> verseList = verseRepository.findDiacriticVerseContaining("دنيا");

        AtomicInteger MonthSingleWordCount = new AtomicInteger();
        verseList.forEach(verse -> {
            MonthSingleWordCount.addAndGet(countOccurrences(removeDiacritics(verse.getDiacriticVerse())));
        });

        /* List<Verse> verseListDis= new ArrayList<>();
        verseList.forEach(verseFound->{
            if (verseListDis.stream().noneMatch(verse -> verse.getId().equals(verseFound.getId()))){
                verseList.add(verseFound);
            }
        });

*/

        System.out.println(MonthSingleWordCount);
        System.out.println("found Verses: " + verseList.size());
    }

    public static String removeDiacritics(String arabicText) {
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

    //NOT CONNECTED REGEX String regex = "\\bشهر\\b";

    public static int countOccurrences(String text) {
        // Define the regex pattern to match the word "شهر"
        String regex = "دنيا";

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
