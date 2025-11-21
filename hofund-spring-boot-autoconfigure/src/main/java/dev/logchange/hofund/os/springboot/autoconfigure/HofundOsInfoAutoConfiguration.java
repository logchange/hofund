package dev.logchange.hofund.os.springboot.autoconfigure;

import dev.logchange.hofund.os.HofundOsInfoMeter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.micrometer.metrics.autoconfigure.export.ConditionalOnEnabledMetricsExport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnEnabledMetricsExport(value="prometheus")
@ConditionalOnProperty(name = "hofund.enabled", havingValue = "true", matchIfMissing = true)
public class HofundOsInfoAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public HofundOsInfoMeter hofundOsInfoMeter() {
        return new HofundOsInfoMeter();
    }


}
