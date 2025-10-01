package dev.logchange.hofund.connection.spring.datasource;

import dev.logchange.hofund.connection.spring.datasource.h2.H2Connection;
import dev.logchange.hofund.connection.spring.datasource.oracle.OracleConnection;
import dev.logchange.hofund.connection.spring.datasource.postgresql.PostgreSQLConnection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataSourceConnectionFactoryTest {

    @Mock
    private DataSource dataSource;
    
    @Mock
    private Connection connection;
    
    @Mock
    private DatabaseMetaData metaData;

    @Test
    void of_shouldCreatePostgreSQLConnection_whenDatabaseIsPostgreSQL() throws SQLException {
        // given:
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.getMetaData()).thenReturn(metaData);
        when(metaData.getDatabaseProductName()).thenReturn("PostgreSQL");
        when(metaData.getURL()).thenReturn("jdbc:postgresql://localhost:5432/testdb");

        // when:
        DatasourceConnection result = DataSourceConnectionFactory.of(dataSource);

        // then:
        assertThat(result).isInstanceOf(PostgreSQLConnection.class);
    }

    @Test
    void of_shouldCreateOracleConnection_whenDatabaseIsOracle() throws SQLException {
        // given:
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.getMetaData()).thenReturn(metaData);
        when(metaData.getDatabaseProductName()).thenReturn("Oracle");
        when(metaData.getURL()).thenReturn("jdbc:oracle:thin:@localhost:1521:testdb");
        when(metaData.getUserName()).thenReturn("testuser");

        // when:
        DatasourceConnection result = DataSourceConnectionFactory.of(dataSource);

        // then:
        assertThat(result).isInstanceOf(OracleConnection.class);
    }

    @Test
    void of_shouldCreateH2Connection_whenDatabaseIsH2() throws SQLException {
        // given:
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.getMetaData()).thenReturn(metaData);
        when(metaData.getDatabaseProductName()).thenReturn("H2");
        when(metaData.getURL()).thenReturn("jdbc:h2:mem:testdb");

        // when:
        DatasourceConnection result = DataSourceConnectionFactory.of(dataSource);

        // then:
        assertThat(result).isInstanceOf(H2Connection.class);
    }

    @Test
    void of_shouldCreateUnknownDatasourceConnection_whenDatabaseIsNotSupported() throws SQLException {
        // given:
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.getMetaData()).thenReturn(metaData);
        when(metaData.getDatabaseProductName()).thenReturn("MySQL");

        // when:
        DatasourceConnection result = DataSourceConnectionFactory.of(dataSource);

        // then:
        assertThat(result).isInstanceOf(UnknownDatasourceConnection.class);
    }

    @Test
    void of_shouldCreateUnknownDatasourceConnection_whenConnectionIsNull() throws SQLException {
        // given:
        when(dataSource.getConnection()).thenReturn(null);

        // when:
        DatasourceConnection result = DataSourceConnectionFactory.of(dataSource);

        // then:
        assertThat(result).isInstanceOf(UnknownDatasourceConnection.class);
    }

    @Test
    void of_shouldCreateUnknownDatasourceConnection_whenSQLExceptionIsThrown() throws SQLException {
        // given:
        when(dataSource.getConnection()).thenThrow(new SQLException("Database connection failed"));

        // when:
        DatasourceConnection result = DataSourceConnectionFactory.of(dataSource);

        // then:
        assertThat(result).isInstanceOf(UnknownDatasourceConnection.class);
    }

    @Test
    void of_shouldCreateUnknownDatasourceConnection_whenGetMetaDataThrowsException() throws SQLException {
        // given:
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.getMetaData()).thenThrow(new SQLException("Metadata access failed"));

        // when:
        DatasourceConnection result = DataSourceConnectionFactory.of(dataSource);

        // then:
        assertThat(result).isInstanceOf(UnknownDatasourceConnection.class);
    }

    @Test
    void of_shouldCreateUnknownDatasourceConnection_whenGetDatabaseProductNameThrowsException() throws SQLException {
        // given:
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.getMetaData()).thenReturn(metaData);
        when(metaData.getDatabaseProductName()).thenThrow(new SQLException("Product name access failed"));

        // when:
        DatasourceConnection result = DataSourceConnectionFactory.of(dataSource);

        // then:
        assertThat(result).isInstanceOf(UnknownDatasourceConnection.class);
    }

    @Test
    void of_shouldHandleCaseInsensitiveDatabaseNames() throws SQLException {
        // given:
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.getMetaData()).thenReturn(metaData);
        when(metaData.getDatabaseProductName()).thenReturn("postgresql");

        // when:
        DatasourceConnection result = DataSourceConnectionFactory.of(dataSource);

        // then:
        assertThat(result).isInstanceOf(UnknownDatasourceConnection.class);
    }
}