package dev.logchange.hofund.info;

public interface HofundInfoProvider {
    String getApplicationName();

    String getApplicationVersion();

    default String getApplicationType() {
        return "app";
    }

    /**
     * Available icons:
     * <a href="https://developers.grafana.com/ui/latest/index.html?path=/story/docs-overview-icon--icons-overview">Grafana BuiltIn Icons</a>
     */
    default String getApplicationIcon() {
        return "docker";
    }
}
