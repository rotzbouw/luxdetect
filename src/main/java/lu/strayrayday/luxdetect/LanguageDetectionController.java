package lu.strayrayday.luxdetect;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lu.strayrayday.luxdetect.model.DetectedLanguages;
import lu.strayrayday.luxdetect.model.LanguageDetectionRequest;

@RestController
@RequiredArgsConstructor
public class LanguageDetectionController {

    private final LanguageDetectionService languageDetectionService;

    /**
     * Extract scores per matched language.
     * @param request Object with one self-explanatory field: 'text'.
     * @return Map of languages and their respective scores. Highest score determines the detected language.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public DetectedLanguages detectLanguages(@RequestBody LanguageDetectionRequest request) {
        return DetectedLanguages.builder()
                .scoredLanguages(languageDetectionService.detectLanguages(request.getText()))
                .build();
    }

}