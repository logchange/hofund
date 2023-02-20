package dev.logchange.hofund.connection.spring.http;

import dev.logchange.hofund.connection.AbstractHofundBasicHttpConnection;
import dev.logchange.hofund.connection.HofundConnection;
import dev.logchange.hofund.connection.HofundConnectionsProvider;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class HofundBasicHttpConnectionProvider implements HofundConnectionsProvider {

    private final List<AbstractHofundBasicHttpConnection> connections;

    @Override
    public List<HofundConnection> getConnections() {
        if (connections == null) {
            return Collections.emptyList();
        }
        return connections.stream()
                .map(AbstractHofundBasicHttpConnection::toHofundConnection)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
