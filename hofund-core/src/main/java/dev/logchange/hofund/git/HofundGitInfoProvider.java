package dev.logchange.hofund.git;

public interface HofundGitInfoProvider {

    String getCommitId();

    String dirty();

    String getBranch();

    String getBuildHost();

    String getBuildTime();

}
