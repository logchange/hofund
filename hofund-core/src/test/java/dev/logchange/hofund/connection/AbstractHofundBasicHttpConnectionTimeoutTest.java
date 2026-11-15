package dev.logchange.hofund.connection;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * getConnectTimeout() + getReadTimeout() do NOT bound the total time of a single check.
 * connectTimeout applies to the TCP handshake only and readTimeout is a per-read SO_TIMEOUT,
 * so followed redirects and a slow-but-not-stalled server stretch a single check well past it.
 */
class AbstractHofundBasicHttpConnectionTimeoutTest {

    private static final int CONNECT_TIMEOUT = 1000;
    private static final int READ_TIMEOUT = 1000;
    private static final long TIMEOUT_BUDGET = CONNECT_TIMEOUT + READ_TIMEOUT;

    private static final String BODY = "{\"application\":{\"version\":\"1.0.0\"}}";

    @Test
    void readTimeoutDoesNotBoundTotalTimeWhenServerDripsBody() {
        try (MockWebServer server = new MockWebServer()) {
            // given: every chunk arrives well within readTimeout, but the whole body does not
            server.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .setBody(BODY)
                    .throttleBody(4, 300, TimeUnit.MILLISECONDS));

            HofundConnection connection = connectionTo(server.url("/api/health"));

            // when:
            long start = System.nanoTime();
            HofundConnectionResult result = connection.getFun().get().getConnection();
            long elapsed = millisSince(start);

            // then:
            assertEquals(Status.UP, result.getStatus());
            assertEquals("1.0.0", result.getVersion().toString());
            assertTrue(elapsed > TIMEOUT_BUDGET,
                    "expected the check to outlive the " + TIMEOUT_BUDGET + " ms budget, took " + elapsed + " ms");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void followedRedirectsMultiplyTimeoutBudget() {
        try (MockWebServer server = new MockWebServer()) {
            // given: each hop stays under readTimeout, the chain does not
            server.enqueue(redirectTo(server.url("/hop1")));
            server.enqueue(redirectTo(server.url("/hop2")));
            server.enqueue(redirectTo(server.url("/hop3")));
            server.enqueue(new MockResponse()
                    .setResponseCode(200)
                    .setBody(BODY)
                    .setHeadersDelay(700, TimeUnit.MILLISECONDS));

            HofundConnection connection = connectionTo(server.url("/api/health"));

            // when:
            long start = System.nanoTime();
            HofundConnectionResult result = connection.getFun().get().getConnection();
            long elapsed = millisSince(start);

            // then:
            assertEquals(Status.UP, result.getStatus());
            assertEquals(4, server.getRequestCount());
            assertTrue(elapsed > TIMEOUT_BUDGET,
                    "expected the check to outlive the " + TIMEOUT_BUDGET + " ms budget, took " + elapsed + " ms");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static MockResponse redirectTo(HttpUrl location) {
        return new MockResponse()
                .setResponseCode(302)
                .addHeader("Location", location.toString())
                .setHeadersDelay(700, TimeUnit.MILLISECONDS);
    }

    private static HofundConnection connectionTo(HttpUrl url) {
        return new TestHttpConnection(url.toString()).toHofundConnection();
    }

    private static long millisSince(long startNanos) {
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos);
    }

    private static class TestHttpConnection extends AbstractHofundBasicHttpConnection {

        private final String url;

        private TestHttpConnection(String url) {
            this.url = url;
        }

        @Override
        protected String getTarget() {
            return "test-service";
        }

        @Override
        protected String getUrl() {
            return url;
        }

        @Override
        protected int getConnectTimeout() {
            return CONNECT_TIMEOUT;
        }

        @Override
        protected int getReadTimeout() {
            return READ_TIMEOUT;
        }
    }
}
