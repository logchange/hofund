package dev.logchange.hofund.connection;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public abstract class AbstractHofundBasicHttpConnection {

    /**
     * Name of the resource that application connects to f.e. products.
     * <p>
     * If application that we test connection to
     * also use hofund it should be value from application.properties assign to
     * hofund.info.application.name property (check target directory, because if
     * you are using @project.name@ it will be evaluated to real name)
     * <p>
     * TLDR: Use value from name tag from pom.xml file from project you connect to but lower cased.
     *
     * @return Name of the target
     */
    protected abstract String getTarget();

    /**
     * @return Url to test http connection
     */
    protected abstract String getUrl();

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

    /**
     * If your connection can be disabled f.e. by parameter or should be
     * active between 9am to 5pm you can override this method and implement it as you wish.
     *
     * @return checking status - informs if connection check is active
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
            return HofundConnection.builder()
                    .target(getTarget())
                    .type(Type.HTTP)
                    .url(getUrl())
                    .fun(new AtomicReference<>(testConnection()))
                    .build();
        } catch (Exception e) {
            log.warn("Error creating hofund connection, skipping", e);
            return null;
        }
    }


    private StatusFunction testConnection() {
        return () -> {
            try {
                log.debug("Testing http connection to: {} url: {}", getTarget(), getUrl());

                if (getCheckingStatus() == CheckingStatus.INACTIVE) {
                    log.debug("Skipping checking connection to: {} due to inactive status checking", getTarget());
                    return Status.INACTIVE;
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
                    return Status.UP;
                } else {
                    log.warn("Error testing connection to: {} finished with status code: {}", getUrl(), responseCode);
                    return Status.DOWN;
                }
            } catch (IOException e) {
                log.warn("Error testing connection to: {} msg: {}", getUrl(), e.getMessage());
                log.debug("Exception: ", e);
                return Status.DOWN;
            }
        };
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
}
