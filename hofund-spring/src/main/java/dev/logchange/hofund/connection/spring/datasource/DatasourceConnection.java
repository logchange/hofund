package dev.logchange.hofund.connection.spring.datasource;

import dev.logchange.hofund.connection.HofundConnection;
import dev.logchange.hofund.connection.Status;
import dev.logchange.hofund.connection.StatusFunction;
import dev.logchange.hofund.connection.Type;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
public abstract class DatasourceConnection {

    private static final int QUERY_TIMEOUT = 1;

    protected final DatabaseMetaData metaData;
    protected final DataSource dataSource;

    protected final String testQuery;

    public HofundConnection toHofundConnection() throws SQLException {
        return HofundConnection.builder()
                .target(getTarget())
                .type(Type.DATABASE)
                .fun(new AtomicReference<>(testConnection()))
                .build();
    }

    protected abstract String getTarget() throws SQLException;

    private StatusFunction testConnection() {
        return () -> {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(testQuery);
                statement.setQueryTimeout(QUERY_TIMEOUT);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next() && Objects.equals(resultSet.getString(1), "1")) {
                    return Status.UP;
                } else {
                    return Status.DOWN;
                }
            } catch (SQLException e) {
                return Status.DOWN;
            }
        };
    }

}
