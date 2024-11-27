package dev.logchange.hofund.connection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HofundConnectionTest {


    @ParameterizedTest
    @EnumSource(
            value = Type.class,
            names = "DATABASE",
            mode = EnumSource.Mode.EXCLUDE)
    void shouldTargetPropertyWhenTypeIsDifferentThanDB() {
        // given:
        HofundConnection connection = HofundConnection.builder()
                .target("Target")
                .type(Type.HTTP)
                .build();

        // when:
        String targetTag = connection.toTargetTag();

        // then:
        assertEquals("Target", targetTag);
    }

    @Test
    void shouldTargetPropertyWhenTypeIsDB() {
        // given:
        HofundConnection connection = HofundConnection.builder()
                .target("Target")
                .type(Type.DATABASE)
                .build();

        String expected = "Target_database";

        // when:
        String targetTag = connection.toTargetTag();

        // then:
        assertEquals(expected, targetTag);
    }

    @Test
    void shouldReturnEmptyDescriptionWhenPropertyIsNull() {
        // given:
        HofundConnection connection = HofundConnection.builder().build();

        // when:
        String description = connection.getDescription();

        // then:
        assertEquals("", description);
    }

    @Test
    void shouldReturnValueOfDescriptionWhenPropertyIsNotNull() {
        // given:

        String expected = "Description";
        HofundConnection connection = HofundConnection.builder().description(expected).build();

        // when:
        String description = connection.getDescription();

        // then:
        assertEquals(expected, description);
    }
}
