package dev.logchange.hofund.connection;

import dev.logchange.hofund.EnvProvider;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AbstractHofundBasicQueueConnectionTest {

    private static class MockEnvProvider implements EnvProvider {

        private final String returnValue;

        MockEnvProvider(String returnValue) {
            this.returnValue = returnValue;
        }

        @Override
        public String getEnv(String name) {
            return returnValue;
        }
    }

    private static class TestConnection extends AbstractHofundBasicQueueConnection {

        private final AtomicInteger probeCalls = new AtomicInteger();

        TestConnection(EnvProvider envProvider) {
            super(envProvider);
        }

        @Override
        protected String getTarget() {
            return "amqp_broker";
        }

        @Override
        protected String getUrl() {
            return "amqp://broker.example.com:5672";
        }

        @Override
        protected void probe() {
            probeCalls.incrementAndGet();
        }
    }

    @Test
    void shouldBeInactiveAndNotProbeWhenDisabledByEnvVariable() {
        // given:
        TestConnection connection = new TestConnection(new MockEnvProvider("true"));

        // when:
        Status status = statusOf(connection);

        // then:
        assertEquals(Status.INACTIVE, status);
        assertEquals(0, connection.probeCalls.get());
    }

    @Test
    void shouldProbeWhenEnvVariableIsNotSet() {
        // given:
        TestConnection connection = new TestConnection(new MockEnvProvider(null));

        // when:
        Status status = statusOf(connection);

        // then:
        assertEquals(Status.UP, status);
        assertEquals(1, connection.probeCalls.get());
    }

    private static Status statusOf(TestConnection connection) {
        return connection.toHofundConnection().getFun().get().getConnection().getStatus();
    }
}
