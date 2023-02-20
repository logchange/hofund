package dev.logchange.hofund.connection;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
     * TLDR: Use value from <name></name> tag from pom.xml file from project you connect to but lower cased.
     */
    protected abstract String getTarget();

    /**
     * Url to test http connection
     */
    protected abstract String getUrl();

    protected int getConnectTimeout() {
        return 1000;
    }

    protected int getReadTimeout() {
        return 1000;
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
                log.debug("Testing http connection to: " + getTarget() + " url: " + getUrl());

                HttpURLConnection urlConn = (HttpURLConnection) getURL().openConnection();
                urlConn.setConnectTimeout(getConnectTimeout());
                urlConn.setReadTimeout(getReadTimeout());

                urlConn.connect();
                int responseCode = urlConn.getResponseCode();
                log.debug("Connection to url: " + getUrl() + " status code: " + responseCode);

                if (responseCode >= 100 && responseCode < 400) {
                    return Status.UP;
                } else {
                    log.warn("Error testing connection to: " + getUrl() + " finished with status code: " + responseCode);
                    return Status.DOWN;
                }
            } catch (IOException e) {
                log.warn("Error testing connection to: " + getUrl() + " msg: " + e.getMessage());
                log.debug("Exception: ", e);
                return Status.DOWN;
            }
        };
    }

}
