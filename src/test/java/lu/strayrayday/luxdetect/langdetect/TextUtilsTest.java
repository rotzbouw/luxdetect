package lu.strayrayday.luxdetect.langdetect;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class TextUtilsTest {

    @Test
    public void testNormalisation() {

        String testString = "String containing punctuation (%&.- and numbers 1337 and it's great.";
        String expected = "String containing punctuation  and numbers  and it's great";

        assertThat(TextUtils.normalise(testString)).isEqualTo(expected);

    }

    @Test
    public void testPadding() {

        String testString = "unpadded";
        String expected = "____unpadded____";

        assertThat(TextUtils.getPaddedToken(testString, "_", 4)).isEqualTo(expected);

    }

}