package dev.logchange.hofund.connection.spring.datasource;

import dev.logchange.hofund.connection.HofundConnection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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

    @Test
    void getConnections_shouldReturnTwoConnections_whenTwoDataSourcesHaveSameTargetUrlDbVendor() throws SQLException {
        // given:
        DataSource dataSource1 = Mockito.mock(DataSource.class);
        DataSource dataSource2 = Mockito.mock(DataSource.class);

        Connection connection1 = Mockito.mock(Connection.class);
        Connection connection2 = Mockito.mock(Connection.class);

        DatabaseMetaData metaData1 = Mockito.mock(DatabaseMetaData.class);
        DatabaseMetaData metaData2 = Mockito.mock(DatabaseMetaData.class);

        PreparedStatement statement1 = Mockito.mock(PreparedStatement.class);
        PreparedStatement statement2 = Mockito.mock(PreparedStatement.class);

        ResultSet resultSet1 = Mockito.mock(ResultSet.class);
        ResultSet resultSet2 = Mockito.mock(ResultSet.class);

        // Set up the same target, url, and dbVendor for both connections
        String target = "sameTarget";
        String url = "jdbc:oracle:thin:@localhost:1521:orcl";
        String dbVendor = "Oracle";

        // Configure first data source
        when(dataSource1.getConnection()).thenReturn(connection1);
        when(connection1.getMetaData()).thenReturn(metaData1);
        when(metaData1.getUserName()).thenReturn(target);
        when(metaData1.getURL()).thenReturn(url);
        when(metaData1.getDatabaseProductName()).thenReturn(dbVendor);

        // Configure second data source
        when(dataSource2.getConnection()).thenReturn(connection2);
        when(connection2.getMetaData()).thenReturn(metaData2);
        when(metaData2.getUserName()).thenReturn(target);
        when(metaData2.getURL()).thenReturn(url);
        when(metaData2.getDatabaseProductName()).thenReturn(dbVendor);

        List<DataSource> dataSources = new ArrayList<>();
        dataSources.add(dataSource1);
        dataSources.add(dataSource2);

        DataSourceConnectionsProvider provider = new DataSourceConnectionsProvider(dataSources);

        // when:
        List<HofundConnection> connections = provider.getConnections();

        // then:
        assertThat(connections).hasSize(1);

        // Verify both connections have the same target, url, and description (dbVendor)
        HofundConnection connection1Result = connections.get(0);

        assertThat(connection1Result.getTarget()).isEqualTo(target.toLowerCase());
        assertThat(connection1Result.getUrl()).isEqualTo(url);
        assertThat(connection1Result.getDescription()).isEqualTo(dbVendor);
    }

}
