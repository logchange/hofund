package dev.logchange.hofund.connection.spring.datasource.oracle;

import dev.logchange.hofund.connection.spring.datasource.DatasourceConnection;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Locale;

import static org.slf4j.LoggerFactory.getLogger;

public class OracleConnection extends DatasourceConnection {

    private static final Logger log = getLogger(OracleConnection.class);

    private static final String TEST_QUERY = "SELECT 1 FROM DUAL";

    private String target;
    private String url;
    private String dbVendor;

    public OracleConnection(DatabaseMetaData metaData, DataSource dataSource) {
        super(dataSource, TEST_QUERY);
        try {
            this.target = metaData.getUserName().toLowerCase(Locale.ROOT);
            this.url = metaData.getURL();
            this.dbVendor = metaData.getDatabaseProductName();
        } catch (SQLException e) {
            log.warn("Error getting db information", e);
            this.target = "ERROR";
            this.url = "ERROR";
            this.dbVendor = "ERROR";
        }
    }

    /*
     * You should consider a schema to be the user account and collection of all objects therein as a schema for all intents and purposes.
     * see https://stackoverflow.com/questions/880230/difference-between-a-user-and-a-schema-in-oracle
     */
    @Override
    protected String getTarget() {
        return target;
    }

    @Override
    protected String getUrl() {
        return url;
    }

    @Override
    protected String getDbVendor() {
        return dbVendor;
    }
}
