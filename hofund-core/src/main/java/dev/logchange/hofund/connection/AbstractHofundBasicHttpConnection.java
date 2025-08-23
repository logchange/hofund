package dev.logchange.hofund.connection;

import dev.logchange.hofund.EnvProvider;
import org.slf4j.Logger;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static dev.logchange.hofund.connection.HofundConnection.getEnvVarName;
import static dev.logchange.hofund.connection.HofundConnectionResult.NOT_APPLICABLE;
import static dev.logchange.hofund.connection.HofundConnectionResult.UNKNOWN;
import static org.slf4j.LoggerFactory.getLogger;

public abstract class AbstractHofundBasicHttpConnection {

    private static final Logger log = getLogger(AbstractHofundBasicHttpConnection.class);

    private final EnvProvider envProvider;

    protected AbstractHofundBasicHttpConnection() {
        this(new EnvProvider.SystemEnvProvider());
    }

    protected AbstractHofundBasicHttpConnection(EnvProvider envProvider) {
        this.envProvider = envProvider;
    }

    /**
     * Name of the resource that application connects to f.e. Products.
     * <p>
     * If the application that we test connection to
     * also uses hofund, it should be valued from application.properties assign to
     * hofund.info.application.name property (check the target directory, because if
     * you are using @project.name@ it will be evaluated to real name)
     * <p>
     * TLDR: Use value from name tag from pom.xml file from a project you connect to but lower cased.
     *
     * @return Name of the target
     */
    protected abstract String getTarget();

    /**
     * @return Url to test http connection
     */
    protected abstract String getUrl();

    /**
     * @return Description of the connection f.e for Database it is a type like Oracle/PostgreSQL
     */
    protected String getDescription() {
        return "";
    }

    /**
     * Available icons:
     * <a href="https://developers.grafana.com/ui/latest/index.html?path=/story/iconography-icon--icons-overview--icons-overview">Grafana BuiltIn Icons</a>
     */
    protected String getIcon() {
        return "";
    }

    protected int getConnectTimeout() {
        return 1000;
    }

    protected int getReadTimeout() {
        return 1000;
    }

    protected RequestMethod getRequestMethod() {
        return RequestMethod.GET;
    }

    protected List<RequestHeader> getRequestHeaders() {
        return Collections.emptyList();
    }

    private void setRequestHeaders(HttpURLConnection urlConn) {
        List<RequestHeader> requestHeaders = getRequestHeaders();

        if (requestHeaders == null || requestHeaders.isEmpty()) {
            return;
        }

        for (RequestHeader header : requestHeaders) {
            urlConn.setRequestProperty(header.getName(), header.getValue());
        }
    }

    /**
     * If your connection can be disabled f.e. By parameter or should be
     * active between 9 am to 5 pm, you can override this method and implement it as you wish.
     *
     * @return checking status - informs if the connection check is active
     */
    protected CheckingStatus getCheckingStatus() {
        return CheckingStatus.ACTIVE;
    }

    protected URL getURL() {
        try {
            return new URL(getUrl());
        } catch (MalformedURLException e) {
            log.warn("Error reading url", e);
            throw new IllegalArgumentException(e);
        }
    }

    public HofundConnection toHofundConnection() {
        try {
            HofundConnection hofundConnection = new HofundConnection(
                    getTarget(),
                    getUrl(),
                    Type.HTTP,
                    new AtomicReference<>(testConnection()),
                    getDescription()
            );
            hofundConnection.setIcon(getIcon());
            return hofundConnection;
        } catch (Exception e) {
            log.warn("Error creating hofund connection, skipping", e);
            return null;
        }
    }

    private ConnectionFunction testConnection() {
        return () -> {
            try {
                log.debug("Testing http connection to: {} url: {}", getTarget(), getUrl());

                if (getCheckingStatus() == CheckingStatus.INACTIVE) {
                    log.debug("Skipping checking connection to: {} due to inactive status checking", getTarget());
                    return HofundConnectionResult.http(Status.INACTIVE, NOT_APPLICABLE);
                }

                if (isCheckingStatusInactiveByEnvs()) {
                    log.debug("Skipping checking connection to: {} due to disabling it in system envs", getTarget());
                    return HofundConnectionResult.http(Status.INACTIVE, NOT_APPLICABLE);
                }

                HttpURLConnection urlConn = (HttpURLConnection) getURL().openConnection();
                urlConn.setConnectTimeout(getConnectTimeout());
                urlConn.setReadTimeout(getReadTimeout());
                urlConn.setRequestMethod(getRequestMethod().getName());

                setRequestHeaders(urlConn);

                urlConn.connect();
                int responseCode = urlConn.getResponseCode();
                log.debug("Connection to url: {} status code: {}", getUrl(), responseCode);

                if (responseCode >= 100 && responseCode < 400) {
                    return HofundConnectionResult.http(Status.UP, urlConn);
                } else {
                    log.warn("Error testing connection to: {} finished with status code: {}", getUrl(), responseCode);
                    return HofundConnectionResult.http(Status.DOWN, UNKNOWN);
                }
            } catch (Exception e) {
                log.warn("Error testing connection to: {} msg: {}", getUrl(), e.getMessage());
                log.debug("Exception: ", e);
                return HofundConnectionResult.http(Status.DOWN, UNKNOWN);
            }
        };
    }

    /**
     * Checks if the connection status should be set to inactive based on environment variables.
     * This method allows disabling connection checks for specific targets using environment variables.
     *
     * <p>The environment variable name is constructed as: {@code HOFUND_CONNECTION_<TARGET>_DISABLED}
     * where {@code <TARGET>} is the uppercase value returned by {@link #getTarget()}.
     *
     * <p>The connection check will be disabled if the environment variable value is either:
     * <ul>
     *   <li>"true" (case-insensitive)</li>
     *   <li>"1"</li>
     * </ul>
     *
     * <p>Example: For a target named "payment-api", the environment variable would be:
     * {@code HOFUND_CONNECTION_PAYMENT_API_DISABLED=true}
     *
     * @return {@code true} if the connection check should be disabled based on environment variables,
     *         {@code false} otherwise
     */
    protected boolean isCheckingStatusInactiveByEnvs() {
        String target = getTarget();
        String envVarName = getEnvVarName(target);

        String envVarValue = envProvider.getEnv(envVarName);

        if ("true".equalsIgnoreCase(envVarValue) || "1".equals(envVarValue)) {
            log.info("Connection check for target '{}' is disabled by environment variable '{}' with value '{}'", target, envVarName, envVarValue);
            return true;
        }

        return false;
    }
}
