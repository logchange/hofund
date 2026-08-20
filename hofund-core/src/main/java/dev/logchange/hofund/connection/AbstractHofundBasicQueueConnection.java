package dev.logchange.hofund.connection;

import dev.logchange.hofund.EnvProvider;
import org.slf4j.Logger;

import java.util.concurrent.atomic.AtomicReference;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Base class for a {@link Type#QUEUE} connection check: a queue, a topic or a whole message broker.
 * <p>
 * Hofund brings no messaging client of its own, so the only thing a subclass has to provide is
 * {@link #probe()} — returning normally reports UP, throwing anything reports DOWN. Everything around it, the
 * status mapping, the {@link CheckingStatus} handling, the {@code HOFUND_CONNECTION_<TARGET>_DISABLED}
 * environment variable and the logging, is handled here, so it stays identical across applications.
 * <p>
 * The description is empty by default. For {@link Type#QUEUE} and {@link Type#DATABASE} the description is
 * part of the {@code target} metric label, so a value set here changes the label a dashboard matches on.
 *
 * @see SimpleHofundQueueConnection
 */
public abstract class AbstractHofundBasicQueueConnection {

    private static final Logger log = getLogger(AbstractHofundBasicQueueConnection.class);

    private final EnvProvider envProvider;

    protected AbstractHofundBasicQueueConnection() {
        this(new EnvProvider.SystemEnvProvider());
    }

    protected AbstractHofundBasicQueueConnection(EnvProvider envProvider) {
        this.envProvider = envProvider;
    }

    /**
     * Name of the queue, topic or broker that the application connects to f.e. amqp_broker.
     *
     * @return Name of the target
     */
    protected abstract String getTarget();

    /**
     * @return Address of the queue, topic or broker, f.e. {@code amqp://broker.example.com:5672}
     */
    protected abstract String getUrl();

    /**
     * Checks that the target is reachable. Returning normally reports UP, any exception reports DOWN.
     *
     * @throws Exception when the target cannot be reached
     */
    protected abstract void probe() throws Exception;

    /**
     * @return Description of the connection f.e. AMQP message broker. Beware: for {@link Type#QUEUE} the
     * description becomes part of the {@code target} metric label.
     */
    protected String getDescription() {
        return "";
    }

    /**
     * Available icons:
     * <a href="https://developers.grafana.com/ui/latest/index.html?path=/story/iconography-icon--icons-overview--icons-overview">Grafana BuiltIn Icons</a>
     *
     * @return Icon of the connection
     */
    protected String getIcon() {
        return "";
    }

    /**
     * If your connection can be disabled f.e. by a feature toggle, override this method. An unmonitored
     * connection reports INACTIVE, which is not the same as DOWN: a misconfigured target that is supposed to
     * be monitored should stay DOWN.
     *
     * @return checking status - informs if the connection check is active
     */
    protected CheckingStatus getCheckingStatus() {
        return CheckingStatus.ACTIVE;
    }

    public HofundConnection toHofundConnection() {
        HofundConnection hofundConnection = new HofundConnection(
                getTarget(),
                getUrl(),
                Type.QUEUE,
                new AtomicReference<>(testConnection()),
                getDescription()
        );
        hofundConnection.setIcon(getIcon());
        return hofundConnection;
    }

    private ConnectionFunction testConnection() {
        return () -> {
            if (getCheckingStatus() == CheckingStatus.INACTIVE) {
                log.debug("Skipping checking connection to: {} due to inactive status checking", getTarget());
                return HofundConnectionResult.queue(Status.INACTIVE);
            }

            if (isCheckingStatusInactiveByEnvs()) {
                log.debug("Skipping checking connection to: {} due to disabling it in system envs", getTarget());
                return HofundConnectionResult.queue(Status.INACTIVE);
            }

            try {
                log.debug("Testing queue connection to: {} url: {}", getTarget(), getUrl());
                probe();
                return HofundConnectionResult.queue(Status.UP);
            } catch (Exception e) {
                log.warn("Error testing connection to: {} url: {} errorType: {} error: {}",
                        getTarget(), getUrl(), e.getClass().getSimpleName(), e.getMessage());
                log.debug("Exception: ", e);
                return HofundConnectionResult.queue(Status.DOWN);
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
     * @return {@code true} if the connection check should be disabled based on environment variables,
     *         {@code false} otherwise
     */
    protected boolean isCheckingStatusInactiveByEnvs() {
        return CheckingStatusEnvs.isInactiveByEnvs(envProvider, getTarget());
    }
}
