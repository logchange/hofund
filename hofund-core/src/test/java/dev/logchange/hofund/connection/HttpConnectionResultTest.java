package dev.logchange.hofund.connection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class HttpConnectionResultTest {

    @Mock
    private HttpURLConnection connection;

    @Test
    void shouldReturnUnknown_whenInputStreamThrowsIOException() throws Exception {
        // given:
        IOException streamError = new IOException("stream error");
        URL url = new URL("http://test-url.com");
        doThrow(streamError).when(connection).getInputStream();
        doReturn(url).when(connection).getURL();

        // when:
        HofundConnectionResult hofundConnectionResult = HofundConnectionResult.http(Status.UP, connection);

        // then:
        assertEquals(HofundConnectionResult.UNKNOWN, hofundConnectionResult.getVersion().toString());
    }

    @Test
    void shouldReturnUnknown_whenInputStreamIsEmpty() throws Exception {
        // given:
        InputStream emptyStream = new ByteArrayInputStream(new byte[0]);
        doReturn(emptyStream).when(connection).getInputStream();

        // when:
        HofundConnectionResult hofundConnectionResult = HofundConnectionResult.http(Status.UP, connection);

        // then:
        assertEquals(HofundConnectionResult.UNKNOWN, hofundConnectionResult.getVersion().toString());
    }

    @ParameterizedTest
    @MethodSource("provideValidVersionResponses")
    void shouldExtractVersionCorrectly(String responseBody, String expectedVersion) throws IOException {
        // given:
        InputStream inputStream = new ByteArrayInputStream(responseBody.getBytes());
        doReturn(inputStream).when(connection).getInputStream();

        HofundConnectionResult hofundConnectionResult = HofundConnectionResult.http(Status.UP, connection);

        // then:
        assertEquals(expectedVersion, hofundConnectionResult.getVersion().toString());
    }

    private static Stream<Arguments> provideValidVersionResponses() {
        return Stream.of(
                Arguments.of(
                        "Some response without version information",
                        HofundConnectionResult.UNKNOWN
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
                        HofundConnectionResult.UNKNOWN
                )
        );
    }
}
