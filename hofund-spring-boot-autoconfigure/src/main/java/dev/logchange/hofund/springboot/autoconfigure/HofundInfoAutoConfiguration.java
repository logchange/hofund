package dev.logchange.hofund.springboot.autoconfigure;

import dev.logchange.hofund.info.HofundInfo;
import dev.logchange.hofund.info.HofundInfoProvider;
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

    @Bean
    @ConditionalOnMissingBean
    public HofundInfo hofundInfo(HofundInfoProvider hofundInfoProvider) {
        return new HofundInfo(hofundInfoProvider);
    }


    @Bean
    @ConditionalOnMissingBean
    public HofundInfoProvider hofundInfoProvider() {
        HofundInfoProvider provider = new HofundInfoProvider() {
            @Override
            public Optional<String> getAppName() {
                return Optional.of("AAAAAAAAAAAAAAAAAAAA");
            }

            @Override
            public Optional<String> getAppVersion() {
                return Optional.of("VVVVVVVVVVVVVVVVV");
            }
        };
        return provider;
    }


}
