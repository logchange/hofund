package dev.logchange.hofund.connection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AbstractHofundBasicHttpConnectionVersionTest {

    private static class TestVersionExtractor extends AbstractHofundBasicHttpConnection {
        @Override
        protected String getTarget() {
            return "test-target";
        }

        @Override
        protected String getUrl() {
            return "http://test-url.com";
        }

        @Override
        public String extractVersionFromResponse(String responseBody) {
            return super.extractVersionFromResponse(responseBody);
        }
    }

    private final TestVersionExtractor extractor = new TestVersionExtractor();

    @ParameterizedTest
    @NullAndEmptySource
    void shouldReturnUnknownForNullOrEmptyResponse(String responseBody) {
        // when:
        String version = extractor.extractVersionFromResponse(responseBody);

        // then:
        assertEquals(Connection.UNKNOWN, version);
    }

    @Test
    void shouldReturnUnknownWhenNoApplicationVersionFound() {
        // given:
        String responseBody = "Some response without version information";

        // when:
        String version = extractor.extractVersionFromResponse(responseBody);

        // then:
        assertEquals(Connection.UNKNOWN, version);
    }

    @Test
    void shouldReturnUnknownWhenApplicationVersionFoundButNoEquals() {
        // given:
        String responseBody = "Some response with application_version but no equals sign";

        // when:
        String version = extractor.extractVersionFromResponse(responseBody);

        // then:
        assertEquals(Connection.UNKNOWN, version);
    }

    @Test
    void shouldReturnUnknownWhenApplicationVersionFoundButNoOpenQuote() {
        // given:
        String responseBody = "Some response with application_version=but no open quote";

        // when:
        String version = extractor.extractVersionFromResponse(responseBody);

        // then:
        assertEquals(Connection.UNKNOWN, version);
    }

    @Test
    void shouldReturnUnknownWhenApplicationVersionFoundButNoCloseQuote() {
        // given:
        String responseBody = "Some response with application_version=\"but no close quote";

        // when:
        String version = extractor.extractVersionFromResponse(responseBody);

        // then:
        assertEquals(Connection.UNKNOWN, version);
    }

    @ParameterizedTest
    @MethodSource("provideValidVersionResponses")
    void shouldExtractVersionCorrectly(String responseBody, String expectedVersion) {
        // when:
        String version = extractor.extractVersionFromResponse(responseBody);

        // then:
        assertEquals(expectedVersion, version);
    }

    private static Stream<Arguments> provideValidVersionResponses() {
        return Stream.of(
            Arguments.of(
                "# HELP hofund_info Basic information about application\n" +
                "# TYPE hofund_info gauge\n" +
                "hofund_info{application_name=\"stats\",application_version=\"1.7.14-SNAPSHOT\",id=\"stats\"} 1\n" +
                "# HELP hofund_connection Current status of given connection\n" +
                "# TYPE hofund_connection gauge",
                "1.7.14-SNAPSHOT"
            ),
            Arguments.of(
                "hofund_info{application_name=\"stats\",application_version=\"2.0.0\",id=\"stats\"} 1",
                "2.0.0"
            ),
            Arguments.of(
                "hofund_info{application_name=\"stats\",application_version=\"1.0.0-beta.1+build.123\",id=\"stats\"} 1",
                "1.0.0-beta.1+build.123"
            ),
            Arguments.of(
                "application_version=\"3.1.4\" and some other text",
                "3.1.4"
            ),
            Arguments.of(
                "Some text and then application_version=\"5.0.1\"",
                "5.0.1"
            ),
            Arguments.of(
                "application_version=\"1.0.0\" and then application_version=\"2.0.0\"",
                "1.0.0"
            )
        );
    }
}
