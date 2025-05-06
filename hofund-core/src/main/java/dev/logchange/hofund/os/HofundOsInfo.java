package dev.logchange.hofund.os;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static dev.logchange.hofund.StringUtils.emptyIfNull;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HofundOsInfo {

    private final String name;
    private final String version;
    private final String arch;
    private final String manufacturer;

    public static HofundOsInfo get() {
        String osName = System.getProperty("os.name");
        String osVersion = System.getProperty("os.version");
        String osArch = System.getProperty("os.arch");

        return HofundOsInfo.builder()
                .name(getOsFamily(osName))
                .version(emptyIfNull(osVersion))
                .arch(emptyIfNull(osArch))
                .manufacturer(getManufacturer(osName))
                .build();
    }

    private static String getOsFamily(String osName) {
        if (osName == null) {
            return "";
        }

        osName = osName.toLowerCase();

        if (osName.contains("windows")) {
            return "Windows";
        } else if (osName.contains("mac") || osName.contains("darwin")) {
            return "macOS";
        } else if (osName.contains("linux")) {
            return "Linux";
        } else if (osName.contains("unix")) {
            return "Unix";
        } else if (osName.contains("sun") || osName.contains("solaris")) {
            return "Solaris";
        } else if (osName.contains("freebsd")) {
            return "FreeBSD";
        } else {
            return emptyIfNull(osName);
        }
    }

    private static String getManufacturer(String osName) {
        if (osName == null) {
            return "";
        }

        osName = osName.toLowerCase();

        if (osName.contains("windows")) {
            return "Microsoft";
        } else if (osName.contains("mac") || osName.contains("darwin")) {
            return "Apple";
        } else if (osName.contains("linux")) {
            return "GNU/Linux";
        } else if (osName.contains("sun") || osName.contains("solaris")) {
            return "Oracle";
        } else if (osName.contains("freebsd")) {
            return "FreeBSD Foundation";
        } else {
            return "Unknown";
        }
    }
}
