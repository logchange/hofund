package dev.logchange.hofund.info.springboot.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("hofund.info")
public class HofundInfoProperties {

    private final Application application = new Application();

    public Application getApplication() {
        return application;
    }

    public static class Application {

        /**
         * The name of your application (will be used in queries so simpler names
         * are preferred) name from pom.xml will be use, whe you set this to @project.name@
         */
        private String name = "";

        /**
         * The version of your application, standard approach is the version
         * from pom.xml by using @project.version@
         */
        private String version = "";

        /**
         * The type of your application, f.e. Backend / frontend / microservice / rest-api
         *  anything that you would like to group by
         * <p>
         * Default: app
         */
        private String type = "";

        /**
         * The icon that you would like to see in grafana for this app.
         * Available icons:
         * <a href="https://developers.grafana.com/ui/latest/index.html?path=/story/docs-overview-icon--icons-overview">Grafana BuiltIn Icons</a>
         * <p>
         * Use: empty keyword to disable the default icon (docker)
         */
        private String icon = "";

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
