package dev.logchange.hofund.connection;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HofundConnectionsTest {

    @Test
    void shouldReturnEmptyListForNoProviders() {
        assertTrue(HofundConnections.from(null).isEmpty());
        assertTrue(HofundConnections.from(Collections.emptyList()).isEmpty());
    }

    @Test
    void shouldSkipNullProvider() {
        // given:
        List<HofundConnectionsProvider> providers = Arrays.asList(null, provider(connection("target1")));

        // when:
        List<HofundConnection> result = HofundConnections.from(providers);

        // then:
        assertEquals(1, result.size());
        assertEquals("target1", result.get(0).getTarget());
    }

    @Test
    void shouldSkipProviderReturningNull() {
        // given:
        List<HofundConnectionsProvider> providers = Arrays.asList(() -> null, provider(connection("target1")));

        // when:
        List<HofundConnection> result = HofundConnections.from(providers);

        // then:
        assertEquals(1, result.size());
        assertEquals("target1", result.get(0).getTarget());
    }

    @Test
    void shouldSkipNullConnectionInsideProviderList() {
        // given:
        List<HofundConnection> withNull = new ArrayList<>();
        withNull.add(null);
        withNull.add(connection("target2"));

        // when:
        List<HofundConnection> result = HofundConnections.from(Collections.singletonList(() -> withNull));

        // then:
        assertEquals(1, result.size());
        assertEquals("target2", result.get(0).getTarget());
    }

    @Test
    void shouldKeepEveryConnectionOfEveryProvider() {
        // given:
        List<HofundConnectionsProvider> providers =
                Arrays.asList(provider(connection("target1")), provider(connection("target2")));

        // when:
        List<HofundConnection> result = HofundConnections.from(providers);

        // then:
        assertEquals(2, result.size());
    }

    private static HofundConnectionsProvider provider(HofundConnection connection) {
        return () -> Collections.singletonList(connection);
    }

    private static HofundConnection connection(String target) {
        return new HofundConnection(
                target,
                "fake",
                Type.QUEUE,
                new AtomicReference<>(() -> HofundConnectionResult.queue(Status.UP)),
                ""
        );
    }
}
