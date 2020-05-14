package lu.strayrayday.luxdetect.langdetect;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import lu.strayrayday.luxdetect.LanguageDetectionController;
import lu.strayrayday.luxdetect.LanguageDetectionService;
import lu.strayrayday.luxdetect.model.DetectedLanguages;
import lu.strayrayday.luxdetect.model.LanguageDetectionRequest;

@RunWith(SpringRunner.class)
@WebMvcTest(LanguageDetectionController.class)
public class LanguageDetectionControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LanguageDetectionService languageDetectionService;

    @Test
    public void testController() throws Exception {

        String testSentence = "This is a test sentence.";
        LanguageDetectionRequest languageDetectionRequest = new LanguageDetectionRequest();
        languageDetectionRequest.setText(testSentence);

        Map<String, Double> detectedLanguagesMap = Map.of("ENGLISH", 100d);
        BDDMockito.given(languageDetectionService.detectLanguages(testSentence)).willReturn(detectedLanguagesMap);
        DetectedLanguages detectedLanguages = DetectedLanguages.builder()
                .scoredLanguages(detectedLanguagesMap)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mvc.perform(post("/")
                .content(mapper.writeValueAsString(languageDetectionRequest)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(mapper.writeValueAsString(detectedLanguages)));

    }

}