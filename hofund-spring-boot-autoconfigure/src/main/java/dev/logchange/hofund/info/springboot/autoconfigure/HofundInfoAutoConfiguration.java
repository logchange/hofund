package dev.logchange.hofund.info.springboot.autoconfigure;

import dev.logchange.hofund.info.HofundInfoMeter;
import dev.logchange.hofund.info.HofundInfoProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.metrics.export.ConditionalOnEnabledMetricsExport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@ConditionalOnEnabledMetricsExport(value="prometheus")
@EnableConfigurationProperties(HofundInfoProperties.class)
public class HofundInfoAutoConfiguration {

    private final HofundInfoProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public HofundInfoMeter hofundInfoMeter(HofundInfoProvider infoProvider) {
        return new HofundInfoMeter(infoProvider);
    }


    @Bean
    @ConditionalOnMissingBean
    public HofundInfoProvider hofundInfoProvider() {
        return new HofundInfoProvider() {
            @Override
            public String getApplicationName() {
                return properties.getApplication().getName().toLowerCase();
            }

            @Override
            public String getApplicationVersion() {
                return properties.getApplication().getVersion();
            }
        };
    }


}
