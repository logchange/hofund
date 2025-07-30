package dev.logchange.hofund.connection;

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
                        "Some response without version information",
                        "UNKNOWN"
                ),
                Arguments.of(
                        "{\"application\":{\"name\":\"AntyFraud\",\"version\":\"25.3.1-SNAPSHOT\"},\"git\":{\"branch\":\"main\",\"commit\":{\"id\":\"b282301\",\"time\":\"2025-07-28T14:24:39Z\"}}}",
                        "25.3.1-SNAPSHOT"
                ),
                Arguments.of(
                        "{\"application\":{\"name\":\"TestApp\",\"version\":\"1.0.0\"},\"otherField\":\"value\"}",
                        "1.0.0"
                ),
                Arguments.of(
                        "{\"application\":{\"version\":\"2.0.0-RC1\"},\"git\":{\"branch\":\"develop\"}}",
                        "2.0.0-RC1"
                ),
                Arguments.of(
                        "{\"someField\":\"value\",\"application\":{\"name\":\"App\",\"version\":\"3.2.1+build.456\"}}",
                        "3.2.1+build.456"
                ),
                Arguments.of(
                        "{\"someField\":\"value\",\"application\":{\"name\":\"App\",\"version\":\"\"}}",
                        "UNKNOWN"
                )
        );
    }
}
