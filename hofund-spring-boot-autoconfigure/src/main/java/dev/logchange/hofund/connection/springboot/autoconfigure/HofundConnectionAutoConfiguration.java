package dev.logchange.hofund.connection.springboot.autoconfigure;

import dev.logchange.hofund.connection.AbstractHofundBasicHttpConnection;
import dev.logchange.hofund.connection.AbstractHofundBasicQueueConnection;
import dev.logchange.hofund.connection.HofundConnectionMeter;
import dev.logchange.hofund.connection.HofundConnectionsProvider;
import dev.logchange.hofund.connection.HofundConnectionsRefresher;
import dev.logchange.hofund.connection.spring.datasource.DataSourceConnectionsProvider;
import dev.logchange.hofund.connection.spring.http.HofundBasicHttpConnectionProvider;
import dev.logchange.hofund.connection.spring.queue.HofundBasicQueueConnectionProvider;
import dev.logchange.hofund.info.HofundInfoProvider;
import dev.logchange.hofund.info.springboot.autoconfigure.HofundInfoAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.micrometer.metrics.autoconfigure.export.ConditionalOnEnabledMetricsExport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.List;

@Configuration(proxyBeanMethods = false)
@ConditionalOnEnabledMetricsExport(value="prometheus")
@AutoConfigureAfter(HofundInfoAutoConfiguration.class)
public class HofundConnectionAutoConfiguration {

    /**
     * Replaces every connection function with one that probes in the background, so a scrape only reads the
     * last known status instead of waiting for the dependencies. Nothing else has to know about it: the meters
     * read through {@code HofundConnection#getFun()} on every scrape and pick the new function up on their own.
     * <p>
     * Switched off with {@code HOFUND_CONNECTIONS_REFRESH_DISABLED=true}, in which case this bean is still
     * created but wraps nothing and the old on-scrape behaviour stays in place.
     */
    @Bean
    @ConditionalOnMissingBean
    public HofundConnectionsRefresher hofundConnectionsRefresher(List<HofundConnectionsProvider> hofundConnectionsProviders) {
        return new HofundConnectionsRefresher(hofundConnectionsProviders);
    }

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


    @Bean
    @ConditionalOnMissingBean
    public HofundBasicHttpConnectionProvider httpBasicConnectionProvider(List<AbstractHofundBasicHttpConnection> hofundBasicHttpConnections) {
        return new HofundBasicHttpConnectionProvider(hofundBasicHttpConnections);
    }

    @Bean
    @ConditionalOnMissingBean
    public HofundBasicQueueConnectionProvider queueBasicConnectionProvider(List<AbstractHofundBasicQueueConnection> hofundBasicQueueConnections) {
        return new HofundBasicQueueConnectionProvider(hofundBasicQueueConnections);
    }
}
