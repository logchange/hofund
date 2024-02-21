package dev.logchange.hofund.java;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static dev.logchange.hofund.StringUtils.emptyIfNull;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class HofundJavaInfo {
    private final String version;

    private final JavaVendorInfo vendor;

    private final JavaRuntimeEnvironmentInfo runtime;

    private final JavaVirtualMachineInfo jvm;

    public static HofundJavaInfo get() {
        return HofundJavaInfo.builder()
                .version(System.getProperty("java.version"))
                .vendor(new JavaVendorInfo())
                .runtime(new JavaRuntimeEnvironmentInfo())
                .jvm(new JavaVirtualMachineInfo())
                .build();
    }


    public static class JavaVendorInfo {

        private final String name;

        private final String version;

        public JavaVendorInfo() {
            this.name = System.getProperty("java.vendor");
            this.version = System.getProperty("java.vendor.version");
        }

        public String getName() {
            return emptyIfNull(name);
        }

        public String getVersion() {
            return emptyIfNull(version);
        }
    }


    public static class JavaRuntimeEnvironmentInfo {

        private final String name;

        private final String version;

        public JavaRuntimeEnvironmentInfo() {
            this.name = System.getProperty("java.runtime.name");
            this.version = System.getProperty("java.runtime.version");
        }

        public String getName() {
            return emptyIfNull(name);
        }

        public String getVersion() {
            return emptyIfNull(version);
        }
    }

    public static class JavaVirtualMachineInfo {

        private final String name;

        private final String vendor;

        private final String version;

        public JavaVirtualMachineInfo() {
            this.name = System.getProperty("java.vm.name");
            this.vendor = System.getProperty("java.vm.vendor");
            this.version = System.getProperty("java.vm.version");
        }

        public String getName() {
            return emptyIfNull(name);
        }

        public String getVendor() {
            return emptyIfNull(vendor);
        }

        public String getVersion() {
            return emptyIfNull(version);
        }
    }

}

