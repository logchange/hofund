package dev.logchange.hofund.connection;

@FunctionalInterface
public interface ConnectionFunction {
    Connection getConnection();
}
