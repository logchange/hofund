package dev.logchange.hofund.info.springboot.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties("hofund.info")
public class HofundInfoProperties {

    private final Application application = new Application();

    @Getter
    @Setter
    public static class Application {

        /**
         * The name of your application (will be used in queries so simpler names
         * are prefered) name from pom.xml will be use, whe you set this to @project.version@
         */
        private String name = "";

        /**
         * The version of yuor application, standard approach is the version
         * from pom.xml by using @project.version@
         */
        private String version = "";

    }
}
