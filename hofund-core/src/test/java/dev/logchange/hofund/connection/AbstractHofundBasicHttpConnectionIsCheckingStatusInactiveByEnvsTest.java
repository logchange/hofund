package dev.logchange.hofund.connection;

import dev.logchange.hofund.EnvProvider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbstractHofundBasicHttpConnectionIsCheckingStatusInactiveByEnvsTest {

    private static class MockEnvProvider implements EnvProvider {
        private final String returnValue;

        public MockEnvProvider(String returnValue) {
            this.returnValue = returnValue;
        }

        @Override
        public String getEnv(String name) {
            return returnValue;
        }
    }

    private static class TestConnection extends AbstractHofundBasicHttpConnection {
        private final String target;

        public TestConnection(String target, EnvProvider envProvider) {
            super(envProvider);
            this.target = target;
        }

        @Override
        protected String getTarget() {
            return target;
        }

        @Override
        protected String getUrl() {
            return "http://example.com";
        }
    }

    @Test
    void shouldReturnTrueWhenEnvVarIsTrue() {
        // given:
        EnvProvider envProvider = new MockEnvProvider("true");
        TestConnection connection = new TestConnection("test", envProvider);

        // when:
        boolean result = connection.isCheckingStatusInactiveByEnvs();

        // then:
        assertTrue(result, "Should return true when env var is 'true'");
    }

    @Test
    void shouldReturnTrueWhenEnvVarIsTrueUpperCase() {
        // given:
        EnvProvider envProvider = new MockEnvProvider("TRUE");
        TestConnection connection = new TestConnection("test", envProvider);

        // when:
        boolean result = connection.isCheckingStatusInactiveByEnvs();

        // then:
        assertTrue(result, "Should return true when env var is 'TRUE'");
    }

    @Test
    void shouldReturnTrueWhenEnvVarIsOne() {
        // given:
        EnvProvider envProvider = new MockEnvProvider("1");
        TestConnection connection = new TestConnection("test", envProvider);

        // when:
        boolean result = connection.isCheckingStatusInactiveByEnvs();

        // then:
        assertTrue(result, "Should return true when env var is '1'");
    }

    @Test
    void shouldReturnFalseWhenEnvVarIsFalse() {
        // given:
        EnvProvider envProvider = new MockEnvProvider("false");
        TestConnection connection = new TestConnection("test", envProvider);

        // when:
        boolean result = connection.isCheckingStatusInactiveByEnvs();

        // then:
        assertFalse(result, "Should return false when env var is 'false'");
    }

    @Test
    void shouldReturnFalseWhenEnvVarIsEmpty() {
        // given:
        EnvProvider envProvider = new MockEnvProvider("");
        TestConnection connection = new TestConnection("test", envProvider);

        // when:
        boolean result = connection.isCheckingStatusInactiveByEnvs();

        // then:
        assertFalse(result, "Should return false when env var is empty");
    }

    @Test
    void shouldReturnFalseWhenEnvVarIsNull() {
        // given:
        EnvProvider envProvider = new MockEnvProvider(null);
        TestConnection connection = new TestConnection("test", envProvider);

        // when:
        boolean result = connection.isCheckingStatusInactiveByEnvs();

        // then:
        assertFalse(result, "Should return false when env var is null");
    }

    @Test
    void shouldConstructCorrectEnvVarName() {
        // given:
        final String[] capturedName = new String[1];
        EnvProvider envProvider = name -> {
            capturedName[0] = name;
            return "true";
        };
        TestConnection connection = new TestConnection("testTarget", envProvider);

        // when:
        connection.isCheckingStatusInactiveByEnvs();

        // then:
        assertEquals("HOFUND_CONNECTION_TESTTARGET_DISABLED", capturedName[0], 
                "Should construct correct env var name");
    }
}
