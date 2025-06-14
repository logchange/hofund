package dev.logchange.hofund.os;

import static dev.logchange.hofund.StringUtils.emptyIfNull;

public class HofundOsInfo {

    private final String name;
    private final String version;
    private final String arch;
    private final String manufacturer;

    private HofundOsInfo(String name, String version, String arch, String manufacturer) {
        this.name = name;
        this.version = version;
        this.arch = arch;
        this.manufacturer = manufacturer;
    }

    public static HofundOsInfo get() {
        String osName = System.getProperty("os.name");
        String osVersion = System.getProperty("os.version");
        String osArch = System.getProperty("os.arch");

        return new HofundOsInfo(
                getOsFamily(osName),
                emptyIfNull(osVersion),
                emptyIfNull(osArch),
                getManufacturer(osName));
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

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getArch() {
        return arch;
    }

    public String getManufacturer() {
        return manufacturer;
    }
}
