package dev.logchange.hofund.connection;

import dev.logchange.hofund.info.HofundInfoProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HofundConnectionTest {


    @ParameterizedTest
    @EnumSource(
            value = Type.class,
            names = {"DATABASE", "QUEUE"},
            mode = EnumSource.Mode.EXCLUDE)
    void shouldTargetPropertyWhenTypeIsDifferentThanDB(Type type) {
        // given:
        HofundConnection connection = new HofundConnection(
                "Target",
                "url",
                type,
                new AtomicReference<>(),
                "");

        // when:
        String targetTag = connection.toTargetTag();

        // then:
        assertEquals("Target", targetTag);
    }

    @Test
    void shouldTargetPropertyWhenTypeIsDB() {
        // given:
        HofundConnection connection = new HofundConnection(
                "Target",
                "url",
                Type.DATABASE,
                new AtomicReference<>(),
                "");


        String expected = "Target_database";

        // when:
        String targetTag = connection.toTargetTag();

        // then:
        assertEquals(expected, targetTag);
    }

    @Test
    void shouldTargetPropertyWhenTypeIsQueue() {
        // given:
        HofundConnection connection = new HofundConnection(
                "Target",
                "url",
                Type.QUEUE,
                new AtomicReference<>(),
                "");


        String expected = "Target_queue";

        // when:
        String targetTag = connection.toTargetTag();

        // then:
        assertEquals(expected, targetTag);
    }

    @Test
    void shouldReturnEmptyDescriptionWhenPropertyIsNull() {
        // given:
        HofundConnection connection = new HofundConnection(
                "Target",
                "url",
                Type.HTTP,
                new AtomicReference<>(),
                null);


        // when:
        String description = connection.getDescription();

        // then:
        assertEquals("", description);
    }

    @Test
    void shouldReturnValueOfDescriptionWhenPropertyIsNotNull() {
        // given:

        String expected = "Description";

        HofundConnection connection = new HofundConnection(
                "products",
                "url",
                Type.DATABASE,
                new AtomicReference<>(),
                expected);

        // when:
        String description = connection.getDescription();

        // then:
        assertEquals(expected, description);
    }

    @Test
    void getEdgeId_shouldOmitDescription_whenDescriptionIsNull() {
        // given
        HofundInfoProvider provider = mock(HofundInfoProvider.class);
        when(provider.getApplicationName()).thenReturn("app");

        HofundConnection connection = new HofundConnection(
                "products",
                "url",
                Type.DATABASE,
                new AtomicReference<>(),
                null);


        // when
        String edgeId = connection.getEdgeId(provider);

        // then
        assertEquals("app-products_database", edgeId);
    }

    @Test
    void getEdgeId_shouldOmitDescription_whenDescriptionIsEmpty() {
        // given
        HofundInfoProvider provider = mock(HofundInfoProvider.class);
        when(provider.getApplicationName()).thenReturn("app");

        HofundConnection connection = new HofundConnection(
                "products",
                "url",
                Type.DATABASE,
                new AtomicReference<>(),
                "");


        // when
        String edgeId = connection.getEdgeId(provider);

        // then
        assertEquals("app-products_database", edgeId);
    }

    @Test
    void getEdgeId_shouldIncludeDescription_whenDescriptionIsPresent() {
        // given
        HofundInfoProvider provider = mock(HofundInfoProvider.class);
        when(provider.getApplicationName()).thenReturn("app");

        HofundConnection connection = new HofundConnection(
                "products",
                "url",
                Type.DATABASE,
                new AtomicReference<>(),
                "Oracle");


        // when
        String edgeId = connection.getEdgeId(provider);

        // then
        assertEquals("app-products_database_oracle", edgeId);
    }

    @Test
    void shouldThrowIllegalArgumentException_whenUrlEndsWithPrometheus() {
        // given
        String urlEndingWithPrometheus = "http://example.com/prometheus";

        // when & then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new HofundConnection(
                        "products",
                        urlEndingWithPrometheus,
                        Type.HTTP,
                        new AtomicReference<>(),
                        "description"
                )
        );

        assertEquals("URLs ending with '/prometheus' are forbidden as they can lead to recursive dependencies", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "http://example.com",
            "http://example.com/",
            "http://example.com/api",
            "http://example.com/promethe",
            "http://example.com/prometheus/api",
            ""
    })
    void shouldNotThrowException_whenUrlDoesNotEndWithPrometheus(String validUrl) {
        // when & then
        new HofundConnection(
                "products",
                validUrl,
                Type.HTTP,
                new AtomicReference<>(),
                "description"
        );
        // No exception should be thrown
    }
}
