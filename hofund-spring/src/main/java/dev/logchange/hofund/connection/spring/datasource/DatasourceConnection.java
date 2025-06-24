package dev.logchange.hofund.connection.spring.datasource;

import dev.logchange.hofund.connection.HofundConnection;
import dev.logchange.hofund.connection.Status;
import dev.logchange.hofund.connection.StatusFunction;
import dev.logchange.hofund.connection.Type;
import dev.logchange.hofund.connection.spring.datasource.oracle.OracleConnection;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static org.slf4j.LoggerFactory.getLogger;

public abstract class DatasourceConnection {

    private static final Logger log = getLogger(DatasourceConnection.class);

    private static final int QUERY_TIMEOUT = 1;
    protected final DataSource dataSource;
    protected final String testQuery;

    public DatasourceConnection(DataSource dataSource, String testQuery) {
        this.dataSource = dataSource;
        this.testQuery = testQuery;
    }

    public HofundConnection toHofundConnection() {
        return new HofundConnection(
                getTarget(),
                getUrl(),
                Type.DATABASE,
                new AtomicReference<>(testConnection()),
                getDbVendor());
    }

    protected abstract String getTarget();

    protected abstract String getUrl();

    protected abstract String getDbVendor();

    private StatusFunction testConnection() {
        return () -> {
            log.debug("Testing db connection to: {} url: {}", getTarget(), getUrl());
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(testQuery)) {
                statement.setQueryTimeout(QUERY_TIMEOUT);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next() && Objects.equals(resultSet.getString(1), "1")) {
                    return Status.UP;
                } else {
                    log.warn("No connection to database url: {}", getUrl());
                    return Status.DOWN;
                }
            } catch (SQLException e) {
                log.warn("Error testing database connection url: {} msg: {}", getUrl(), e.getMessage());
                log.debug("Exception: ", e);
                return Status.DOWN;
            }
        };
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        DatasourceConnection that = (DatasourceConnection) o;
        return Objects.equals(getTarget(), that.getTarget())
                && Objects.equals(getUrl(), that.getUrl())
                && Objects.equals(getDbVendor(), that.getDbVendor());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getTarget());
        result = 31 * result + Objects.hashCode(getUrl());
        result = 31 * result + Objects.hashCode(getDbVendor());
        return result;
    }

}
