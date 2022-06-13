package dev.logchange.hofund.connection.spring.datasource.postgresql;

import dev.logchange.hofund.connection.HofundConnection;
import dev.logchange.hofund.connection.Status;
import dev.logchange.hofund.connection.StatusFunction;
import dev.logchange.hofund.connection.Type;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;


@RequiredArgsConstructor
public class PostgreSQLConnection {

    private static final String SELECT_1 = "SELECT 1;";
    private static final int QUERY_TIMEOUT = 1;

    private final DatabaseMetaData metaData;
    private final DataSource dataSource;

    public HofundConnection toHofundConnection() throws SQLException {
        return HofundConnection.builder()
                .target(getTarget())
                .type(Type.DATABASE)
                .fun(new AtomicReference<>(testConnection()))
                .build();
    }

    private String getTarget() throws SQLException {
        String url = metaData.getURL();
        int slashIndex = metaData.getURL().lastIndexOf('/');
        int to = Math.max(url.length(), url.lastIndexOf("?") + 1);
        return url.substring(slashIndex + 1, to).toLowerCase(Locale.ROOT);
    }

    private StatusFunction testConnection() {
        return () -> {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(SELECT_1);
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
