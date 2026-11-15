package dev.logchange.hofund.connection;

import dev.logchange.hofund.EnvProvider;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertSame;

class HofundConnectionsRefresherTest {

    private static final EnvProvider DEFAULTS = name -> null;

    @Test
    void shouldReplaceConnectionFunctionWithCachedOne() {
        // given:
        AtomicInteger calls = new AtomicInteger();
        HofundConnection connection = connection(calls);

        // when:
        try (HofundConnectionsRefresher refresher = new HofundConnectionsRefresher(singletonList(connection), DEFAULTS)) {
            // then:
            assertInstanceOf(CachedConnectionFunction.class, connection.getFun().get());
            assertEquals(Status.UP, connection.getFun().get().getConnection().getStatus());
            assertEquals(1, calls.get(), "reading the cached value must not reach the dependency again");
        }
    }

    @Test
    void shouldNotWrapTwiceWhenAppliedAgain() {
        // given: every meter builds its own list, but providers hand out the same connection instances
        AtomicInteger calls = new AtomicInteger();
        HofundConnection connection = connection(calls);

        try (HofundConnectionsRefresher first = new HofundConnectionsRefresher(singletonList(connection), DEFAULTS)) {
            ConnectionFunction wrapped = connection.getFun().get();

            // when:
            try (HofundConnectionsRefresher second = new HofundConnectionsRefresher(singletonList(connection), DEFAULTS)) {
                // then:
                assertSame(wrapped, connection.getFun().get());
                assertEquals(1, calls.get());
            }
        }
    }

    @Test
    void shouldHandleNoConnections() {
        List<HofundConnection> none = Collections.emptyList();

        try (HofundConnectionsRefresher refresher = new HofundConnectionsRefresher(none, DEFAULTS)) {
            assertEquals(0, none.size());
        }
    }

    @Test
    void shouldLeaveTheOldMechanismInPlaceWhenDisabledByEnvs() {
        // given:
        AtomicInteger calls = new AtomicInteger();
        HofundConnection connection = connection(calls);
        ConnectionFunction original = connection.getFun().get();
        EnvProvider disabled = name -> HofundConnectionsRefresher.DISABLED_ENV.equals(name) ? "true" : null;

        // when:
        try (HofundConnectionsRefresher refresher = new HofundConnectionsRefresher(singletonList(connection), disabled)) {
            // then: nothing was wrapped, so every read still probes as before
            assertNotNull(refresher);
            assertSame(original, connection.getFun().get());
            assertEquals(0, calls.get());

            connection.getFun().get().getConnection();
            connection.getFun().get().getConnection();
            assertEquals(2, calls.get());
        }
    }

    @Test
    void shouldReadIntervalFromEnvs() {
        // given:
        AtomicInteger calls = new AtomicInteger();
        HofundConnection connection = connection(calls);
        EnvProvider fastRefresh = name -> HofundConnectionsRefresher.INTERVAL_ENV.equals(name) ? "50" : null;

        // when:
        try (HofundConnectionsRefresher refresher = new HofundConnectionsRefresher(singletonList(connection), fastRefresh)) {
            // then:
            assertNotNull(refresher);
            assertTrue(awaitCalls(calls, 2), "background refresh did not pick the interval up from the env, calls: " + calls.get());
        }
    }

    @Test
    void shouldFallBackToDefaultIntervalWhenEnvIsGarbage() {
        // given:
        AtomicInteger calls = new AtomicInteger();
        HofundConnection connection = connection(calls);
        EnvProvider garbage = name -> HofundConnectionsRefresher.INTERVAL_ENV.equals(name) ? "not-a-number" : null;

        // when:
        try (HofundConnectionsRefresher refresher = new HofundConnectionsRefresher(singletonList(connection), garbage)) {
            // then: still wrapped, just on the default interval
            assertInstanceOf(CachedConnectionFunction.class, connection.getFun().get());
            assertEquals(1, calls.get());
        }
    }

    private static boolean awaitCalls(AtomicInteger calls, int expected) {
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);

        while (System.nanoTime() < deadline) {
            if (calls.get() >= expected) {
                return true;
            }
            Thread.yield();
        }

        return false;
    }

    private static HofundConnection connection(AtomicInteger calls) {
        ConnectionFunction fun = () -> {
            calls.incrementAndGet();
            return HofundConnectionResult.http(Status.UP, HofundConnectionResult.UNKNOWN);
        };

        return new HofundConnection("target", "http://localhost/health", Type.HTTP, new AtomicReference<>(fun), "");
    }
}
