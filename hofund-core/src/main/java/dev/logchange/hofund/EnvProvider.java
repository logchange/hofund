package dev.logchange.hofund;

public interface EnvProvider {

    String getEnv(String name);

    class SystemEnvProvider implements EnvProvider {

        @Override
        public String getEnv(String name) {
            return System.getenv(name);
        }
    }
}