package dev.logchange.hofund.connection;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AbstractHofundBasicHttpConnectionErrorTest {

    @Test
    void shouldReturnDownStatusAndUnknownVersionForErrorResponse() {
        try (MockWebServer server = new MockWebServer()) {
            // given:
            server.enqueue(new MockResponse()
                    .setResponseCode(500)
                    .setBody("Internal Server Error"));

            HttpUrl url = server.url("/api/health");
            TestHttpConnection connection = new TestHttpConnection("test-service", url.toString(), 1000, 1000);
            HofundConnection hofundConnection = connection.toHofundConnection();

            // when:
            Connection con = hofundConnection.getFun().get().getConnection();
            Status status = con.getStatus();
            String version = con.getVersion();

            // then:
            assertEquals(Status.DOWN, status);
            assertEquals(Connection.UNKNOWN, version);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldReturnDownStatusAndUnknownVersionForClientErrorResponse() {
        try (MockWebServer server = new MockWebServer()) {
            // given:
            server.enqueue(new MockResponse()
                    .setResponseCode(404)
                    .setBody("Not Found"));

            HttpUrl url = server.url("/api/health");
            TestHttpConnection connection = new TestHttpConnection("test-service", url.toString(), 1000, 1000);
            HofundConnection hofundConnection = connection.toHofundConnection();

            // when:
            Connection con = hofundConnection.getFun().get().getConnection();
            Status status = con.getStatus();
            String version = con.getVersion();

            // then:
            assertEquals(Status.DOWN, status);
            assertEquals(Connection.UNKNOWN, version);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldReturnDownStatusAndUnknownVersionForConnectionTimeout() {
        try (MockWebServer server = new MockWebServer()) {
            // given:
            server.enqueue(new MockResponse()
                    .setSocketPolicy(okhttp3.mockwebserver.SocketPolicy.NO_RESPONSE));

            HttpUrl url = server.url("/api/health");
            TestHttpConnection connection = new TestHttpConnection("test-service", url.toString(), 100, 100);
            HofundConnection hofundConnection = connection.toHofundConnection();

            // when:
            Connection con = hofundConnection.getFun().get().getConnection();
            Status status = con.getStatus();
            String version = con.getVersion();

            // then:
            assertEquals(Status.DOWN, status);
            assertEquals(Connection.UNKNOWN, version);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldReturnDownStatusAndUnknownVersionForInvalidUrl() {
        // given:
        TestHttpConnection connection = new TestHttpConnection("test-service", "http://invalid-url-that-does-not-exist.example", 1000, 1000);
        HofundConnection hofundConnection = connection.toHofundConnection();

        // when:
        Connection con = hofundConnection.getFun().get().getConnection();
        Status status = con.getStatus();
        String version = con.getVersion();

        // then:
        assertEquals(Status.DOWN, status);
        assertEquals(Connection.UNKNOWN, version);
    }

    @Test
    void shouldReturnUpStatusAndExtractVersionFromRedirectResponse() {
        try (MockWebServer server = new MockWebServer()) {
            // given:
            server.enqueue(new MockResponse()
                    .setResponseCode(302)
                    .setBody("{\"application\":{\"version\":\"2.1.0\"},\"git\":{\"branch\":\"develop\"}}"));

            HttpUrl url = server.url("/api/health");
            TestHttpConnection connection = new TestHttpConnection("test-service", url.toString(), 1000, 1000);
            HofundConnection hofundConnection = connection.toHofundConnection();

            // when:
            Connection con = hofundConnection.getFun().get().getConnection();
            Status status = con.getStatus();
            String version = con.getVersion();

            // then:
            assertEquals(Status.UP, status);
            assertEquals("2.1.0", version);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class TestHttpConnection extends AbstractHofundBasicHttpConnection {
        private final String target;
        private final String url;
        private final int connectTimeout;
        private final int readTimeout;

        public TestHttpConnection(String target, String url, int connectTimeout, int readTimeout) {
            this.target = target;
            this.url = url;
            this.connectTimeout = connectTimeout;
            this.readTimeout = readTimeout;
        }

        @Override
        protected String getTarget() {
            return target;
        }

        @Override
        protected String getUrl() {
            return url;
        }

        @Override
        protected int getConnectTimeout() {
            return connectTimeout;
        }

        @Override
        protected int getReadTimeout() {
            return readTimeout;
        }
    }
}
