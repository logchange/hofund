package dev.logchange.hofund.connection;

import org.slf4j.Logger;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static dev.logchange.hofund.connection.HofundConnectionResult.UNKNOWN;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Moves the probing off the thread that scrapes the metric.
 * <p>
 * The delegate is called once up front, so the value is ready as soon as this function exists, and then
 * refreshed in the background. {@link #getConnection()} only ever returns the last known result, which
 * keeps a scrape O(1) no matter how slow or how many the dependencies are - a probe can no longer push the
 * whole endpoint past Prometheus {@code scrape_timeout} and take every other metric of the application down
 * with it.
 * <p>
 * The refresh is scheduled with a fixed <b>delay</b>, not at a fixed rate: a probe that hangs must not queue
 * up a burst of catch-up runs the moment it returns.
 */
public class CachedConnectionFunction implements ConnectionFunction, AutoCloseable {

    private static final Logger log = getLogger(CachedConnectionFunction.class);

    private final AtomicReference<HofundConnectionResult> last;
    private final ScheduledFuture<?> refresh;

    public CachedConnectionFunction(ConnectionFunction delegate, ScheduledExecutorService scheduler, long intervalMillis) {
        this.last = new AtomicReference<>(probe(delegate));
        this.refresh = scheduler.scheduleWithFixedDelay(
                () -> last.set(probe(delegate)), intervalMillis, intervalMillis, TimeUnit.MILLISECONDS);
    }

    @Override
    public HofundConnectionResult getConnection() {
        return last.get();
    }

    @Override
    public void close() {
        refresh.cancel(false);
    }

    /**
     * scheduleWithFixedDelay silently cancels the task if it throws, which would freeze the metric on its last
     * value forever with nothing in the logs, so nothing is allowed to escape from here.
     */
    private static HofundConnectionResult probe(ConnectionFunction delegate) {
        try {
            return delegate.getConnection();
        } catch (Exception e) {
            log.warn("Error refreshing connection status, reporting DOWN, msg: {}", e.getMessage());
            log.debug("Exception: ", e);
            return HofundConnectionResult.http(Status.DOWN, UNKNOWN);
        }
    }
}
