package lu.strayrayday.luxdetect;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.MapUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import lu.strayrayday.luxdetect.langdetect.TextUtils;
import lu.strayrayday.luxdetect.model.LanguageProfile;

@Slf4j
@Service
public class LanguageDetectionService {

    private static final Set<String> SUPPORTED_LANGUAGES = Set.of("LUXEMBOURGISH", "GERMAN", "FRENCH", "DUTCH", "ENGLISH");

    private final Map<String, Map<String, Double>> languageNgramScores = new HashMap<>();
    private final Map<String, Double> backOffScores = new HashMap<>();

    public Map<String, Double> detectLanguages(String text) {

        if (MapUtils.isEmpty(languageNgramScores)) {
            loadLanguageProfilesFromClasspath();
        }

        Map<String, Double> scoredLanguages = new HashMap<>();
        TextUtils.get5Grams(text).forEach(ngram -> {
            languageNgramScores.keySet()
                    .forEach(k -> {
                        Double score = languageNgramScores.get(k).getOrDefault(ngram, backOffScores.get(k));
                        scoredLanguages.put(k, scoredLanguages.getOrDefault(k, 0d) + score);
                    });
        });

        return scoredLanguages;

    }

    private void loadLanguageProfilesFromClasspath() {

        try {
            for (String lang : SUPPORTED_LANGUAGES) {
                InputStream resource = new ClassPathResource(lang + ".json").getInputStream();
                ObjectMapper mapper = new ObjectMapper();
                LanguageProfile languageProfile = mapper.readValue(resource, LanguageProfile.class);
                languageNgramScores.put(languageProfile.getLanguage(), getInvertedLanguageProfile(languageProfile));
                backOffScores.put(languageProfile.getLanguage(), Math.log(1d/languageProfile.getSamples()));
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

    }

    private Map<String, Double> getInvertedLanguageProfile(LanguageProfile languageProfile) {

        Map<String, Double> invertedMap = new HashMap<>();
        languageProfile.getNgrams().forEach((k, v) -> v.forEach(ngram -> invertedMap.put(ngram, k)));

        return invertedMap;

    }

}