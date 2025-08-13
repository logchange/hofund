package dev.logchange.hofund.connection;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

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
            assertEquals(HofundConnectionResult.NOT_APPLICABLE, hc.getRequiredVersion().toString());

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
            hc.getFun().get().getConnection();

            // then:
            RecordedRequest recorded = server.takeRequest();
            assertEquals("POST", recorded.getMethod());
        }
    }

    @Test
    void inactiveCheckingStatusShouldReturnInactiveAndNotCallServer() throws IOException {
        try (MockWebServer server = new MockWebServer()) {
            // given
            HttpUrl url = server.url("/api/health");
            SimpleHofundHttpConnection connection = new SimpleHofundHttpConnection(
                    "svc", url.toString(), CheckingStatus.INACTIVE
            );

            // when:
            HofundConnection hc = connection.toHofundConnection();
            HofundConnectionResult result = hc.getFun().get().getConnection();

            // then:
            assertEquals(Status.INACTIVE, result.getStatus());
            assertEquals(HofundConnectionResult.NOT_APPLICABLE, result.getVersion().toString());
            assertEquals(0, server.getRequestCount());
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
            HofundConnection hcActive = active.toHofundConnection();
            hcActive.getFun().get().getConnection();

            // then:
            assertEquals("1.0.0", hcActive.getRequiredVersion().toString());

            // when - INACTIVE
            SimpleHofundHttpConnection inactive = SimpleHofundHttpConnection.of("svc2", url.toString(), CheckingStatus.INACTIVE, "2.0.0");
            HofundConnection hcInactive = inactive.toHofundConnection();
            HofundConnectionResult resInactive = hcInactive.getFun().get().getConnection();

            // then
            assertEquals("2.0.0", hcInactive.getRequiredVersion().toString());
            assertEquals(Status.INACTIVE, resInactive.getStatus());
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
            HofundConnection hcInactive = inactive.toHofundConnection();
            HofundConnectionResult resInactive = hcInactive.getFun().get().getConnection();

            // then:
            assertEquals("2.0.0", hcInactive.getRequiredVersion().toString());
            assertEquals(Status.INACTIVE, resInactive.getStatus());
        }
    }
}
