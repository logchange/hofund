package dev.logchange.hofund.connection.spring.datasource;

import dev.logchange.hofund.connection.HofundConnection;
import dev.logchange.hofund.connection.StatusFunction;
import dev.logchange.hofund.connection.Type;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
public abstract class DatasourceConnection {

    protected final DatabaseMetaData metaData;
    protected final DataSource dataSource;

    public HofundConnection toHofundConnection() throws SQLException {
        return HofundConnection.builder()
                .target(getTarget())
                .type(Type.DATABASE)
                .fun(new AtomicReference<>(testConnection()))
                .build();
    }

    protected abstract String getTarget() throws SQLException;

    protected abstract StatusFunction testConnection();

}
