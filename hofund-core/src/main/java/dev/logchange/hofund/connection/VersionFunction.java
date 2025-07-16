package dev.logchange.hofund.connection;

import java.net.ProtocolException;

@FunctionalInterface
public interface VersionFunction {
    String getVersion() throws ProtocolException;
}
