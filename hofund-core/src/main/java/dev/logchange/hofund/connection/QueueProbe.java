package dev.logchange.hofund.connection;

/**
 * A single reachability check of a queue, topic or message broker.
 * <p>
 * Hofund does not depend on any messaging client, so the probe itself is supplied by the application: open a
 * short-lived connection, browse a queue, connect a socket — whatever proves that the broker answers. Returning
 * normally means the target is UP; any exception means DOWN.
 * <p>
 * The probe runs on the thread that scrapes the metric, so it should be as cheap as a connect and must never
 * touch production traffic — a consumer created on a real destination becomes a competing consumer.
 */
@FunctionalInterface
public interface QueueProbe {

    /**
     * @throws Exception when the target cannot be reached; the connection is then reported as DOWN
     */
    void probe() throws Exception;
}
