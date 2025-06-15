package dev.logchange.hofund.web;

import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class HofundWebServerInfo {

    private static final Logger log = getLogger(HofundWebServerInfo.class);

    private final String name;

    private final String version;

    private HofundWebServerInfo(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public static HofundWebServerInfo create(String name, String version) {
        log.info("Server name: {} version: {}", name, version);

        return new HofundWebServerInfo(name, version);
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }
}

