package dev.logchange.hofund.connection.spring.datasource;

import dev.logchange.hofund.connection.HofundConnection;
import dev.logchange.hofund.connection.spring.datasource.postgresql.PostgreSQLConnection;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class DataSourceConnectionFactory {

    public static HofundConnection of(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            String dbType = metaData.getDatabaseProductName();
            if (dbType.equals("PostgreSQL")) {
                return new PostgreSQLConnection(metaData, dataSource).toHofundConnection();
            }
            return null;
        } catch (SQLException sqlException) {
            return null;
        }
    }
}
