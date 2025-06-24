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

    public static Optional<DatasourceConnection> of(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            if (connection != null) {
                DatabaseMetaData metaData = connection.getMetaData();
                String productName = metaData.getDatabaseProductName();
                DatabaseProductName dbType = DatabaseProductName.of(productName);
                log.debug("DataSource product name is: {}", productName);
                return switch (dbType) {
                    case POSTGRESQL -> Optional.of(new PostgreSQLConnection(metaData, dataSource));
                    case ORACLE -> Optional.of(new OracleConnection(metaData, dataSource));
                    case H2 -> Optional.of(new H2Connection(metaData, dataSource));
                    default -> {
                        log.warn("Currently there is no support for DataSource: {} please create issue at: https://github.com/logchange/hofund", productName);
                        yield Optional.empty();
                    }
                };
            } else {
                log.warn("Connection to database is null!");
                return Optional.empty();
            }
        } catch (SQLException e) {
            log.warn("Error creating datasource HofundConnection", e);
            return Optional.empty();
        }
    }
}
