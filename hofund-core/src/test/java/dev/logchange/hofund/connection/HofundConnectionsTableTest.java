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
                "+----------+---------+--------+------+---------+------------------+\n" +
                        "| TYPE     | NAME    | STATUS | URL  | VERSION | REQUIRED_VERSION |\n" +
                        "+----------+---------+--------+------+---------+------------------+\n" +
                        "| HTTP     | target1 | UP     | fake | 1.0.0   | 1.0.1            |\n" +
                        "| HTTP     | target3 | UP     | fake | 1.0.1   | N/A              |\n" +
                        "| DATABASE | target2 | UP     | fake | N/A     | N/A              |\n" +
                        "| DATABASE | target4 | UP     | fake | N/A     | N/A              |\n" +
                        "+----------+---------+--------+------+---------+------------------+\n";

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
                "+----------+---------+--------+------+---------+------------------+\n" +
                        "| TYPE     | NAME    | STATUS | URL  | VERSION | REQUIRED_VERSION |\n" +
                        "+----------+---------+--------+------+---------+------------------+\n" +
                        "| HTTP     | target1 | UP     | fake | 1.0.0   | 1.0.1            |\n" +
                        "| HTTP     | target3 | UP     | fake | 1.0.1   | N/A              |\n" +
                        "| HTTP     | target1 | UP     | fake | 1.0.0   | 1.0.1            |\n" +
                        "| HTTP     | target3 | UP     | fake | 1.0.1   | N/A              |\n" +
                        "| DATABASE | target2 | UP     | fake | N/A     | N/A              |\n" +
                        "| DATABASE | target4 | UP     | fake | N/A     | N/A              |\n" +
                        "| DATABASE | target2 | UP     | fake | N/A     | N/A              |\n" +
                        "| DATABASE | target4 | UP     | fake | N/A     | N/A              |\n" +
                        "+----------+---------+--------+------+---------+------------------+\n";

        // when:
        String result = table.print();

        // then:
        assertEquals(expected, result);
    }

    private static class TastableHofundConnectionsProvider implements HofundConnectionsProvider {

        public List<HofundConnection> getConnections() {
            HofundConnection target1 = getHofundConnection("target1", "1.0.0");
            target1.setRequiredVersion("1.0.1");
            return Arrays.asList(
                    target1,
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
                new AtomicReference<>(() -> HofundConnectionResult.http(Status.UP, version)),
                null
        );
    }
    private static HofundConnection dbHofundConnection(String target) {
        return new HofundConnection(
                target,
                "fake",
                Type.DATABASE,
                new AtomicReference<>(() -> HofundConnectionResult.db(Status.UP)),
                null
        );
    }
}