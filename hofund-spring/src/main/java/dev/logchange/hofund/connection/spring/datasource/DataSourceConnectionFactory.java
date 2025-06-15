package dev.logchange.hofund.connection.spring.datasource;

import dev.logchange.hofund.connection.HofundConnection;
import dev.logchange.hofund.connection.spring.datasource.h2.H2Connection;
import dev.logchange.hofund.connection.spring.datasource.oracle.OracleConnection;
import dev.logchange.hofund.connection.spring.datasource.postgresql.PostgreSQLConnection;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static org.slf4j.LoggerFactory.getLogger;

public class DataSourceConnectionFactory {

    private static final Logger log = getLogger(DataSourceConnectionFactory.class);

    public static HofundConnection of(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            String productName = metaData.getDatabaseProductName();
            DatabaseProductName dbType = DatabaseProductName.of(productName);
            log.debug("DataSource product name is: {}", productName);
            return switch (dbType) {
                case POSTGRESQL -> new PostgreSQLConnection(metaData, dataSource).toHofundConnection();
                case ORACLE -> new OracleConnection(metaData, dataSource).toHofundConnection();
                case H2 -> new H2Connection(metaData, dataSource).toHofundConnection();
                default -> {
                    log.warn("Currently there is no support for DataSource: {} please create issue at: https://github.com/logchange/hofund", productName);
                    yield null;
                }
            };
        } catch (SQLException e) {
            log.warn("Error creating datasource HofundConnection", e);
            return null;
        }
    }
}
