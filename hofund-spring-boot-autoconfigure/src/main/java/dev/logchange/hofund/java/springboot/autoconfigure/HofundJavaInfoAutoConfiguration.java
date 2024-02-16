package dev.logchange.hofund.java.springboot.autoconfigure;

import dev.logchange.hofund.java.HofundJavaInfoMeter;
import dev.logchange.hofund.os.HofundOsInfoMeter;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
//available since spring boot 2.4.0
//@ConditionalOnEnabledMetricsExport(value="prometheus")
@ConditionalOnClass(PrometheusMeterRegistry.class)
public class HofundJavaInfoAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public HofundJavaInfoMeter hofundJavaInfoMeter() {
        return new HofundJavaInfoMeter();
    }


}
