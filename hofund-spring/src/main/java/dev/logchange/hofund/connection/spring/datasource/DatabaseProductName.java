package dev.logchange.hofund.connection.spring.datasource;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum DatabaseProductName {
    POSTGRESQL("PostgreSQL"),
    ORACLE("Oracle"),
    H2("H2"),
    NOT_RECOGNIZED("Not recognized");

    private final String name;

    public static DatabaseProductName of(String name) {
        return Stream.of(values())
                .filter(v -> v.getName().equals(name))
                .findFirst()
                .orElse(NOT_RECOGNIZED);
    }
}
