package dev.logchange.hofund.connection;

import dev.logchange.hofund.info.HofundInfoProvider;
import io.micrometer.core.instrument.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
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
        // given:
        HofundInfoProvider provider = mock(HofundInfoProvider.class);
        when(provider.getApplicationName()).thenReturn("app");

        HofundConnection connection = new HofundConnection(
                "products",
                "url",
                Type.DATABASE,
                new AtomicReference<>(),
                null);


        // when:
        String edgeId = connection.getEdgeId(provider);

        // then:
        assertEquals("app-products_database", edgeId);
    }

    @Test
    void getEdgeId_shouldOmitDescription_whenDescriptionIsEmpty() {
        // given:
        HofundInfoProvider provider = mock(HofundInfoProvider.class);
        when(provider.getApplicationName()).thenReturn("app");

        HofundConnection connection = new HofundConnection(
                "products",
                "url",
                Type.DATABASE,
                new AtomicReference<>(),
                "");


        // when:
        String edgeId = connection.getEdgeId(provider);

        // then:
        assertEquals("app-products_database", edgeId);
    }

    @Test
    void getEdgeId_shouldIncludeDescription_whenDescriptionIsPresent() {
        // given:
        HofundInfoProvider provider = mock(HofundInfoProvider.class);
        when(provider.getApplicationName()).thenReturn("app");

        HofundConnection connection = new HofundConnection(
                "products",
                "url",
                Type.DATABASE,
                new AtomicReference<>(),
                "Oracle");


        // when:
        String edgeId = connection.getEdgeId(provider);

        // then:
        assertEquals("app-products_database_oracle", edgeId);
    }

    @Test
    void shouldThrowIllegalArgumentException_whenUrlEndsWithPrometheus() {
        // given:
        String urlEndingWithPrometheus = "http://example.com/prometheus";

        // when-then:
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

        assertEquals("URL for HofundConnection cannot end with '/prometheus'. It is forbidden as it can lead to recursive dependencies", exception.getMessage());
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
        // when-then:
        new HofundConnection(
                "products",
                validUrl,
                Type.HTTP,
                new AtomicReference<>(),
                "description"
        );
        // No exception should be thrown
    }

    @Test
    void shouldThrowIllegalArgumentException_whenUrlIsNull() {
        // given:
        String nullUrl = null;
        String target = "products";
        Type type = Type.HTTP;
        String description = "description";

        // when-then:
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new HofundConnection(
                        target,
                        nullUrl,
                        type,
                        new AtomicReference<>(),
                        description
                )
        );

        assertEquals("URL for HofundConnection cannot be null. Target: " + target + " Type: " + type + " Description: " + description, exception.getMessage());
    }

    @Test
    void shouldReturnDatabaseIcon_whenTypeIsDatabase() {
        // given:
        HofundConnection connection = new HofundConnection(
                "Target",
                "url",
                Type.DATABASE,
                new AtomicReference<>(),
                "");

        // when:
        String icon = connection.getIcon();

        // then:
        assertEquals("database", icon);
    }

    @Test
    void shouldReturnFileAltIcon_whenTypeIsFTP() {
        // given:
        HofundConnection connection = new HofundConnection(
                "Target",
                "url",
                Type.FTP,
                new AtomicReference<>(),
                "");

        // when:
        String icon = connection.getIcon();

        // then:
        assertEquals("file-alt", icon);
    }

    @Test
    void shouldReturnShareAltIcon_whenTypeIsHTTP() {
        // given:
        HofundConnection connection = new HofundConnection(
                "Target",
                "url",
                Type.HTTP,
                new AtomicReference<>(),
                "");

        // when:
        String icon = connection.getIcon();

        // then:
        assertEquals("share-alt", icon);
    }

    @Test
    void shouldReturnChannelAddIcon_whenTypeIsQueue() {
        // given:
        HofundConnection connection = new HofundConnection(
                "Target",
                "url",
                Type.QUEUE,
                new AtomicReference<>(),
                "");

        // when:
        String icon = connection.getIcon();

        // then:
        assertEquals("channel-add", icon);
    }

    @Test
    void shouldReturnCustomIcon_whenIconIsSet() {
        // given:
        HofundConnection connection = new HofundConnection(
                "Target",
                "url",
                Type.HTTP,
                new AtomicReference<>(),
                "");
        String customIcon = "custom-icon";
        connection.setIcon(customIcon);

        // when:
        String icon = connection.getIcon();

        // then:
        assertEquals(customIcon, icon);
    }

    @Test
    void shouldReturnCorrectTags_whenGetTagsIsCalled() {
        // given:
        String target = "products";
        String url = "http://example.com";
        Type type = Type.DATABASE;
        String description = "Oracle";
        String appName = "app";
        String version = "1.0.0";

        HofundInfoProvider infoProvider = mock(HofundInfoProvider.class);
        when(infoProvider.getApplicationName()).thenReturn(appName);

        AtomicReference<ConnectionFunction> funRef = new AtomicReference<>();
        ConnectionFunction connectionFunction = mock(ConnectionFunction.class);
        HofundConnectionResult connectionResult = mock(HofundConnectionResult.class);
        when(connectionResult.getVersion()).thenReturn(version);
        when(connectionFunction.getConnection()).thenReturn(connectionResult);
        funRef.set(connectionFunction);

        HofundConnection hofundConnection = new HofundConnection(
                target,
                url,
                type,
                funRef,
                description
        );

        // when:
        List<Tag> tags = hofundConnection.getTags(infoProvider);

        // then:
        assertEquals(5, tags.size());
        assertEquals("id", tags.get(0).getKey());
        assertEquals("app-products_database_oracle", tags.get(0).getValue());
        assertEquals("source", tags.get(1).getKey());
        assertEquals("app", tags.get(1).getValue());
        assertEquals("target", tags.get(2).getKey());
        assertEquals("products_database_oracle", tags.get(2).getValue());
        assertEquals("type", tags.get(3).getKey());
        assertEquals("database", tags.get(3).getValue());
        assertEquals("current_version", tags.get(4).getKey());
        assertEquals("1.0.0", tags.get(4).getValue());
    }

    @Test
    void shouldReturnCorrectValues_whenGettersAreCalled() {
        // given:
        String target = "products";
        String url = "http://example.com";
        Type type = Type.DATABASE;
        String description = "Oracle";
        AtomicReference<ConnectionFunction> funRef = new AtomicReference<>();

        HofundConnection connection = new HofundConnection(
                target,
                url,
                type,
                funRef,
                description
        );

        // when-then:
        assertEquals(target, connection.getTarget());
        assertEquals(url, connection.getUrl());
        assertEquals(type, connection.getType());
        assertEquals(funRef, connection.getFun());
    }
}
