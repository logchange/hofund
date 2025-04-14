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

    @Test
    void getEdgeId_shouldOmitDescription_whenDescriptionIsNull() {
        // given
        HofundInfoProvider provider = mock(HofundInfoProvider.class);
        when(provider.getApplicationName()).thenReturn("app");

        HofundConnection connection = HofundConnection.builder()
                .target("products")
                .type(Type.DATABASE)
                .description(null)
                .fun(new AtomicReference<>())
                .build();

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

        HofundConnection connection = HofundConnection.builder()
                .target("products")
                .type(Type.DATABASE)
                .description("")
                .fun(new AtomicReference<>())
                .build();

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

        HofundConnection connection = HofundConnection.builder()
                .target("products")
                .type(Type.DATABASE)
                .description("Oracle")
                .fun(new AtomicReference<>())
                .build();

        // when
        String edgeId = connection.getEdgeId(provider);

        // then
        assertEquals("app-products_database_oracle", edgeId);
    }
}
