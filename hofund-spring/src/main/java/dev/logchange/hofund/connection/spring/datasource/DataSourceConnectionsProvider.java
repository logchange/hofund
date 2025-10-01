package dev.logchange.hofund.connection.spring.datasource;

import dev.logchange.hofund.connection.HofundConnection;
import dev.logchange.hofund.connection.HofundConnectionsProvider;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class DataSourceConnectionsProvider implements HofundConnectionsProvider {

    private static final Logger log = getLogger(DataSourceConnectionsProvider.class);

    private final List<HofundConnection> connections;

    public DataSourceConnectionsProvider(List<DataSource> dataSources) {
        this.connections = getConnections(dataSources);
    }

    @Override
    public List<HofundConnection> getConnections() {
        if (connections == null) {
            return Collections.emptyList();
        }

        return connections;
    }

    private List<HofundConnection> getConnections(List<DataSource> dataSources) {
        if (dataSources == null) {
            return Collections.emptyList();
        }

        List<DatasourceConnection> result = new ArrayList<>();

        for (DataSource dataSource : dataSources) {
            Optional<DatasourceConnection> datasourceConnection = DataSourceConnectionFactory.of(dataSource);

            if (datasourceConnection.isPresent()) {
                DatasourceConnection connection = datasourceConnection.get();

                if (!result.contains(connection)) {
                    result.add(connection);
                } else {
                    log.warn("Found duplicate datasource connection to: {} url: {}", connection.getTarget(), connection.getUrl());
                }
            }
        }

        return result.stream()
                .map(DatasourceConnection::toHofundConnection)
                .collect(Collectors.toList());
    }
}
