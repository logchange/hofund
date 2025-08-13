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

    private static final List<String> HEADERS = Arrays.asList("TYPE", "NAME", "STATUS", "URL", "VERSION", "REQUIRED VERSION");

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
                HofundConnectionResult connectionResult = connection.getFun().get().getConnection();
                String target = connection.getTarget();
                Version currentVersion = connectionResult.getVersion();
                Version requiredVersion = connection.getRequiredVersion();

                table.addRow(
                        connection.getType().name(),
                        target,
                        connectionResult.getStatus().getName(),
                        connection.getUrl(),
                        currentVersion.toString(),
                        requiredVersion.toString()
                );
                checkVersions(target, currentVersion, requiredVersion);
            } catch (Exception e) {
                String target = connection.getTarget();
                String url = connection.getUrl();
                log.warn("Error creating hofund connection for: {} url: {} with msg: {}", target, url, e.getMessage());
                table.addRow(
                        connection.getType().name(),
                        target,
                        Status.DOWN.getName(),
                        url,
                        HofundConnectionResult.UNKNOWN,
                        HofundConnectionResult.UNKNOWN
                );
            }
        }
        return table.printTable();
    }

    private static void checkVersions(String target, Version version, Version requiredVersion) {
        if (version.isUnspecified() ||  requiredVersion.isUnspecified()) {
            log.debug("Either current({}) or required({}) version of {} is unspecified, cannot verify versions.", version, requiredVersion, target);
            return;
        }
        if (version.compareTo(requiredVersion) < 0) {
            log.error("Current version: {} of {} is lower than the required version: {}", version, target, requiredVersion);
        }
    }

    @Override
    public String toString() {
        return print();
    }
}