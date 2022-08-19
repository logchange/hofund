package dev.logchange.hofund.connection.spring.datasource.postgresql;

import dev.logchange.hofund.connection.Status;
import dev.logchange.hofund.connection.StatusFunction;
import dev.logchange.hofund.connection.spring.datasource.DatasourceConnection;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Locale;
import java.util.Objects;


public class PostgreSQLConnection extends DatasourceConnection {

    private static final String SELECT_1 = "SELECT 1;";
    private static final int QUERY_TIMEOUT = 1;

    public PostgreSQLConnection(DatabaseMetaData metaData, DataSource dataSource) {
        super(metaData, dataSource);
    }

    @Override
    protected String getTarget() throws SQLException {
        String url = metaData.getURL();
        int slashIndex = metaData.getURL().lastIndexOf('/');
        int to = Math.max(url.length(), url.lastIndexOf("?") + 1);
        return url.substring(slashIndex + 1, to).toLowerCase(Locale.ROOT);
    }

    @Override
    protected StatusFunction testConnection() {
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
