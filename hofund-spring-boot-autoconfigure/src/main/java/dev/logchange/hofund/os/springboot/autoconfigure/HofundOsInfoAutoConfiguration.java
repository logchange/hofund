package dev.logchange.hofund.os.springboot.autoconfigure;

import dev.logchange.hofund.os.HofundOsInfoMeter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.metrics.export.ConditionalOnEnabledMetricsExport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@ConditionalOnEnabledMetricsExport(value="prometheus")
public class HofundOsInfoAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public HofundOsInfoMeter hofundOsInfoMeter() {
        return new HofundOsInfoMeter();
    }


}
