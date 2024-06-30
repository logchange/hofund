package dev.logchange.hofund.java.springboot.autoconfigure;

import dev.logchange.hofund.java.HofundJavaInfoMeter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.metrics.export.ConditionalOnEnabledMetricsExport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@ConditionalOnEnabledMetricsExport(value="prometheus")
public class HofundJavaInfoAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public HofundJavaInfoMeter hofundJavaInfoMeter() {
        return new HofundJavaInfoMeter();
    }


}
