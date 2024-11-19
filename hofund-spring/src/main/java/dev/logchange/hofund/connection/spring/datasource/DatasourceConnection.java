package dev.logchange.hofund.connection.spring.datasource;

import dev.logchange.hofund.connection.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@RequiredArgsConstructor
public abstract class DatasourceConnection {

    private static final int QUERY_TIMEOUT = 1;
    protected final DataSource dataSource;
    protected final String testQuery;

    public HofundDatabaseConnection toHofundConnection() {
        return HofundDatabaseConnection.hofundDatabaseConnectionBuilder()
                .target(getTarget())
                .type(Type.DATABASE)
                .dbVendor(getDbVendor())
                .url(getUrl())
                .fun(new AtomicReference<>(testConnection()))
                .build();
    }

    protected abstract String getTarget();

    protected abstract String getUrl();

    protected abstract String getDbVendor();

    private StatusFunction testConnection() {
        return () -> {
            log.debug("Testing db connection to: {} url: {}", getTarget(), getUrl());
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(testQuery);
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

}
