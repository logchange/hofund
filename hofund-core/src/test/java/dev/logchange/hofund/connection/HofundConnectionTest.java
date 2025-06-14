package dev.logchange.hofund.connection;

import dev.logchange.hofund.info.HofundInfoProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HofundConnectionTest {


    @ParameterizedTest
    @EnumSource(
            value = Type.class,
            names = "DATABASE",
            mode = EnumSource.Mode.EXCLUDE)
    void shouldTargetPropertyWhenTypeIsDifferentThanDB() {
        // given:
        HofundConnection connection = new HofundConnection(
                "Target",
                "url",
                Type.HTTP,
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
}
