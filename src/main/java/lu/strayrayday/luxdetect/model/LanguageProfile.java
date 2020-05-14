package lu.strayrayday.luxdetect.model;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class LanguageProfile {

    private String language;

    private int samples;

    private Map<Double, List<String>> ngrams;

}