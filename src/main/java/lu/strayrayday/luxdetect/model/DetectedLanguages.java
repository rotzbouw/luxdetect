package lu.strayrayday.luxdetect.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DetectedLanguages {

    @Builder.Default
    private Map<String, Double> scoredLanguages = new HashMap<>();

}