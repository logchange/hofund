package dev.logchange.hofund.git.springboot.autoconfigure;

import dev.logchange.hofund.git.HofundGitInfoMeter;
import dev.logchange.hofund.git.HofundGitInfoProvider;
import org.springframework.boot.actuate.autoconfigure.metrics.export.ConditionalOnEnabledMetricsExport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnEnabledMetricsExport(value="prometheus")
@EnableConfigurationProperties(HofundGitInfoProperties.class)
public class HofundGitInfoAutoConfiguration {

    private final HofundGitInfoProperties properties;

    private final HofundDefaultGitInfoProperties defaultProperties;

    public HofundGitInfoAutoConfiguration(HofundGitInfoProperties properties, HofundDefaultGitInfoProperties defaultProperties) {
        this.properties = properties;
        this.defaultProperties = defaultProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public HofundGitInfoMeter hofundGitInfoMeter(HofundGitInfoProvider infoProvider) {
        return new HofundGitInfoMeter(infoProvider);
    }


    @Bean
    @ConditionalOnMissingBean
    public HofundGitInfoProvider hofundGitInfoProvider() {
        return new HofundGitInfoProvider() {
            @Override
            public String getCommitId() {
                return defaultIfEmpty(properties.getCommit().getIdAbbrev(), defaultProperties.getCommitIdAbbrev());
            }

            @Override
            public String dirty() {
                return defaultIfEmpty(properties.getDirty(), defaultProperties.getDirty());
            }

            @Override
            public String getBranch() {
                return defaultIfEmpty(properties.getBranch(), defaultProperties.getBranch());
            }

            @Override
            public String getBuildHost() {
                return defaultIfEmpty(properties.getBuild().getHost(), defaultProperties.getBuildHost());
            }

            @Override
            public String getBuildTime() {
                return defaultIfEmpty(properties.getBuild().getTime(), defaultProperties.getBuildTime());
            }
        };
    }

    public static String defaultIfEmpty(String val, String defaultVal) {
        if (isEmpty(val)) {
            return defaultVal;
        }

        return val;
    }

    public static boolean isEmpty(String val) {
        return val == null || val.trim().isEmpty();
    }

}
