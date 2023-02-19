package dev.logchange.hofund.git.springboot.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("hofund.git-info")
public class HofundGitInfoProperties {

    private Commit commit = new Commit();

    private String dirty = "";

    private String branch = "";

    private Build build = new Build();


    @Getter
    @Setter
    public static class Commit {

        private String id = "";

        private String idAbbrev = "";

    }

    @Getter
    @Setter
    public static class Build {

        private String host = "";

        private String time = "";

    }
}
