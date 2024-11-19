package dev.logchange.hofund.connection;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HofundConnectionsTableTest {

    @Test
    void testSortByType() {
        // given:
        TastableHofundConnectionsProvider provider = new TastableHofundConnectionsProvider();
        HofundConnectionsTable table = new HofundConnectionsTable(Collections.singletonList(provider));
        String expected =
                "+----------+---------+--------+------+\n" +
                        "| TYPE     | NAME    | STATUS | URL  |\n" +
                        "+----------+---------+--------+------+\n" +
                        "| HTTP     | target1 | UP     | fake |\n" +
                        "| HTTP     | target3 | UP     | fake |\n" +
                        "| DATABASE | target2 | UP     | fake |\n" +
                        "| DATABASE | target4 | UP     | fake |\n" +
                        "+----------+---------+--------+------+\n";

        // when:
        String result = table.print();

        // then:
        assertEquals(expected, result);
    }

    @Test
    void testSortByTypeWithTwoProviders() {
        // given:
        TastableHofundConnectionsProvider provider1 = new TastableHofundConnectionsProvider();
        TastableHofundConnectionsProvider provider2 = new TastableHofundConnectionsProvider();

        HofundConnectionsTable table = new HofundConnectionsTable(Arrays.asList(provider1, provider2));
        String expected =
                "+----------+---------+--------+------+\n" +
                        "| TYPE     | NAME    | STATUS | URL  |\n" +
                        "+----------+---------+--------+------+\n" +
                        "| HTTP     | target1 | UP     | fake |\n" +
                        "| HTTP     | target3 | UP     | fake |\n" +
                        "| HTTP     | target1 | UP     | fake |\n" +
                        "| HTTP     | target3 | UP     | fake |\n" +
                        "| DATABASE | target2 | UP     | fake |\n" +
                        "| DATABASE | target4 | UP     | fake |\n" +
                        "| DATABASE | target2 | UP     | fake |\n" +
                        "| DATABASE | target4 | UP     | fake |\n" +
                        "+----------+---------+--------+------+\n";

        // when:
        String result = table.print();

        // then:
        assertEquals(expected, result);
    }

    private static class TastableHofundConnectionsProvider implements HofundConnectionsProvider {

        public List<HofundConnection> getConnections() {
            return Arrays.asList(
                    getHofundConnection("target1", Type.HTTP),
                    getHofundConnection("target2", Type.DATABASE),
                    getHofundConnection("target3", Type.HTTP),
                    getHofundConnection("target4", Type.DATABASE)
            );
        }

    }

    private static HofundConnection getHofundConnection(String target, Type type) {
        return new HofundConnection(
                target,
                "fake",
                type,
                new AtomicReference<>(() -> Status.UP)
        );
    }
}