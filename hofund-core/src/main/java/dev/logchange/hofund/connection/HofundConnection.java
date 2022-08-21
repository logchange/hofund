package dev.logchange.hofund.connection;

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
    private final Type type;
    private final AtomicReference<StatusFunction> fun;

}
