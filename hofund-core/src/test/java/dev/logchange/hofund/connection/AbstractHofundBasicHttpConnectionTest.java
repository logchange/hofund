package dev.logchange.hofund.connection;

import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AbstractHofundBasicHttpConnectionTest {

    /**
     * AbstractHofundBasicHttpConnection is an abstract class so to test it
     * we need to create below fake class.
     */
    @RequiredArgsConstructor
    private static class TestableAbstractHofundBasicHttpConnection extends AbstractHofundBasicHttpConnection {

        private final String target;
        private final String url;
        private final CheckingStatus checkingStatus;
        private final RequestMethod requestMethod;

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
    }

    @Test
    void testGetMethod() {
        try (MockWebServer server = new MockWebServer()) {

            // given:
            server.enqueue(new MockResponse()
                    .setBody("hello, world!")
            );

            HttpUrl url = server.url("/api/some/");

            TestableAbstractHofundBasicHttpConnection connection = new TestableAbstractHofundBasicHttpConnection("AlaMaKota", url.toString(), CheckingStatus.ACTIVE, RequestMethod.GET);

            HofundConnection hofundConnection = connection.toHofundConnection();

            // when:
            Status status = hofundConnection.getFun().get().getStatus();

            // then:
            RecordedRequest request = server.takeRequest();
            assertEquals(Status.UP, status);
            assertEquals(request.getMethod(), "GET");
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

            TestableAbstractHofundBasicHttpConnection connection = new TestableAbstractHofundBasicHttpConnection("AlaMaKota", url.toString(), CheckingStatus.ACTIVE, RequestMethod.POST);

            HofundConnection hofundConnection = connection.toHofundConnection();

            // when:
            Status status = hofundConnection.getFun().get().getStatus();

            // then:
            RecordedRequest request = server.takeRequest();
            assertEquals(Status.UP, status);
            assertEquals(request.getMethod(), "POST");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCheckingStatusInactive() {
        // given:
        TestableAbstractHofundBasicHttpConnection connection = new TestableAbstractHofundBasicHttpConnection("AlaMaKota", "https://a.b.c.d/", CheckingStatus.INACTIVE, RequestMethod.GET);

        HofundConnection hofundConnection = connection.toHofundConnection();

        // when:
        Status status = hofundConnection.getFun().get().getStatus();

        // then:
        assertEquals(Status.INACTIVE, status);
    }

}