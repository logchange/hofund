package dev.logchange.hofund.connection.spring.datasource.postgresql;

import dev.logchange.hofund.connection.spring.datasource.DatasourceConnection;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Locale;

@Slf4j
public class PostgreSQLConnection extends DatasourceConnection {

    private static final String TEST_QUERY = "SELECT 1";

    private String target;
    private String url;

    public PostgreSQLConnection(DatabaseMetaData metaData, DataSource dataSource) {
        super(dataSource, TEST_QUERY);
        try {
            this.url = metaData.getURL();
            int slashIndex = url.lastIndexOf('/');
            int to = url.length();
            if (url.lastIndexOf("?") != -1) {
                to = url.lastIndexOf("?");
            }
            this.target = url.substring(slashIndex + 1, to).toLowerCase(Locale.ROOT);
        } catch (SQLException e) {
            log.warn("Error getting db information", e);
            this.target = "ERROR";
            this.url = "ERROR";
        }
    }

    @Override
    protected String getTarget() {
        return target;
    }

    @Override
    protected String getUrl() {
        return url;
    }

}
