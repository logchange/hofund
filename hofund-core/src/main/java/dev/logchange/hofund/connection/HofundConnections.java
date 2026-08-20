package dev.logchange.hofund.connection;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Collects the connections of every provider into one list, skipping what would later break metric binding.
 * <p>
 * A provider is free to return {@code null} or a list containing {@code null} — f.e.
 * {@link AbstractHofundBasicHttpConnection#toHofundConnection()} returns {@code null} when the connection
 * cannot be created. Such an entry used to reach the meters and fail the whole binding with a
 * {@link NullPointerException}, taking every other connection down with it, so it is dropped here and logged.
 */
public final class HofundConnections {

    private static final Logger log = getLogger(HofundConnections.class);

    private HofundConnections() {
    }

    public static List<HofundConnection> from(List<HofundConnectionsProvider> connectionsProviders) {
        if (connectionsProviders == null || connectionsProviders.isEmpty()) {
            return Collections.emptyList();
        }

        List<HofundConnection> result = new ArrayList<>();
        for (HofundConnectionsProvider provider : connectionsProviders) {
            if (provider == null) {
                log.warn("Null connections provider, skipping");
                continue;
            }

            List<HofundConnection> connections = provider.getConnections();
            if (connections == null) {
                log.warn("Connections provider {} returned null, skipping", provider.getClass().getName());
                continue;
            }

            for (HofundConnection connection : connections) {
                if (connection == null) {
                    log.warn("Connections provider {} returned a null connection, skipping", provider.getClass().getName());
                    continue;
                }
                result.add(connection);
            }
        }

        return result;
    }
}
