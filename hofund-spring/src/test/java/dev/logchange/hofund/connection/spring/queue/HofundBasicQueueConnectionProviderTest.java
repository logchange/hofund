package dev.logchange.hofund.connection.spring.queue;

import dev.logchange.hofund.connection.AbstractHofundBasicQueueConnection;
import dev.logchange.hofund.connection.HofundConnection;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HofundBasicQueueConnectionProviderTest {

    private static class TestConnection extends AbstractHofundBasicQueueConnection {

        private final String target;

        TestConnection(String target) {
            this.target = target;
        }

        @Override
        protected String getTarget() {
            return target;
        }

        @Override
        protected String getUrl() {
            return "amqp://broker.example.com:5672";
        }

        @Override
        protected void probe() {
        }
    }

    private static class NullConnection extends TestConnection {

        NullConnection() {
            super("null_broker");
        }

        @Override
        public HofundConnection toHofundConnection() {
            return null;
        }
    }

    @Test
    void shouldReturnEmptyListForNoConnections() {
        assertTrue(new HofundBasicQueueConnectionProvider(Collections.emptyList()).getConnections().isEmpty());
    }

    @Test
    void shouldMapConnectionsAndSkipNullOnes() {
        // given:
        List<AbstractHofundBasicQueueConnection> connections =
                Arrays.asList(new TestConnection("amqp_broker"), new NullConnection());

        // when:
        List<HofundConnection> result = new HofundBasicQueueConnectionProvider(connections).getConnections();

        // then:
        assertEquals(1, result.size());
        assertEquals("amqp_broker", result.get(0).getTarget());
    }
}
