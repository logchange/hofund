package dev.logchange.hofund.connection.spring.datasource;

import dev.logchange.hofund.connection.HofundConnection;
import dev.logchange.hofund.connection.HofundConnectionsProvider;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DataSourceConnectionsProvider implements HofundConnectionsProvider {

    private final List<DataSource> dataSources;

    @Override
    public List<HofundConnection> getConnections() {
        if (dataSources == null) {
            return Collections.emptyList();
        }

        return dataSources.stream()
                .map(DataSourceConnectionFactory::of)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
