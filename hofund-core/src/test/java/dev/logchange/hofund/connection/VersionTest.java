package dev.logchange.hofund.connection;

import org.junit.jupiter.api.Test;

import static dev.logchange.hofund.connection.HofundConnectionResult.NOT_APPLICABLE;
import static dev.logchange.hofund.connection.HofundConnectionResult.UNKNOWN;
import static org.junit.jupiter.api.Assertions.*;

class VersionTest {

    @Test
    void shouldInformIfVersionIsUnspecified() {
        // given:
        Version regular = Version.of("1.2.3");
        Version unknown = Version.of(UNKNOWN);
        Version notApplicable = Version.of(NOT_APPLICABLE);

        // when-then:
        assertFalse(regular.isUnspecified());
        assertTrue(unknown.isUnspecified());
        assertTrue(notApplicable.isUnspecified());
    }

    @Test
    void shouldCompareRegularVersions() {
        // given:
        Version v1 = Version.of("1.0.0");
        Version v2 = Version.of("2.0.0");
        Version v3 = Version.of("1.1.0");
        Version v4 = Version.of("1.0.1");
        Version v5 = Version.of("1.0.0");

        // when-then:
        assertTrue(v1.compareTo(v2) < 0, "1.0.0 should be less than 2.0.0");
        assertTrue(v2.compareTo(v1) > 0, "2.0.0 should be greater than 1.0.0");
        assertTrue(v1.compareTo(v3) < 0, "1.0.0 should be less than 1.1.0");
        assertTrue(v3.compareTo(v1) > 0, "1.1.0 should be greater than 1.0.0");
        assertTrue(v1.compareTo(v4) < 0, "1.0.0 should be less than 1.0.1");
        assertTrue(v4.compareTo(v1) > 0, "1.0.1 should be greater than 1.0.0");
        assertEquals(0, v1.compareTo(v5), "1.0.0 should be equal to 1.0.0");
    }

    @Test
    void shouldHandleDifferentLengthVersions() {
        // given:
        Version v1 = Version.of("1.0");
        Version v2 = Version.of("1.0.0");
        Version v3 = Version.of("1.0.1");

        // when-then:
        assertEquals(0, v1.compareTo(v2), "1.0 should be equal to 1.0.0");
        assertTrue(v1.compareTo(v3) < 0, "1.0 should be less than 1.0.1");
    }

    @Test
    void shouldHandleSpecialValues() {
        // given:
        Version unknown = Version.of(UNKNOWN);
        Version notApplicable = Version.of(NOT_APPLICABLE);
        Version regular = Version.of("1.0.0");

        // when-then:
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> unknown.compareTo(regular));
        assertEquals("Cannot compare regular versions with unspecified!", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> notApplicable.compareTo(regular));
        assertEquals("Cannot compare regular versions with unspecified!", ex2.getMessage());

        IllegalArgumentException ex3 = assertThrows(IllegalArgumentException.class, () -> regular.compareTo(unknown));
        assertEquals("Cannot compare regular versions with unspecified!", ex3.getMessage());

        IllegalArgumentException ex4 = assertThrows(IllegalArgumentException.class, () -> regular.compareTo(notApplicable));
        assertEquals("Cannot compare regular versions with unspecified!", ex4.getMessage());

        assertEquals(0, unknown.compareTo(notApplicable), "UNKNOWN should be equal to NOT_APPLICABLE");
        assertEquals(0, notApplicable.compareTo(unknown), "NOT_APPLICABLE should be equal to UNKNOWN");
    }

    @Test
    void shouldHandleNonNumericVersionParts() {
        // given:
        Version v1 = Version.of("1.0.beta");
        Version v2 = Version.of("1.0.0");

        Version v3 = Version.of("1.0.1");
        Version v4 = Version.of("1.0.1-SNAPSHOT");
        Version v5 = Version.of("1.0.4-SNAPSHOT");

        // then
        assertEquals(0, v1.compareTo(v2), "1.0.beta should be equal to 1.0.0");
        assertEquals(0, v2.compareTo(v1), "1.0.1-SNAPSHOT should be equal to 1.0.1");
        assertTrue(v5.compareTo(v3) > 0, "1.0.4-SNAPSHOT should be greater than 1.0.1");
        assertTrue(v5.compareTo(v4) > 0, "1.0.4-SNAPSHOT should be greater than 1.0.1-SNAPSHOT");
    }

    @Test
    void shouldImplementEqualsAndHashCodeCorrectly() {
        // given
        Version v1 = Version.of("1.0.0");
        Version v2 = Version.of("1.0.0");
        Version v3 = Version.of("2.0.0");

        // then
        assertEquals(v1, v2, "Equal versions should be equal");
        assertNotEquals(v1, v3, "Different versions should not be equal");
        assertEquals(v1.hashCode(), v2.hashCode(), "Equal versions should have the same hash code");
    }

    @Test
    void shouldImplementToStringCorrectly() {
        // given
        String versionString = "1.0.0";
        Version version = Version.of(versionString);

        // then
        assertEquals(versionString, version.toString(), "toString should return the version string");
    }
}