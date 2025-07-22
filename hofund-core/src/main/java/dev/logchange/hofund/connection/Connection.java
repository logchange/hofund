package dev.logchange.hofund.connection;

public class Connection {

    public static final String UNKNOWN = "UNKNOWN";
    public static final String NOT_APPLICABLE = "N/A";

    private final Status status;
    private final String version;


    private Connection(Status status, String version) {
        this.status = status;
        this.version = version;
    }

    public static Connection db(Status status) {
        return new Connection(status, NOT_APPLICABLE);
    }

    public static Connection http(Status status, String version) {
        return new Connection(status, version);
    }

    public Status getStatus() {
        return status;
    }

    public String getVersion() {
        return version;
    }
}
