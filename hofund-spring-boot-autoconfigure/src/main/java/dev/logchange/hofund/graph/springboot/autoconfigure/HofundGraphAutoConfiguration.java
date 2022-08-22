package dev.logchange.hofund.graph.springboot.autoconfigure;

import dev.logchange.hofund.connection.HofundConnectionsProvider;
import dev.logchange.hofund.connection.springboot.autoconfigure.HofundConnectionAutoConfiguration;
import dev.logchange.hofund.graph.edge.HofundEdgeMeter;
import dev.logchange.hofund.graph.node.HofundNodeMeter;
import dev.logchange.hofund.info.HofundInfoProvider;
import dev.logchange.hofund.info.springboot.autoconfigure.HofundInfoAutoConfiguration;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
//available since spring boot 2.4.0
//@ConditionalOnEnabledMetricsExport(value="prometheus")
@ConditionalOnClass(PrometheusMeterRegistry.class)
@AutoConfigureAfter({HofundInfoAutoConfiguration.class, HofundConnectionAutoConfiguration.class})
public class HofundGraphAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(HofundInfoProvider.class)
    public HofundNodeMeter hofundNodeMeter(HofundInfoProvider infoProvider, List<HofundConnectionsProvider> hofundConnectionsProviders) {
        return new HofundNodeMeter(infoProvider, hofundConnectionsProviders);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(HofundInfoProvider.class)
    public HofundEdgeMeter hofundEdgeMeter(HofundInfoProvider infoProvider, List<HofundConnectionsProvider> hofundConnectionsProviders) {
        return new HofundEdgeMeter(infoProvider, hofundConnectionsProviders);
    }
}