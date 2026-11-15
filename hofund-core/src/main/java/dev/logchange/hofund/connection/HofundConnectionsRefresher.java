package dev.logchange.hofund.connection;

import dev.logchange.hofund.EnvProvider;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import static dev.logchange.hofund.connection.CheckingStatusEnvs.isTrue;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Swaps every connection function for a {@link CachedConnectionFunction} so the probes run here instead of on
 * the thread that scrapes the metric.
 * <p>
 * Providers build their list once and hand out the same {@link HofundConnection} instances to every consumer,
 * so replacing the function inside {@link HofundConnection#getFun()} reaches all the meters and the startup
 * table at once, without any of them knowing about it.
 * <p>
 * The pool holds one thread per connection: a probe stuck in DNS or in a socket read occupies its own thread
 * until it gives up, and with a fixed delay it can never queue a second run of itself, so the thread count is
 * bounded by construction.
 * <p>
 * Both environment variables are plural on purpose - {@code HOFUND_CONNECTION_<TARGET>_DISABLED} is singular,
 * so a connection with the target {@code refresh} cannot collide with the switch below.
 *
 * <ul>
 *   <li>{@code HOFUND_CONNECTIONS_REFRESH_DISABLED} - {@code true} or {@code 1} goes back to probing on the
 *       thread that scrapes the metric</li>
 *   <li>{@code HOFUND_CONNECTIONS_REFRESH_INTERVAL_MILLIS} - how often the background probe runs</li>
 * </ul>
 */
public class HofundConnectionsRefresher implements AutoCloseable {

    private static final Logger log = getLogger(HofundConnectionsRefresher.class);

    static final String DISABLED_ENV = "HOFUND_CONNECTIONS_REFRESH_DISABLED";
    static final String INTERVAL_ENV = "HOFUND_CONNECTIONS_REFRESH_INTERVAL_MILLIS";
    static final long DEFAULT_INTERVAL_MILLIS = 120_000;

    private final ScheduledExecutorService scheduler;
    private final List<CachedConnectionFunction> cached = new ArrayList<>();

    public HofundConnectionsRefresher(List<HofundConnectionsProvider> connectionsProviders) {
        this(HofundConnections.from(connectionsProviders), new EnvProvider.SystemEnvProvider());
    }

    HofundConnectionsRefresher(List<HofundConnection> connections, EnvProvider envProvider) {
        this.scheduler = newScheduler(Math.max(1, connections.size()));

        if (isTrue(envProvider.getEnv(DISABLED_ENV))) {
            log.info("Background connection refresh is disabled by {}, connections are probed on the thread that scrapes the metric", DISABLED_ENV);
            return;
        }

        long intervalMillis = intervalMillis(envProvider);

        for (HofundConnection connection : connections) {
            cache(connection, intervalMillis);
        }

        log.info("Hofund refreshes {} connection(s) in the background every {} ms, set {} to change it",
                cached.size(), intervalMillis, INTERVAL_ENV);
    }

    private void cache(HofundConnection connection, long intervalMillis) {
        ConnectionFunction current = connection.getFun().get();

        if (current instanceof CachedConnectionFunction) {
            log.debug("Connection to {} is already refreshed in the background, skipping", connection.getTarget());
            return;
        }

        CachedConnectionFunction cachedFunction = new CachedConnectionFunction(current, scheduler, intervalMillis);
        connection.getFun().set(cachedFunction);
        cached.add(cachedFunction);
    }

    @Override
    public void close() {
        cached.forEach(CachedConnectionFunction::close);
        scheduler.shutdownNow();
    }

    private static long intervalMillis(EnvProvider envProvider) {
        String configured = envProvider.getEnv(INTERVAL_ENV);

        if (configured == null || configured.trim().isEmpty()) {
            return DEFAULT_INTERVAL_MILLIS;
        }

        try {
            long interval = Long.parseLong(configured.trim());
            if (interval <= 0) {
                throw new NumberFormatException("must be positive");
            }
            return interval;
        } catch (NumberFormatException e) {
            log.warn("Invalid {}={}, falling back to {} ms", INTERVAL_ENV, configured, DEFAULT_INTERVAL_MILLIS);
            return DEFAULT_INTERVAL_MILLIS;
        }
    }

    private static ScheduledExecutorService newScheduler(int threads) {
        return Executors.newScheduledThreadPool(threads, new ThreadFactory() {

            private final AtomicInteger counter = new AtomicInteger();

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "hofund-connection-refresh-" + counter.incrementAndGet());
                thread.setDaemon(true);
                return thread;
            }
        });
    }
}
