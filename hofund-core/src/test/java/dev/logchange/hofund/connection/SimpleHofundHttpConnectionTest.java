package dev.logchange.hofund.connection;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static dev.logchange.hofund.connection.HofundConnectionResult.NOT_APPLICABLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SimpleHofundHttpConnectionTest {

    @Test
    void defaultConstructorShouldUseGETActiveAndSetDefaults() throws IOException, InterruptedException {
        try (MockWebServer server = new MockWebServer()) {
            // given:
            server.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .setBody("{\"application\":{\"version\":\"1.2.3\"}}"));

            HttpUrl url = server.url("/api/health");
            SimpleHofundHttpConnection connection = new SimpleHofundHttpConnection("test-service", url.toString());

            // when:
            HofundConnection hc = connection.toHofundConnection();
            assertNotNull(hc);
            HofundConnectionResult result = hc.getFun().get().getConnection();

            // then:
            assertEquals(Status.UP, result.getStatus());
            assertEquals("1.2.3", result.getVersion().toString());
            assertEquals("test-service", hc.getTarget());
            assertEquals(url.toString(), hc.getUrl());
            assertEquals(Type.HTTP, hc.getType());
            assertEquals("share-alt", hc.getIcon());
            assertEquals(NOT_APPLICABLE, hc.getRequiredVersion().toString());

            RecordedRequest recorded = server.takeRequest();
            assertEquals("GET", recorded.getMethod());
            assertEquals("/api/health", recorded.getPath());
        }
    }

    @Test
    void constructorWithIconShouldOverrideDefaultIcon() {
        // given:
        SimpleHofundHttpConnection connection = new SimpleHofundHttpConnection("svc", "http://example/health", "rocket");

        // when:
        HofundConnection hc = connection.toHofundConnection();

        // then:
        assertNotNull(hc);
        assertEquals("rocket", hc.getIcon());
        assertEquals("svc", hc.getTarget());
        assertEquals("", hc.getDescription());
        assertEquals(Type.HTTP, hc.getType());
        assertEquals(Version.of(NOT_APPLICABLE), hc.getRequiredVersion());
        assertEquals("http://example/health", hc.getUrl());
    }

    @Test
    void constructorWithPostMethodShouldUsePOST() throws IOException, InterruptedException {
        try (MockWebServer server = new MockWebServer()) {
            // given:
            server.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .setBody("{\"application\":{\"version\":\"9.9.9\"}}"));

            HttpUrl url = server.url("/api/health");
            SimpleHofundHttpConnection connection = new SimpleHofundHttpConnection("svc", url.toString(), RequestMethod.POST);

            // when:
            HofundConnection hc = connection.toHofundConnection();
            assertNotNull(hc);
            HofundConnectionResult connectionResult = hc.getFun().get().getConnection();

            // then:
            RecordedRequest recorded = server.takeRequest();
            assertEquals("POST", recorded.getMethod());
            assertEquals("share-alt", hc.getIcon());
            assertEquals("svc", hc.getTarget());
            assertEquals("", hc.getDescription());
            assertEquals(Type.HTTP, hc.getType());
            assertEquals(Version.of(NOT_APPLICABLE), hc.getRequiredVersion());
            assertEquals(Status.UP, connectionResult.getStatus());
            assertEquals("9.9.9", connectionResult.getVersion().toString());
        }
    }

    @Test
    void inactiveCheckingStatusShouldReturnInactiveAndNotCallServer() throws IOException {
        try (MockWebServer server = new MockWebServer()) {
            // given
            HttpUrl url = server.url("/api/health");
            SimpleHofundHttpConnection connection = new SimpleHofundHttpConnection("svc", url.toString(), CheckingStatus.INACTIVE);

            // when:
            HofundConnection hc = connection.toHofundConnection();
            assertNotNull(hc);
            HofundConnectionResult result = hc.getFun().get().getConnection();

            // then:
            assertEquals(Status.INACTIVE, result.getStatus());
            assertEquals(NOT_APPLICABLE, result.getVersion().toString());
            assertEquals(0, server.getRequestCount());

            assertEquals("share-alt", hc.getIcon());
            assertEquals("svc", hc.getTarget());
            assertEquals("", hc.getDescription());
            assertEquals(Type.HTTP, hc.getType());
            assertEquals(Version.of(NOT_APPLICABLE), hc.getRequiredVersion());
            assertEquals(Status.INACTIVE, result.getStatus());
            assertEquals("N/A", result.getVersion().toString());
        }
    }

    @Test
    void factoryMethodShouldSetRequiredVersion() throws IOException {
        try (MockWebServer server = new MockWebServer()) {
            // given:
            server.enqueue(new MockResponse().setResponseCode(200).setBody("{\"application\":{\"version\":\"3.0.0\"}}"));
            HttpUrl url = server.url("/health");

            // when:
            SimpleHofundHttpConnection active = SimpleHofundHttpConnection.of("svc", url.toString(), "1.0.0");
            HofundConnection hc = active.toHofundConnection();
            assertNotNull(hc);
            HofundConnectionResult connectionResult = hc.getFun().get().getConnection();

            // then:
            assertEquals("1.0.0", hc.getRequiredVersion().toString());
            assertEquals("share-alt", hc.getIcon());
            assertEquals("svc", hc.getTarget());
            assertEquals("", hc.getDescription());
            assertEquals(Type.HTTP, hc.getType());
            assertEquals(Status.UP, connectionResult.getStatus());
            assertEquals("3.0.0", connectionResult.getVersion().toString());
        }
    }

    @Test
    void factoryMethodShouldSetRequiredVersionAndStatus() throws IOException {
        try (MockWebServer server = new MockWebServer()) {
            // given:
            server.enqueue(new MockResponse().setResponseCode(200).setBody("{\"application\":{\"version\":\"3.0.0\"}}"));
            HttpUrl url = server.url("/health");

            // when:
            SimpleHofundHttpConnection inactive = SimpleHofundHttpConnection.of("svc2", url.toString(), CheckingStatus.INACTIVE, "2.0.0");
            HofundConnection hc = inactive.toHofundConnection();
            assertNotNull(hc);
            HofundConnectionResult result = hc.getFun().get().getConnection();

            // then:
            assertEquals("2.0.0", hc.getRequiredVersion().toString());
            assertEquals(Status.INACTIVE, result.getStatus());
            assertEquals("share-alt", hc.getIcon());
            assertEquals("svc2", hc.getTarget());
            assertEquals("", hc.getDescription());
            assertEquals(Type.HTTP, hc.getType());
        }
    }

    @Test
    void constructorWithCheckingStatusAndIconShouldRespectIconAndInactiveSkipsCall() throws IOException {
        try (MockWebServer server = new MockWebServer()) {
            // given:
            HttpUrl url = server.url("/ping");
            SimpleHofundHttpConnection connection = new SimpleHofundHttpConnection("svc", url.toString(), CheckingStatus.INACTIVE, "bolt");

            // when:
            HofundConnection hc = connection.toHofundConnection();
            HofundConnectionResult result = hc.getFun().get().getConnection();

            // then:
            assertEquals("bolt", hc.getIcon());
            assertEquals(Status.INACTIVE, result.getStatus());
            assertEquals(0, server.getRequestCount());
            assertEquals("N/A", hc.getRequiredVersion().toString());
            assertEquals("svc", hc.getTarget());
            assertEquals("", hc.getDescription());
            assertEquals(Type.HTTP, hc.getType());
        }
    }

    @Test
    void constructorWithRequestMethodAndIconShouldUseMethodAndIcon() throws IOException, InterruptedException {
        try (MockWebServer server = new MockWebServer()) {
            // given:
            server.enqueue(new MockResponse().setResponseCode(200).setBody("{\"application\":{\"version\":\"1.0.0\"}}"));
            HttpUrl url = server.url("/healthcheck");
            SimpleHofundHttpConnection connection = new SimpleHofundHttpConnection("svc", url.toString(), RequestMethod.POST, "rocket");

            // when:
            HofundConnection hc = connection.toHofundConnection();
            HofundConnectionResult result = hc.getFun().get().getConnection();

            // then:
            assertEquals("rocket", hc.getIcon());
            RecordedRequest recorded = server.takeRequest();
            assertEquals("POST", recorded.getMethod());
            assertEquals("svc", hc.getTarget());
            assertEquals("", hc.getDescription());
            assertEquals(Type.HTTP, hc.getType());
            assertEquals(Status.UP, result.getStatus());
            assertEquals("1.0.0", result.getVersion().toString());
        }
    }

    @Test
    void constructorWithCheckingStatusAndRequestMethodShouldUseCustomMethodWhenActive() throws IOException, InterruptedException { //herre
        try (MockWebServer server = new MockWebServer()) {
            // given:
            server.enqueue(new MockResponse().setResponseCode(200).setBody("{\"application\":{\"version\":\"2.0.0\"}}"));
            HttpUrl url = server.url("/v1/health");
            SimpleHofundHttpConnection connection = new SimpleHofundHttpConnection("svc", url.toString(), CheckingStatus.ACTIVE, RequestMethod.GET);

            // when:
            HofundConnection hc = connection.toHofundConnection();
            HofundConnectionResult result = hc.getFun().get().getConnection();

            // then:
            RecordedRequest recorded = server.takeRequest();
            assertEquals("GET", recorded.getMethod());
            assertEquals("share-alt", hc.getIcon());
            assertEquals("svc", hc.getTarget());
            assertEquals("", hc.getDescription());
            assertEquals(Type.HTTP, hc.getType());
            assertEquals("N/A", hc.getRequiredVersion().toString());
            assertEquals(Status.UP, result.getStatus());
            assertEquals("2.0.0", result.getVersion().toString());
        }
    }

    @Test
    void constructorWithCheckingStatusRequestMethodAndIconShouldUseAllProvidedValues() throws IOException, InterruptedException {
        try (MockWebServer server = new MockWebServer()) {
            // given:
            server.enqueue(new MockResponse().setResponseCode(200).setBody("{\"application\":{\"version\":\"4.5.6\"}}"));
            HttpUrl url = server.url("/hc");
            SimpleHofundHttpConnection connection = new SimpleHofundHttpConnection("svc", url.toString(), CheckingStatus.ACTIVE, RequestMethod.PUT, "star");

            // when:
            HofundConnection hc = connection.toHofundConnection();
            HofundConnectionResult result = hc.getFun().get().getConnection();

            // then:
            assertEquals("star", hc.getIcon());
            RecordedRequest recorded = server.takeRequest();
            assertEquals("PUT", recorded.getMethod());
            assertEquals("svc", hc.getTarget());
            assertEquals("", hc.getDescription());
            assertEquals(Type.HTTP, hc.getType());
            assertEquals("N/A", hc.getRequiredVersion().toString());
            assertEquals(Status.UP, result.getStatus());
            assertEquals("4.5.6", result.getVersion().toString());
        }
    }

    @Test
    void fullConstructorShouldSetRequiredVersionAndUseProvidedMethod() throws IOException, InterruptedException {
        try (MockWebServer server = new MockWebServer()) {
            // given:
            server.enqueue(new MockResponse().setResponseCode(200).setBody("{\"application\":{\"version\":\"7.8.9\"}}"));
            HttpUrl url = server.url("/ready");
            SimpleHofundHttpConnection connection = new SimpleHofundHttpConnection(
                    "svc", url.toString(), CheckingStatus.ACTIVE, RequestMethod.DELETE, "cloud", "10.0.0"
            );

            // when:
            HofundConnection hc = connection.toHofundConnection();
            HofundConnectionResult result = hc.getFun().get().getConnection();

            // then:
            assertEquals("cloud", hc.getIcon());
            RecordedRequest recorded = server.takeRequest();
            assertEquals("DELETE", recorded.getMethod());
            assertEquals("svc", hc.getTarget());
            assertEquals("", hc.getDescription());
            assertEquals(Type.HTTP, hc.getType());
            assertEquals("10.0.0", hc.getRequiredVersion().toString());
            assertEquals(Status.UP, result.getStatus());
            assertEquals("7.8.9", result.getVersion().toString());
        }
    }
}
