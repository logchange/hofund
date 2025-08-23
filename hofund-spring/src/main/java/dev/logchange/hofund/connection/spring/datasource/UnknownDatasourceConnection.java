package dev.logchange.hofund.connection.spring.datasource;

import javax.sql.DataSource;

/**
 * Fallback DatasourceConnection used when database metadata cannot be determined
 * (e.g., connection acquisition fails). It allows Hofund to still expose a row
 * in the connections table and report DOWN status via the standard test mechanism.
 */
public class UnknownDatasourceConnection extends DatasourceConnection {

    private static final String TEST_QUERY = "SELECT 1";

    public UnknownDatasourceConnection(DataSource dataSource) {
        super(dataSource, TEST_QUERY);
    }

    @Override
    protected String getTarget() {
        return "unknown";
    }

    @Override
    protected String getUrl() {
        return "unknown";
    }

    @Override
    protected String getDbVendor() {
        return "UNKNOWN";
    }
}
