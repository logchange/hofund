package dev.logchange.hofund.springboot.autoconfigure;

import dev.logchange.hofund.info.HofundInfo;
import dev.logchange.hofund.info.HofundInfoProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(
        prefix = "management.metrics.export.prometheus",
        name = {"enabled"},
        havingValue = "true",
        matchIfMissing = true
)
public class HofundInfoAutoConfiguration {

    @Value("${hofund.info.application.name:}")
    private String appName;

    @Value("${hofund.info.application.version:}")
    private String appVersion;

    @Bean
    @ConditionalOnMissingBean
    public HofundInfo hofundInfo(HofundInfoProvider hofundInfoProvider) {
        return new HofundInfo(hofundInfoProvider);
    }


    @Bean
    @ConditionalOnMissingBean
    public HofundInfoProvider hofundInfoProvider() {
        return new HofundInfoProvider() {
            @Override
            public Optional<String> getAppName() {
                return Optional.ofNullable(appName);
            }

            @Override
            public Optional<String> getAppVersion() {
                return Optional.of(appVersion);
            }
        };
    }


}
