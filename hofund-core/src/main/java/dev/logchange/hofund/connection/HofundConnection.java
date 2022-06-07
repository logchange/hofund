package dev.logchange.hofund.connection;

import lombok.Data;

@Data
public class HofundConnection {
    private final String target;
    private final Type type;
    private final StatusFunction fun;
}
