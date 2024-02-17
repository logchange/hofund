package dev.logchange.hofund.git.springboot.autoconfigure;

import dev.logchange.hofund.git.HofundGitInfoMeter;
import dev.logchange.hofund.git.HofundGitInfoProvider;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
//available since spring boot 2.4.0
//@ConditionalOnEnabledMetricsExport(value="prometheus")
@ConditionalOnClass(PrometheusMeterRegistry.class)
@EnableConfigurationProperties(HofundGitInfoProperties.class)
public class HofundGitInfoAutoConfiguration {

    private final HofundGitInfoProperties properties;

    private final HofundDefaultGitInfoProperties defaultProperties;

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
