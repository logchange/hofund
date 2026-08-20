package dev.logchange.hofund.connection;

/**
 * Ready to use {@link Type#QUEUE} connection built from a target name, an address and a {@link QueueProbe}.
 * <p>
 * Extend {@link AbstractHofundBasicQueueConnection} instead when the address or the checking status have to be
 * resolved on every scrape.
 */
public class SimpleHofundQueueConnection extends AbstractHofundBasicQueueConnection {

    private final String target;
    private final String url;
    private final QueueProbe probe;
    private final CheckingStatus checkingStatus;
    private final String description;
    private final String icon;

    public SimpleHofundQueueConnection(String target, String url, QueueProbe probe) {
        this(target, url, probe, CheckingStatus.ACTIVE, "", "");
    }

    public SimpleHofundQueueConnection(String target, String url, QueueProbe probe, String description) {
        this(target, url, probe, CheckingStatus.ACTIVE, description, "");
    }

    public SimpleHofundQueueConnection(String target, String url, QueueProbe probe, CheckingStatus checkingStatus) {
        this(target, url, probe, checkingStatus, "", "");
    }

    public SimpleHofundQueueConnection(String target, String url, QueueProbe probe, CheckingStatus checkingStatus,
                                       String description, String icon) {
        this.target = target;
        this.url = url;
        this.probe = probe;
        this.checkingStatus = checkingStatus;
        this.description = description;
        this.icon = icon;
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
    protected void probe() throws Exception {
        probe.probe();
    }

    @Override
    protected String getDescription() {
        return description;
    }

    @Override
    protected String getIcon() {
        return icon;
    }

    @Override
    protected CheckingStatus getCheckingStatus() {
        return checkingStatus;
    }
}
