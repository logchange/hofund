package dev.logchange.hofund.web;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Builder(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class HofundWebServerInfo {

    private final String name;

    private final String version;

    public static HofundWebServerInfo create(String name, String version) {
        log.info("Server name: {} version: {}", name, version);

        return HofundWebServerInfo.builder()
                .name(name)
                .version(version)
                .build();
    }



}

