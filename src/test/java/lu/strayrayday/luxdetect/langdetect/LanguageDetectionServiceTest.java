package lu.strayrayday.luxdetect.langdetect;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.junit.Test;

import lu.strayrayday.luxdetect.LanguageDetectionService;

public class LanguageDetectionServiceTest {

    private final LanguageDetectionService languageDetectionService = new LanguageDetectionService();

    @Test
    public void validate() throws IOException, URISyntaxException {

        validate("ENGLISH", Files.lines(Paths.get(ClassLoader.getSystemResource("english_validation.txt").toURI())));
        validate("GERMAN", Files.lines(Paths.get(ClassLoader.getSystemResource("german_validation.txt").toURI())));
        validate("FRENCH", Files.lines(Paths.get(ClassLoader.getSystemResource("french_validation.txt").toURI())));
        validate("DUTCH", Files.lines(Paths.get(ClassLoader.getSystemResource("dutch_validation.txt").toURI())));
        validate("LUXEMBOURGISH", Files.lines(Paths.get(ClassLoader.getSystemResource("lux_validation.txt").toURI())));

    }

    private void validate(String language, Stream<String> lines) {
        validate(language, lines, 0, Integer.MAX_VALUE);
    }

    private void validate(String language, Stream<String> lines, int minChars, int maxChars) {

        AtomicInteger positive = new AtomicInteger();
        AtomicInteger negative = new AtomicInteger();
        AtomicInteger total = new AtomicInteger();
        lines
                .map(line -> line.split("\t")[1])
                .filter(sentence -> sentence.length() > minChars && sentence.length() <= maxChars)
                .forEach(sentence -> {
                    String highestScoringLanguage = getHighestScoringLanguage(languageDetectionService.detectLanguages(sentence));
                    if (!highestScoringLanguage.equals(language)) {
                        negative.incrementAndGet();
                    } else {
                        positive.incrementAndGet();
                    }
                    total.incrementAndGet();
                });
        double percent = 100 * positive.get() * 1.0 / total.get();
        System.out.println(language + ": " + positive.get() + " positive, " + negative.get() + " negative => " + percent);

    }

    private String getHighestScoringLanguage(Map<String, Double> results) {

        double max = -Double.MAX_VALUE;
        String highestScoringLanguage = "";
        for (String language : results.keySet()) {
            if (results.get(language) > max) {
                max = results.get(language);
                highestScoringLanguage = language;
            }
        }

        return highestScoringLanguage;

    }

}