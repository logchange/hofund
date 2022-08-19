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
            log.debug("DataSource product name is: " + productName);
            switch (dbType) {
                case POSTGRESQL:
                    return new PostgreSQLConnection(metaData, dataSource).toHofundConnection();
                case ORACLE:
                    return new OracleConnection(metaData, dataSource).toHofundConnection();
                default:
                    log.warn("Currently there is no support for DataSource: " + productName + " please create issue at: https://github.com/logchange/hofund");
                    return null;
            }
        } catch (SQLException sqlException) {
            return null;
        }
    }
}
