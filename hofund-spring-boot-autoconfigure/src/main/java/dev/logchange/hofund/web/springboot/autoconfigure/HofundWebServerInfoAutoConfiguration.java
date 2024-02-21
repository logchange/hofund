package dev.logchange.hofund.web.springboot.autoconfigure;

import dev.logchange.hofund.web.HofundWebServerInfo;
import dev.logchange.hofund.web.HofundWebServerInfoMeter;
import dev.logchange.hofund.web.HofundWebServerInfoProvider;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.util.ServerInfo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
//available since spring boot 2.4.0
//@ConditionalOnEnabledMetricsExport(value="prometheus")
@ConditionalOnClass(PrometheusMeterRegistry.class)
public class HofundWebServerInfoAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public HofundWebServerInfoMeter hofundWebServerInfoMeter(HofundWebServerInfoProvider provider) {
        return new HofundWebServerInfoMeter(provider);
    }


    @Bean
    @ConditionalOnClass(ServerInfo.class)
    public HofundWebServerInfoProvider tomcatHofundWebServerInfoProvider(){
        return () -> HofundWebServerInfo.create("Apache Tomcat", ServerInfo.getServerNumber());
    }

}
