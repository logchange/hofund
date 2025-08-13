package dev.logchange.hofund.connection;

import dev.logchange.hofund.info.HofundInfoProvider;
import io.micrometer.core.instrument.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void getVersion_shouldReturnProperVersionObject() {
        // given:
        String value = "1.0.0";
        Version expectedversion = Version.of(value);
        HofundConnection connection = new HofundConnection(
                "products",
                "url",
                Type.HTTP,
                new AtomicReference<>(),
                "Web server");
        connection.setRequiredVersion(value);

        // when:
        Version requiredVersion = connection.getRequiredVersion();

        // then:
        assertEquals(expectedversion, requiredVersion);
    }

    @Test
    void getTags_shouldReturnFullListOfTags() {
        // given:
        List<Tag> expectedTags = Arrays.asList(
                Tag.of("id", "app-products_database_oracle"),
                Tag.of("source", "app"),
                Tag.of("target", "products_databaseoracle"),
                Tag.of("type", "database"),
                Tag.of("current_version", "1.0.0"),
                Tag.of("required_version", "1.0.0")
        );
        HofundInfoProvider provider = mock(HofundInfoProvider.class);
        when(provider.getApplicationName()).thenReturn("app");

        HofundConnection connection = new HofundConnection(
                "products",
                "url",
                Type.DATABASE,
                new AtomicReference<>(() -> HofundConnectionResult.http(Status.UP, "1.0.0")),
                "Oracle");
        connection.setRequiredVersion("1.0.0");

        // when:
        List<Tag> tags = connection.getTags(provider);

        // then:
        assertEquals(expectedTags, tags);
    }

    @Test
    void getTags_shouldReturnWithDefaultRequiredVersion() {
        // given:
        List<Tag> expectedTags = Arrays.asList(
                Tag.of("id", "app-products_database_oracle"),
                Tag.of("source", "app"),
                Tag.of("target", "products_databaseoracle"),
                Tag.of("type", "database"),
                Tag.of("current_version", "1.0.0"),
                Tag.of("required_version", "N/A")
        );
        HofundInfoProvider provider = mock(HofundInfoProvider.class);
        when(provider.getApplicationName()).thenReturn("app");

        HofundConnection connection = new HofundConnection(
                "products",
                "url",
                Type.DATABASE,
                new AtomicReference<>(() -> HofundConnectionResult.http(Status.UP, "1.0.0")),
                "Oracle");

        // when:
        List<Tag> tags = connection.getTags(provider);

        // then:
        assertEquals(expectedTags, tags);
    }

}
