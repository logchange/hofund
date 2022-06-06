package dev.logchange.hofund.springboot.autoconfigure;

import dev.logchange.hofund.info.HofundInfo;
import dev.logchange.hofund.info.HofundInfoProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(
        prefix = "management.metrics.export.prometheus",
        name = {"enabled"},
        havingValue = "true",
        matchIfMissing = true
)
@EnableConfigurationProperties(HofundInfoProperties.class)
public class HofundInfoAutoConfiguration {

    private final HofundInfoProperties properties;

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
                return Optional.ofNullable(properties.getApplication().getName());
            }

            @Override
            public Optional<String> getAppVersion() {
                return Optional.of(properties.getApplication().getVersion());
            }
        };
    }


}
