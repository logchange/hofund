package dev.logchange.hofund.connection.spring.datasource.oracle;

import dev.logchange.hofund.connection.spring.datasource.DatasourceConnection;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Locale;

public class OracleConnection extends DatasourceConnection {

    private static final String TEST_QUERY = "SELECT 1 FROM DUAL";

    public OracleConnection(DatabaseMetaData metaData, DataSource dataSource) {
        super(metaData, dataSource, TEST_QUERY);
    }

    /*
     * You should consider a schema to be the user account and collection of all objects therein as a schema for all intents and purposes.
     * see https://stackoverflow.com/questions/880230/difference-between-a-user-and-a-schema-in-oracle
     */
    @Override
    protected String getTarget() throws SQLException {
        return metaData.getUserName().toLowerCase(Locale.ROOT);
    }
}
