package dev.logchange.hofund.connection.spring.queue;

import dev.logchange.hofund.connection.AbstractHofundBasicQueueConnection;
import dev.logchange.hofund.connection.HofundConnection;
import dev.logchange.hofund.connection.HofundConnectionsProvider;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HofundBasicQueueConnectionProvider implements HofundConnectionsProvider {

    private final List<HofundConnection> connections;

    public HofundBasicQueueConnectionProvider(List<AbstractHofundBasicQueueConnection> connections) {
        this.connections = connections.stream()
                .map(AbstractHofundBasicQueueConnection::toHofundConnection)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<HofundConnection> getConnections() {
        return connections;
    }
}
