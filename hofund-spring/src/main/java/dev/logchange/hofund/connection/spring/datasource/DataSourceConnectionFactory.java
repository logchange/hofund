package dev.logchange.hofund.connection.spring.datasource;

import dev.logchange.hofund.connection.HofundConnection;
import dev.logchange.hofund.connection.spring.datasource.oracle.OracleConnection;
import dev.logchange.hofund.connection.spring.datasource.postgresql.PostgreSQLConnection;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

@Slf4j
public class DataSourceConnectionFactory {

    public static HofundConnection of(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            String productName = metaData.getDatabaseProductName();
            DatabaseProductName dbType = DatabaseProductName.of(productName);
            log.debug("DataSource product name is: {}", productName);
            return switch (dbType) {
                case POSTGRESQL -> new PostgreSQLConnection(metaData, dataSource).toHofundConnection();
                case ORACLE -> new OracleConnection(metaData, dataSource).toHofundConnection();
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
