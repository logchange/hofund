package dev.logchange.hofund.connection;

import dev.logchange.hofund.StringUtils;
import lombok.Builder;
import lombok.Data;

import java.util.concurrent.atomic.AtomicReference;

@Data
@Builder
public class HofundConnection {

    /**
     * Name of the resource that application connects to f.e. cart-db, fcm, products-app
     */
    private final String target;
    private final String url;
    private final Type type;
    private final AtomicReference<StatusFunction> fun;
    private final String description;

    public String toTargetTag() {
        return type == Type.DATABASE ? target + "_" + type : target;
    }

    public String getDescription() {
        return StringUtils.emptyIfNull(description);
    }
}
