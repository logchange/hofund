package dev.logchange.hofund.info.springboot.autoconfigure;

import dev.logchange.hofund.info.HofundInfoMeter;
import dev.logchange.hofund.info.HofundInfoProvider;
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
