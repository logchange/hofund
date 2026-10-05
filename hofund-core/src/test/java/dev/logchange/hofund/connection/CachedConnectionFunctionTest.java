package dev.logchange.hofund.connection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CachedConnectionFunctionTest {

    private static final long INTERVAL = 100;
    private static final long AWAIT_SECONDS = 5;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @AfterEach
    void tearDown() {
        scheduler.shutdownNow();
    }

    @Test
    void shouldProbeOnceOnCreationSoTheValueIsReadyImmediately() {
        // given:
        CountingConnectionFunction delegate = new CountingConnectionFunction(Status.UP);

        // when:
        try (CachedConnectionFunction cached = new CachedConnectionFunction(delegate, scheduler, TimeUnit.MINUTES.toMillis(2))) {
            // then:
            assertEquals(Status.UP, cached.getConnection().getStatus());
            assertEquals(1, delegate.calls());
        }
    }

    @Test
    void shouldServeCachedValueWithoutTouchingDelegate() {
        // given:
        CountingConnectionFunction delegate = new CountingConnectionFunction(Status.UP);

        try (CachedConnectionFunction cached = new CachedConnectionFunction(delegate, scheduler, TimeUnit.MINUTES.toMillis(2))) {
            // when: a scrape reads the gauge many times
            for (int i = 0; i < 100; i++) {
                cached.getConnection();
            }

            // then: the dependency was hit only by the initial probe
            assertEquals(1, delegate.calls());
        }
    }

    @Test
    void shouldRefreshInBackground() throws InterruptedException {
        // given:
        CountingConnectionFunction delegate = new CountingConnectionFunction(Status.DOWN);

        try (CachedConnectionFunction cached = new CachedConnectionFunction(delegate, scheduler, INTERVAL)) {
            delegate.setStatus(Status.UP);

            // when / then:
            assertTrue(awaitStatus(cached, Status.UP), "cached value was not refreshed, still " + cached.getConnection().getStatus());
        }
    }

    @Test
    void shouldKeepRefreshingAfterDelegateThrows() throws InterruptedException {
        // given: scheduleWithFixedDelay cancels a task that throws, which would freeze the metric forever
        CountingConnectionFunction delegate = new CountingConnectionFunction(Status.UP);

        try (CachedConnectionFunction cached = new CachedConnectionFunction(delegate, scheduler, INTERVAL)) {
            delegate.failOnce();

            // when: the failing refresh is reported as DOWN
            assertTrue(awaitStatus(cached, Status.DOWN), "failing probe was not reported as DOWN");

            // then: the schedule survived it and recovers on its own
            assertTrue(awaitStatus(cached, Status.UP), "refreshing stopped after the delegate threw");
        }
    }

    @Test
    void shouldStopRefreshingAfterClose() throws InterruptedException {
        // given:
        CountingConnectionFunction delegate = new CountingConnectionFunction(Status.UP);
        CachedConnectionFunction cached = new CachedConnectionFunction(delegate, scheduler, INTERVAL);
        assertTrue(delegate.awaitSecondCall(), "background refresh did not run");

        // when:
        cached.close();
        Thread.sleep(INTERVAL);
        int afterClose = delegate.calls();
        Thread.sleep(INTERVAL * 5);

        // then:
        assertEquals(afterClose, delegate.calls());
    }

    private static boolean awaitStatus(CachedConnectionFunction cached, Status expected) throws InterruptedException {
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(AWAIT_SECONDS);

        while (System.nanoTime() < deadline) {
            if (cached.getConnection().getStatus() == expected) {
                return true;
            }
            Thread.sleep(INTERVAL / 5);
        }

        return false;
    }

    private static class CountingConnectionFunction implements ConnectionFunction {

        private final AtomicInteger calls = new AtomicInteger();
        private final CountDownLatch secondCall = new CountDownLatch(2);
        private volatile Status status;
        private volatile boolean failNext;

        private CountingConnectionFunction(Status status) {
            this.status = status;
        }

        @Override
        public HofundConnectionResult getConnection() {
            calls.incrementAndGet();
            secondCall.countDown();

            if (failNext) {
                failNext = false;
                throw new IllegalStateException("boom");
            }

            return HofundConnectionResult.http(status, HofundConnectionResult.UNKNOWN);
        }

        private void setStatus(Status status) {
            this.status = status;
        }

        private void failOnce() {
            this.failNext = true;
        }

        private int calls() {
            return calls.get();
        }

        private boolean awaitSecondCall() throws InterruptedException {
            return secondCall.await(AWAIT_SECONDS, TimeUnit.SECONDS);
        }
    }
}
