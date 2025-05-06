package dev.logchange.hofund.connection;

import dev.logchange.hofund.AsciiTable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HofundConnectionsTable {

    private static final List<String> HEADERS = Arrays.asList("TYPE", "NAME", "STATUS", "URL");

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
            table.addRow(
                    connection.getType().name(),
                    connection.getTarget(),
                    connection.getFun().get().getStatus().getName(),
                    connection.getUrl()
            );
        }

        return table.printTable();
    }

    @Override
    public String toString() {
        return print();
    }
}