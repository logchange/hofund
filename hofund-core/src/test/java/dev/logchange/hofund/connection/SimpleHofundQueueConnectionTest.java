package dev.logchange.hofund.connection;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleHofundQueueConnectionTest {

    private static final String TARGET = "amqp_broker";
    private static final String URL = "amqp://broker.example.com:5672";

    @Test
    void shouldExposeQueueConnectionWithoutDescriptionByDefault() {
        // given:
        SimpleHofundQueueConnection connection = new SimpleHofundQueueConnection(TARGET, URL, () -> {
        });

        // when:
        HofundConnection result = connection.toHofundConnection();

        // then:
        assertEquals(TARGET, result.getTarget());
        assertEquals(URL, result.getUrl());
        assertEquals(Type.QUEUE, result.getType());
        assertEquals("", result.getDescription());
        assertEquals("amqp_broker_queue", result.toTargetTag());
    }

    @Test
    void shouldIncludeDescriptionInTargetTagWhenGiven() {
        // given:
        SimpleHofundQueueConnection connection =
                new SimpleHofundQueueConnection(TARGET, URL, () -> {
                }, "AMQP message broker");

        // when:
        HofundConnection result = connection.toHofundConnection();

        // then:
        assertEquals("amqp_broker_queue_amqp message broker", result.toTargetTag());
    }

    @Test
    void shouldBeUpWhenProbeReturnsNormally() {
        // given:
        SimpleHofundQueueConnection connection = new SimpleHofundQueueConnection(TARGET, URL, () -> {
        });

        // when:
        Status status = statusOf(connection);

        // then:
        assertEquals(Status.UP, status);
    }

    @Test
    void shouldBeDownWhenProbeThrows() {
        // given:
        SimpleHofundQueueConnection connection = new SimpleHofundQueueConnection(TARGET, URL, () -> {
            throw new IllegalStateException("broker down");
        });

        // when:
        Status status = statusOf(connection);

        // then:
        assertEquals(Status.DOWN, status);
    }

    @Test
    void shouldBeInactiveAndNotProbeWhenCheckingStatusIsInactive() {
        // given:
        AtomicInteger probeCalls = new AtomicInteger();
        SimpleHofundQueueConnection connection = new SimpleHofundQueueConnection(TARGET, URL,
                probeCalls::incrementAndGet, CheckingStatus.INACTIVE);

        // when:
        Status status = statusOf(connection);

        // then:
        assertEquals(Status.INACTIVE, status);
        assertEquals(0, probeCalls.get());
    }

    @Test
    void shouldReportNotApplicableVersion() {
        // given:
        SimpleHofundQueueConnection connection = new SimpleHofundQueueConnection(TARGET, URL, () -> {
        });

        // when:
        HofundConnectionResult result = connection.toHofundConnection().getFun().get().getConnection();

        // then:
        assertEquals(HofundConnectionResult.NOT_APPLICABLE, result.getVersion().toString());
    }

    private static Status statusOf(SimpleHofundQueueConnection connection) {
        return connection.toHofundConnection().getFun().get().getConnection().getStatus();
    }
}
