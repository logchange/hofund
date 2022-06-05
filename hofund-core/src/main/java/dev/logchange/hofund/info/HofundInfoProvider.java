package dev.logchange.hofund.info;

import java.util.Optional;

public interface HofundInfoProvider {
    Optional<String> getAppName();
    Optional<String> getAppVersion();
}
