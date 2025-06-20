package dev.logchange.hofund.graph.node;

import dev.logchange.hofund.connection.HofundConnection;
import dev.logchange.hofund.connection.HofundConnectionsProvider;
import dev.logchange.hofund.connection.Status;
import dev.logchange.hofund.connection.Type;
import dev.logchange.hofund.info.HofundInfoProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

class HofundNodeMeterTest {

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
            connections.add(new HofundConnection("abc", "https//google.com", Type.HTTP, new AtomicReference<>(() -> Status.UP), ""));
            return connections;
        });

        // when-then:
        Assertions.assertThrows(IllegalArgumentException.class, () -> new HofundNodeMeter(
                infoProvider, connectionsProviders
        ));

    }

    @Test
    void whenTwoDbWithSameName_shouldThrowException() {
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
            connections.add(new HofundConnection("st", "jdbc:oracle:thin:@localhost:1521:xe", Type.DATABASE, new AtomicReference<>(() -> Status.UP), "Oracle"));
            connections.add(new HofundConnection("st", "jdbc:oracle:thin:@localhost:1521:xe", Type.DATABASE, new AtomicReference<>(() -> Status.UP), "PostgreSQL"));
            return connections;
        });

        // when:
        HofundNodeMeter result = new HofundNodeMeter(infoProvider, connectionsProviders);

        // then:
        Assertions.assertEquals(HofundNodeMeter.class, result.getClass());

    }

}