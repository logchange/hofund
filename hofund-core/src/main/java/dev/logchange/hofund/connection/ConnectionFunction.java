package dev.logchange.hofund.connection;

@FunctionalInterface
public interface ConnectionFunction {
    HofundConnectionResult getConnection();
}
