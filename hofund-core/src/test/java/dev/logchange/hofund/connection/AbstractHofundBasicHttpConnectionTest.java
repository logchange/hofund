package dev.logchange.hofund.connection;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AbstractHofundBasicHttpConnectionTest {

    /**
     * AbstractHofundBasicHttpConnection is an abstract class so to test it
     * we need to create below fake class.
     */
    private static class TestableAbstractHofundBasicHttpConnection extends AbstractHofundBasicHttpConnection {

        private final String target;
        private final String url;
        private final CheckingStatus checkingStatus;
        private final RequestMethod requestMethod;
        private final List<RequestHeader> requestHeaders;

        public TestableAbstractHofundBasicHttpConnection(String target, String url, CheckingStatus checkingStatus, RequestMethod requestMethod, List<RequestHeader> requestHeaders) {
            this.target = target;
            this.url = url;
            this.checkingStatus = checkingStatus;
            this.requestMethod = requestMethod;
            this.requestHeaders = requestHeaders;
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
        protected CheckingStatus getCheckingStatus() {
            return checkingStatus;
        }

        @Override
        protected RequestMethod getRequestMethod() {
            return requestMethod;
        }

        @Override
        protected List<RequestHeader> getRequestHeaders() {
            return requestHeaders;
        }
    }

    private static class SomeHealthCheck extends AbstractHofundBasicHttpConnection {

        private final String target;
        private final String url;
        private final RequestMethod requestMethod;


        public SomeHealthCheck() {
            this.target = "some-service";
            this.url = "http://google.com/";
            this.requestMethod = RequestMethod.POST;
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
        protected RequestMethod getRequestMethod() {
            return requestMethod;
        }

        @Override
        protected CheckingStatus getCheckingStatus() {
            return CheckingStatus.INACTIVE; // zobacz niżej
        }
    }


    @Test
    void testGetMethod() {
        String expectedVersion = "1.7.14-SNAPSHOT";
        String body = "{\"application\":{\"version\":\"1.7.14-SNAPSHOT\"},\"git\":{\"branch\":\"develop\"}}";

        try (MockWebServer server = new MockWebServer()) {

            // given:
            server.enqueue(new MockResponse().setBody(body));

            HttpUrl url = server.url("/api/some/");

            TestableAbstractHofundBasicHttpConnection connection = new TestableAbstractHofundBasicHttpConnection("AlaMaKota", url.toString(), CheckingStatus.ACTIVE, RequestMethod.GET, Collections.emptyList());

            HofundConnection hofundConnection = connection.toHofundConnection();

            // when:
            HofundConnectionResult con = hofundConnection.getFun().get().getConnection();
            Status status = con.getStatus();
            String version = con.getVersion();

            // then:
            RecordedRequest request = server.takeRequest();
            assertEquals(Status.UP, status);
            assertEquals(expectedVersion, version);
            assertEquals("GET", request.getMethod());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testPostMethod() {
        try (MockWebServer server = new MockWebServer()) {

            // given:
            server.enqueue(new MockResponse()
                    .setBody("hello, world!")
            );

            HttpUrl url = server.url("/api/some/");

            TestableAbstractHofundBasicHttpConnection connection = new TestableAbstractHofundBasicHttpConnection("AlaMaKota", url.toString(), CheckingStatus.ACTIVE, RequestMethod.POST, Collections.emptyList());

            HofundConnection hofundConnection = connection.toHofundConnection();

            // when:
            HofundConnectionResult con = hofundConnection.getFun().get().getConnection();
            Status status = con.getStatus();
            String version = con.getVersion();

            // then:
            RecordedRequest request = server.takeRequest();
            assertEquals(Status.UP, status);
            assertEquals(HofundConnectionResult.UNKNOWN, version);
            assertEquals("POST", request.getMethod());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCheckingStatusInactive() {
        // given:
        TestableAbstractHofundBasicHttpConnection connection = new TestableAbstractHofundBasicHttpConnection("AlaMaKota", "https://a.b.c.d/", CheckingStatus.INACTIVE, RequestMethod.GET, Collections.emptyList());

        HofundConnection hofundConnection = connection.toHofundConnection();

        // when:
        HofundConnectionResult con = hofundConnection.getFun().get().getConnection();
        Status status = con.getStatus();
        String version = con.getVersion();

        // then:
        assertEquals(Status.INACTIVE, status);
        assertEquals(-1.0, status.getValue());
        assertEquals(HofundConnectionResult.NOT_APPLICABLE, version);
    }


    @Test
    void testCheckingStatusInactive2() {
        // given:
        SomeHealthCheck connection = new SomeHealthCheck();

        HofundConnection hofundConnection = connection.toHofundConnection();

        // when:
        HofundConnectionResult con = hofundConnection.getFun().get().getConnection();
        Status status = con.getStatus();
        String version = con.getVersion();

        // then:
        assertEquals(Status.INACTIVE, status);
        assertEquals(-1.0, status.getValue());
        assertEquals(HofundConnectionResult.NOT_APPLICABLE, version);
    }

    @Test
    void testPostMethodWithRequestHeader() {
        try (MockWebServer server = new MockWebServer()) {

            // given:
            server.enqueue(new MockResponse()
                    .setBody("hello, world!")
            );

            HttpUrl url = server.url("/api/some/");

            TestableAbstractHofundBasicHttpConnection connection = new TestableAbstractHofundBasicHttpConnection("AlaMaKota", url.toString(), CheckingStatus.ACTIVE, RequestMethod.POST, Arrays.asList(RequestHeader.of("Authorization", "Bearer 12345678")));

            HofundConnection hofundConnection = connection.toHofundConnection();

            // when:
            HofundConnectionResult con = hofundConnection.getFun().get().getConnection();
            Status status = con.getStatus();
            String version = con.getVersion();

            // then:
            RecordedRequest request = server.takeRequest();
            assertEquals(Status.UP, status);
            assertEquals(HofundConnectionResult.UNKNOWN, version);
            assertEquals("POST", request.getMethod());
            assertEquals("Bearer 12345678", request.getHeader("Authorization"));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testEnvironmentVariableDisabledNotSet() {
        try (MockWebServer server = new MockWebServer()) {
            // given:
            server.enqueue(new MockResponse().setBody("{}"));

            HttpUrl url = server.url("/api/some/");

            TestableAbstractHofundBasicHttpConnection connection = new TestableAbstractHofundBasicHttpConnection(
                    "testTarget", 
                    url.toString(), 
                    CheckingStatus.ACTIVE, 
                    RequestMethod.GET, 
                    Collections.emptyList()
            );

            HofundConnection hofundConnection = connection.toHofundConnection();

            // when:
            HofundConnectionResult con = hofundConnection.getFun().get().getConnection();
            Status status = con.getStatus();

            // then:
            // If the environment variable is not set, the connection check should proceed normally
            assertEquals(Status.UP, status);

            // Verify that a request was made (meaning the connection check wasn't skipped)
            RecordedRequest request = server.takeRequest();
            assertEquals("GET", request.getMethod());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testIsCheckingStatusInactiveByEnvs() {
        try (MockWebServer server = new MockWebServer()) {
            // given:
            server.enqueue(new MockResponse().setBody("{}"));

            HttpUrl url = server.url("/api/some/");

            // Create a subclass that overrides isCheckingStatusInactiveByEnvs to return true
            TestableAbstractHofundBasicHttpConnection connection = new TestableAbstractHofundBasicHttpConnection(
                    "testTarget", 
                    url.toString(), 
                    CheckingStatus.ACTIVE, 
                    RequestMethod.GET, 
                    Collections.emptyList()
            ) {
                @Override
                protected boolean isCheckingStatusInactiveByEnvs() {
                    return true; // Simulate environment variable being set to disable connection
                }
            };

            HofundConnection hofundConnection = connection.toHofundConnection();

            // when:
            HofundConnectionResult con = hofundConnection.getFun().get().getConnection();
            Status status = con.getStatus();
            String version = con.getVersion();

            // then:
            // If isCheckingStatusInactiveByEnvs returns true, the connection check should be skipped
            // and status should be INACTIVE
            assertEquals(Status.INACTIVE, status);
            assertEquals(HofundConnectionResult.NOT_APPLICABLE, version);

            // No request should be made to the server since the connection check is skipped
            assertEquals(0, server.getRequestCount());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
