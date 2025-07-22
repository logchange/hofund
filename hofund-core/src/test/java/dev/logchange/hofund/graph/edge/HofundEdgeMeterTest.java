package dev.logchange.hofund.graph.edge;

import dev.logchange.hofund.connection.*;
import dev.logchange.hofund.info.HofundInfoProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

class HofundEdgeMeterTest {

    @Test
    void whenNodeIdsCollides_shouldThrowException() {
        // given:
        HofundInfoProvider infoProvider = new HofundInfoProvider() {
            @Override
            public String getApplicationName() {
                return "abc";
            }

            @Override
            public String getApplicationVersion() {
                return "1.2.3";
            }
        };

        List<HofundConnectionsProvider> connectionsProviders = new ArrayList<>();
        connectionsProviders.add(() -> {
            List<HofundConnection> connections = new ArrayList<>();
            connections.add(new HofundConnection("abc", "https//google.com", Type.HTTP, new AtomicReference<>(() -> Connection.http(Status.UP, "1.0.0")), ""));
            connections.add(new HofundConnection("abc", "https//google.com", Type.HTTP, new AtomicReference<>(() -> Connection.http(Status.UP, "1.0.0")), ""));
            return connections;
        });

        // when-then:
        Assertions.assertThrows(IllegalArgumentException.class, () -> new HofundEdgeMeter(
                infoProvider, connectionsProviders
        ));

    }

}