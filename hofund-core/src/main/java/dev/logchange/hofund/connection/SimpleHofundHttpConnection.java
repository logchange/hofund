package dev.logchange.hofund.connection;

import static dev.logchange.hofund.connection.HofundConnectionResult.NOT_APPLICABLE;

public class SimpleHofundHttpConnection extends AbstractHofundBasicHttpConnection {

    private final String target;
    private final String url;
    private final CheckingStatus checkingStatus;
    private final RequestMethod requestMethod;
    private final String icon;
    private final String requiredVersion;

    public SimpleHofundHttpConnection(String target, String url) {
        this.target = target;
        this.url = url;
        this.checkingStatus = CheckingStatus.ACTIVE;
        this.requestMethod = RequestMethod.GET;
        this.icon = "";
        this.requiredVersion = NOT_APPLICABLE;
    }

    public SimpleHofundHttpConnection(String target, String url, String icon) {
        this.target = target;
        this.url = url;
        this.checkingStatus = CheckingStatus.ACTIVE;
        this.requestMethod = RequestMethod.GET;
        this.icon = icon;
        this.requiredVersion = NOT_APPLICABLE;
    }

    public SimpleHofundHttpConnection(String target, String url, CheckingStatus checkingStatus) {
        this.target = target;
        this.url = url;
        this.checkingStatus = checkingStatus;
        this.requestMethod = RequestMethod.GET;
        this.icon = "";
        this.requiredVersion = NOT_APPLICABLE;
    }

    public SimpleHofundHttpConnection(String target, String url, CheckingStatus checkingStatus, String icon) {
        this.target = target;
        this.url = url;
        this.checkingStatus = checkingStatus;
        this.requestMethod = RequestMethod.GET;
        this.icon = icon;
        this.requiredVersion = NOT_APPLICABLE;
    }

    public SimpleHofundHttpConnection(String target, String url, RequestMethod requestMethod) {
        this.target = target;
        this.url = url;
        this.checkingStatus = CheckingStatus.ACTIVE;
        this.requestMethod = requestMethod;
        this.icon = "";
        this.requiredVersion = NOT_APPLICABLE;
    }

    public SimpleHofundHttpConnection(String target, String url, RequestMethod requestMethod, String icon) {
        this.target = target;
        this.url = url;
        this.checkingStatus = CheckingStatus.ACTIVE;
        this.requestMethod = requestMethod;
        this.icon = icon;
        this.requiredVersion = NOT_APPLICABLE;
    }

    public SimpleHofundHttpConnection(String target, String url, CheckingStatus checkingStatus, RequestMethod requestMethod) {
        this.target = target;
        this.url = url;
        this.checkingStatus = checkingStatus;
        this.requestMethod = requestMethod;
        this.icon = "";
        this.requiredVersion = NOT_APPLICABLE;
    }

    public SimpleHofundHttpConnection(String target, String url, CheckingStatus checkingStatus, RequestMethod requestMethod, String icon) {
        this.target = target;
        this.url = url;
        this.checkingStatus = checkingStatus;
        this.requestMethod = requestMethod;
        this.icon = icon;
        this.requiredVersion = NOT_APPLICABLE;
    }

    public SimpleHofundHttpConnection(String target, String url, CheckingStatus checkingStatus, RequestMethod requestMethod, String icon, String requiredVersion) {
        this.target = target;
        this.url = url;
        this.checkingStatus = checkingStatus;
        this.requestMethod = requestMethod;
        this.icon = icon;
        this.requiredVersion = requiredVersion;
    }

    public static SimpleHofundHttpConnection of(String target, String url, String requiredVersion) {
        return new SimpleHofundHttpConnection(target, url, CheckingStatus.ACTIVE, RequestMethod.GET, "", requiredVersion);
    }

    public static SimpleHofundHttpConnection of(String target, String url, CheckingStatus checkingStatus, String requiredVersion) {
        return new SimpleHofundHttpConnection(target, url, checkingStatus, RequestMethod.GET, "", requiredVersion);
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

    @Override
    protected String getIcon() {
        return icon;
    }

    @Override
    protected String getRequiredVersion() {
        return requiredVersion;
    }
}
