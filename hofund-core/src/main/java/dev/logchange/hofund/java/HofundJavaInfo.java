package dev.logchange.hofund.java;

import static dev.logchange.hofund.StringUtils.emptyIfNull;

public class HofundJavaInfo {
    private final String version;

    private final JavaVendorInfo vendor;

    private final JavaRuntimeEnvironmentInfo runtime;

    private final JavaVirtualMachineInfo jvm;

    public HofundJavaInfo(String version, JavaVendorInfo vendor, JavaRuntimeEnvironmentInfo runtime, JavaVirtualMachineInfo jvm) {
        this.version = version;
        this.vendor = vendor;
        this.runtime = runtime;
        this.jvm = jvm;
    }

    public static HofundJavaInfo get() {
        return new HofundJavaInfo(
                System.getProperty("java.version"),
                new JavaVendorInfo(),
                new JavaRuntimeEnvironmentInfo(),
                new JavaVirtualMachineInfo());
    }

    public String getVersion() {
        return version;
    }

    public JavaVendorInfo getVendor() {
        return vendor;
    }

    public JavaRuntimeEnvironmentInfo getRuntime() {
        return runtime;
    }

    public JavaVirtualMachineInfo getJvm() {
        return jvm;
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

