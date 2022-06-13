package dev.logchange.hofund.connection.springboot.autoconfigure;

import dev.logchange.hofund.connection.HofundConnectionMeter;
import dev.logchange.hofund.connection.HofundConnectionsProvider;
import dev.logchange.hofund.connection.spring.datasource.DataSourceConnectionsProvider;
import dev.logchange.hofund.info.HofundInfoProvider;
import dev.logchange.hofund.info.springboot.autoconfigure.HofundInfoAutoConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.List;

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(
        prefix = "management.metrics.export.prometheus",
        name = {"enabled"},
        havingValue = "true",
        matchIfMissing = true
)
@AutoConfigureAfter(HofundInfoAutoConfiguration.class)
public class HofundConnectionAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(HofundInfoProvider.class)
    public HofundConnectionMeter hofundConnectionMeter(HofundInfoProvider infoProvider, List<HofundConnectionsProvider> hofundConnectionsProviders) {
        return new HofundConnectionMeter(infoProvider, hofundConnectionsProviders);
    }

    @Bean
    @ConditionalOnMissingBean
    public DataSourceConnectionsProvider dataSourceConnectionsProvider(List<DataSource> dataSources) {
        return new DataSourceConnectionsProvider(dataSources);
    }
}
