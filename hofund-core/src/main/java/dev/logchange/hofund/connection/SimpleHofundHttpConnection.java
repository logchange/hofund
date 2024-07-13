package dev.logchange.hofund.connection;

public class SimpleHofundHttpConnection extends AbstractHofundBasicHttpConnection {

    private final String target;
    private final String url;
    private final CheckingStatus checkingStatus;
    private final RequestMethod requestMethod;

    public SimpleHofundHttpConnection(String target, String url) {
        this.target = target;
        this.url = url;
        this.checkingStatus = CheckingStatus.ACTIVE;
        this.requestMethod = RequestMethod.GET;
    }

    public SimpleHofundHttpConnection(String target, String url, CheckingStatus checkingStatus) {
        this.target = target;
        this.url = url;
        this.checkingStatus = checkingStatus;
        this.requestMethod = RequestMethod.GET;
    }

    public SimpleHofundHttpConnection(String target, String url, RequestMethod requestMethod) {
        this.target = target;
        this.url = url;
        this.checkingStatus = CheckingStatus.ACTIVE;
        this.requestMethod = requestMethod;
    }

    public SimpleHofundHttpConnection(String target, String url, CheckingStatus checkingStatus, RequestMethod requestMethod) {
        this.target = target;
        this.url = url;
        this.checkingStatus = checkingStatus;
        this.requestMethod = requestMethod;
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
    protected RequestMethod getRequestMethod() {
        return requestMethod;
    }

    @Override
    protected CheckingStatus getCheckingStatus() {
        return checkingStatus;
    }
}
