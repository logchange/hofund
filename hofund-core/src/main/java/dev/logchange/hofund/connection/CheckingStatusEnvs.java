package dev.logchange.hofund.connection;

import dev.logchange.hofund.EnvProvider;
import org.slf4j.Logger;

import static dev.logchange.hofund.connection.HofundConnection.getEnvVarName;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Resolves the {@code HOFUND_CONNECTION_<TARGET>_DISABLED} environment variable, shared by every connection
 * type that supports switching its check off.
 */
final class CheckingStatusEnvs {

    private static final Logger log = getLogger(CheckingStatusEnvs.class);

    private CheckingStatusEnvs() {
    }

    static boolean isInactiveByEnvs(EnvProvider envProvider, String target) {
        String envVarName = getEnvVarName(target);
        String envVarValue = envProvider.getEnv(envVarName);

        if ("true".equalsIgnoreCase(envVarValue) || "1".equals(envVarValue)) {
            log.info("Connection check for target '{}' is disabled by environment variable '{}' with value '{}'", target, envVarName, envVarValue);
            return true;
        }

        return false;
    }
}
