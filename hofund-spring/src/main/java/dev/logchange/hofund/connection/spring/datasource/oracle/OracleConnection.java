package dev.logchange.hofund.connection.spring.datasource.oracle;

import dev.logchange.hofund.connection.StatusFunction;
import dev.logchange.hofund.connection.spring.datasource.DatasourceConnection;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class OracleConnection extends DatasourceConnection {

    public OracleConnection(DatabaseMetaData metaData, DataSource dataSource) {
        super(metaData, dataSource);
    }

    @Override
    protected String getTarget() throws SQLException {
        return null;
    }

    @Override
    protected StatusFunction testConnection() {
        return null;
    }
}
