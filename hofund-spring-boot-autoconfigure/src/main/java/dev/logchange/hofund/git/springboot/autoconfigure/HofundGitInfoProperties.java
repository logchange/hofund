package dev.logchange.hofund.git.springboot.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("hofund.git-info")
public class HofundGitInfoProperties {

    private Commit commit = new Commit();

    private String dirty = "";

    private String branch = "";

    private Build build = new Build();


    public Commit getCommit() {
        return commit;
    }

    public void setCommit(Commit commit) {
        this.commit = commit;
    }

    public Build getBuild() {
        return build;
    }

    public void setBuild(Build build) {
        this.build = build;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getDirty() {
        return dirty;
    }

    public void setDirty(String dirty) {
        this.dirty = dirty;
    }

    public static class Commit {

        private String id = "";

        private String idAbbrev = "";

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIdAbbrev() {
            return idAbbrev;
        }

        public void setIdAbbrev(String idAbbrev) {
            this.idAbbrev = idAbbrev;
        }
    }

    public static class Build {

        private String host = "";

        private String time = "";

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
