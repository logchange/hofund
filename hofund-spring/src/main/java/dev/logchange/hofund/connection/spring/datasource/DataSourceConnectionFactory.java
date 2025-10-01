package dev.logchange.hofund.connection.spring.datasource;

import dev.logchange.hofund.connection.spring.datasource.h2.H2Connection;
import dev.logchange.hofund.connection.spring.datasource.oracle.OracleConnection;
import dev.logchange.hofund.connection.spring.datasource.postgresql.PostgreSQLConnection;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

public class DataSourceConnectionFactory {

    private static final Logger log = getLogger(DataSourceConnectionFactory.class);

    public static DatasourceConnection of(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            if (connection != null) {
                DatabaseMetaData metaData = connection.getMetaData();
                String productName = metaData.getDatabaseProductName();
                DatabaseProductName dbType = DatabaseProductName.of(productName);
                log.debug("DataSource product name is: {}", productName);
                return switch (dbType) {
                    case POSTGRESQL -> new PostgreSQLConnection(metaData, dataSource);
                    case ORACLE -> new OracleConnection(metaData, dataSource);
                    case H2 -> new H2Connection(metaData, dataSource);
                    default -> {
                        log.warn("Currently there is no support for DataSource: {} please create issue at: https://github.com/logchange/hofund", productName);
                        yield new UnknownDatasourceConnection(dataSource);
                    }
                };
            } else {
                log.warn("Connection to database is null!");
                return new UnknownDatasourceConnection(dataSource);
            }
        } catch (SQLException e) {
            log.warn("Error creating datasource HofundConnection", e);
            return new UnknownDatasourceConnection(dataSource);
        }
    }
}
