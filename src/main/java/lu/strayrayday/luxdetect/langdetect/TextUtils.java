package lu.strayrayday.luxdetect.langdetect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TextUtils {

    private final Pattern ONLY_LETTERS_AND_APOSTROPHES = Pattern.compile("[^\\p{L}'\\s]+");

    public String normalise(String text) {
        return ONLY_LETTERS_AND_APOSTROPHES.matcher(text).replaceAll("");
    }

    public Stream<String> get5Grams(String text) {
        return Arrays.stream(normalise(text).split("\\s+"))
                .flatMap(token ->
                        IntStream.range(1, 6).mapToObj(i -> getNGrams(getPaddedToken(token, "_", i - 1), i).stream()).flatMap(Function.identity())
                );
    }

    public List<String> getNGrams(String text, int n) {

        if (StringUtils.isEmpty(text) || n > text.length()) {
            return Collections.emptyList();
        }
        List<String> ngrams = new ArrayList<>();
        for (int i = 0; i <= text.length() - n; i++) {
            ngrams.add(text.substring(i, i + n).trim());
        }

        return ngrams;

    }

    public String getPaddedToken(String text, String padCharacter, int padSize) {

        String pad = padCharacter.repeat(padSize);

        return pad + text + pad;

    }

}