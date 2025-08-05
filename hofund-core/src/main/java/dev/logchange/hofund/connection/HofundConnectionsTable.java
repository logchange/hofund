package dev.logchange.hofund.connection;

import dev.logchange.hofund.AsciiTable;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class HofundConnectionsTable {

    private static final Logger log = getLogger(HofundConnectionsTable.class);

    private static final List<String> HEADERS = Arrays.asList("TYPE", "NAME", "STATUS", "URL", "VERSION");

    private final List<HofundConnection> connections;

    public HofundConnectionsTable(List<HofundConnectionsProvider> connectionsProviders) {

        if (connectionsProviders == null || connectionsProviders.isEmpty()) {
            this.connections = Collections.emptyList();
        } else {
            this.connections = connectionsProviders.stream()
                    .flatMap(t -> t.getConnections().stream())
                    .sorted((d1, d2) -> d2.getType().compareTo(d1.getType()))
                    .collect(Collectors.toList());
        }

    }

    public String print() {
        AsciiTable table = new AsciiTable(HEADERS);

        for (HofundConnection connection : connections) {
            try {
                HofundConnectionResult con = connection.getFun().get().getConnection();
                table.addRow(
                        connection.getType().name(),
                        connection.getTarget(),
                        con.getStatus().getName(),
                        connection.getUrl(),
                        con.getVersion()

                );
            } catch (Exception e) {
                log.warn("Error creating hofund connection for: {} url: {} with msg: {}", connection.getTarget(), connection.getUrl(), e.getMessage());
                table.addRow(
                        connection.getType().name(),
                        connection.getTarget(),
                        Status.DOWN.getName(),
                        connection.getUrl(),
                        HofundConnectionResult.UNKNOWN
                );
            }

        }

        return table.printTable();
    }

    @Override
    public String toString() {
        return print();
    }
}