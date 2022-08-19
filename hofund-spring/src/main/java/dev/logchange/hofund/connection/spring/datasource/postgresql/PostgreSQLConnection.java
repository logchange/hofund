package dev.logchange.hofund.connection.spring.datasource.postgresql;

import dev.logchange.hofund.connection.spring.datasource.DatasourceConnection;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Locale;


public class PostgreSQLConnection extends DatasourceConnection {

    private static final String TEST_QUERY = "SELECT 1;";

    public PostgreSQLConnection(DatabaseMetaData metaData, DataSource dataSource) {
        super(metaData, dataSource, TEST_QUERY);
    }

    @Override
    protected String getTarget() throws SQLException {
        String url = metaData.getURL();
        int slashIndex = url.lastIndexOf('/');
        int to = Math.max(url.length(), url.lastIndexOf("?") + 1);
        return url.substring(slashIndex + 1, to).toLowerCase(Locale.ROOT);
    }

}
