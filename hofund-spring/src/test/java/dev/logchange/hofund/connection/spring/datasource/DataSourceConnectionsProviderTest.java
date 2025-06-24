package dev.logchange.hofund.connection.spring.datasource;

import dev.logchange.hofund.connection.HofundConnection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DataSourceConnectionsProviderTest {

    @Mock
    private DataSource dataSource;

    @Test
    void getConnections_shouldReturnEmptyList_whenDataSourcesIsNull() {
        // given:
        DataSourceConnectionsProvider provider = new DataSourceConnectionsProvider(null);

        // when:
        List<HofundConnection> connections = provider.getConnections();

        // then:
        assertThat(connections).isEmpty();
    }

    @Test
    void getConnections_shouldReturnEmptyList_whenDataSourcesIsEmpty() {
        // given:
        DataSourceConnectionsProvider provider = new DataSourceConnectionsProvider(Collections.emptyList());

        // when:
        List<HofundConnection> connections = provider.getConnections();

        // then:
        assertThat(connections).isEmpty();
    }

    @Test
    void getConnections_shouldFilterOutNullConnections() {
        // given:
        // This test relies on the fact that DataSourceConnectionFactory.of() will return null
        // for our mock DataSource because it can't get a connection from it
        List<DataSource> dataSources = new ArrayList<>();
        dataSources.add(dataSource);
        
        DataSourceConnectionsProvider provider = new DataSourceConnectionsProvider(dataSources);

        // when:
        List<HofundConnection> connections = provider.getConnections();

        // then:
        assertThat(connections).isEmpty();
    }

}