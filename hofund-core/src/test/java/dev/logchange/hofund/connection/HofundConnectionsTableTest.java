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
                    getHofundConnection("target1", "1.0.0"),
                    dbHofundConnection("target2"),
                    getHofundConnection("target3", "1.0.1"),
                    dbHofundConnection("target4")
            );
        }

    }

    private static HofundConnection getHofundConnection(String target, String version) {
        return new HofundConnection(
                target,
                "fake",
                Type.HTTP,
                new AtomicReference<>(() -> Connection.http(Status.UP, version)),
                null
        );
    }
    private static HofundConnection dbHofundConnection(String target) {
        return new HofundConnection(
                target,
                "fake",
                Type.DATABASE,
                new AtomicReference<>(() -> Connection.db(Status.UP)),
                null
        );
    }
}