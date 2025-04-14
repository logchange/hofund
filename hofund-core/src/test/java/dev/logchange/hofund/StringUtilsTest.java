package dev.logchange.hofund;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @Test
    void testEmptyIfNull_withNull_shouldReturnEmptyString() {
        // given
        String input = null;

        // when
        String result = StringUtils.emptyIfNull(input);

        // then
        assertEquals("", result);
    }

    @Test
    void testEmptyIfNull_withNonNull_shouldReturnSameString() {
        // given
        String input = "hello";

        // when
        String result = StringUtils.emptyIfNull(input);

        // then
        assertEquals("hello", result);
    }

    @Test
    void testIsEmpty_withNull_shouldReturnTrue() {
        // given
        String input = null;

        // when
        boolean result = StringUtils.isEmpty(input);

        // then
        assertTrue(result);
    }

    @Test
    void testIsEmpty_withEmptyString_shouldReturnTrue() {
        // given
        String input = "";

        // when
        boolean result = StringUtils.isEmpty(input);

        // then
        assertTrue(result);
    }

    @Test
    void testIsEmpty_withNonEmptyString_shouldReturnFalse() {
        // given
        String input = "not empty";

        // when
        boolean result = StringUtils.isEmpty(input);

        // then
        assertFalse(result);
    }
}
