package dev.logchange.hofund.os.springboot.autoconfigure;

import dev.logchange.hofund.info.HofundInfoMeter;
import dev.logchange.hofund.info.HofundInfoProvider;
import dev.logchange.hofund.info.springboot.autoconfigure.HofundInfoProperties;
import dev.logchange.hofund.os.HofundOsInfoMeter;
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
public class HofundOsInfoAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public HofundOsInfoMeter hofundOsInfoMeter() {
        return new HofundOsInfoMeter();
    }


}
