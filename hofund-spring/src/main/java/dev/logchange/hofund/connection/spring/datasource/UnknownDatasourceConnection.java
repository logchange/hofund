package dev.logchange.hofund.connection.spring.datasource;

import org.slf4j.Logger;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Fallback DatasourceConnection used when database metadata cannot be determined
 * (e.g., connection acquisition fails). It allows Hofund to still expose a row
 * in the connections table and report DOWN status via the standard test mechanism.
 */
public class UnknownDatasourceConnection extends DatasourceConnection {

    private static final Logger log = getLogger(UnknownDatasourceConnection.class);

    private static final String TEST_QUERY = "SELECT 1";

    private final String target;
    private final String url;

    public UnknownDatasourceConnection(DataSource dataSource) {
        super(dataSource, TEST_QUERY);
        // Try to obtain original URL and username from DataSource implementation (e.g., HikariDataSource)
        String discoveredUrl = firstNonBlank(
                invokeString(dataSource, "getJdbcUrl"),
                invokeString(dataSource, "getUrl"),
                invokeString(dataSource, "getURL")
        );
        String discoveredUser = firstNonBlank(
                invokeString(dataSource, "getUsername"),
                invokeString(dataSource, "getUser"),
                invokeString(dataSource, "getUserName")
        );

        this.url = Optional.ofNullable(discoveredUrl).orElse("unknown");
        this.target = deriveTarget(discoveredUser, this.url);

        if (log.isDebugEnabled()) {
            log.debug("UnknownDatasourceConnection initialized with url: {} target: {}", this.url, this.target);
        }
    }

    @Override
    protected String getTarget() {
        return target;
    }

    @Override
    protected String getUrl() {
        return url;
    }

    @Override
    protected String getDbVendor() {
        return "UNKNOWN";
    }

    private static String firstNonBlank(String... values) {
        if (values == null) return null;
        for (String v : values) {
            if (v != null && !v.isBlank()) return v;
        }
        return null;
    }

    private static String invokeString(Object obj, String methodName) {
        try {
            Method m = obj.getClass().getMethod(methodName);
            Object result = m.invoke(obj);
            return result != null ? result.toString() : null;
        } catch (Exception ignored) {
            return null;
        }
    }

    private static String deriveTarget(String user, String url) {
        if (user != null && !user.isBlank()) {
            return user.toLowerCase(Locale.ROOT);
        }
        if (url != null && !url.isBlank() && !"unknown".equals(url)) {
            // Try extracting last path or segment without query, similar to PostgreSQLConnection
            try {
                String noQuery = url.split("\\?")[0];
                int slash = noQuery.lastIndexOf('/')
                        ;
                if (slash >= 0 && slash < noQuery.length() - 1) {
                    return noQuery.substring(slash + 1).toLowerCase(Locale.ROOT);
                }
                // For URLs without '/', try last ':' like in H2
                int colon = noQuery.lastIndexOf(':');
                if (colon >= 0 && colon < noQuery.length() - 1) {
                    return noQuery.substring(colon + 1).toLowerCase(Locale.ROOT);
                }
            } catch (Exception ignored) {
                // fall through
            }
        }
        return "unknown";
    }
}
