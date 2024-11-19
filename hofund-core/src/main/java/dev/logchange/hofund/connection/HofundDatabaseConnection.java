package dev.logchange.hofund.connection;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicReference;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class HofundDatabaseConnection extends HofundConnection {
    private final String dbVendor;

    @Builder(builderMethodName = "hofundDatabaseConnectionBuilder")
    public HofundDatabaseConnection(String target, String url, Type type, AtomicReference<StatusFunction> fun, String dbVendor) {
        super(target, url, type, fun);
        this.dbVendor = dbVendor;
    }
}
