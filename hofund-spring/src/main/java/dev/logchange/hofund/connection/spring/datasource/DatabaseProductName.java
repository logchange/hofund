package dev.logchange.hofund.connection.spring.datasource;

import java.util.stream.Stream;

public enum DatabaseProductName {
    POSTGRESQL("PostgreSQL"),
    ORACLE("Oracle"),
    H2("H2"),
    NOT_RECOGNIZED("Not recognized");

    private final String name;

    DatabaseProductName(String name) {
        this.name = name;
    }

    public static DatabaseProductName of(String name) {
        return Stream.of(values())
                .filter(v -> v.getName().equals(name))
                .findFirst()
                .orElse(NOT_RECOGNIZED);
    }

    public String getName() {
        return name;
    }
}
