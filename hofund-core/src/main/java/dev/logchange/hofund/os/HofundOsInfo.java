package dev.logchange.hofund.os;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HofundOsInfo {

    private final String name;

    private final String version;

    private final String arch;

    public static HofundOsInfo get() {
        String name = System.getProperty("os.name");
        String version = System.getProperty("os.version");
        String arch = System.getProperty("os.arch");
        return HofundOsInfo.builder()
                .name(name)
                .version(version)
                .arch(arch)
                .build();
    }

}

