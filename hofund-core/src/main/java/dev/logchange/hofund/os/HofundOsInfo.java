package dev.logchange.hofund.os;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import oshi.SystemInfo;
import oshi.software.os.OperatingSystem;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HofundOsInfo {

    private final String name;
    private final String version;
    private final String arch;
    private final String manufacturer;

    public static HofundOsInfo get() {
        String arch = System.getProperty("os.arch");
        SystemInfo systemInfo = new SystemInfo();
        OperatingSystem os = systemInfo.getOperatingSystem();
        return HofundOsInfo.builder()
                .name(os.getFamily())
                .version(os.getVersionInfo().toString())
                .arch(arch)
                .manufacturer(os.getManufacturer())
                .build();
    }

}

