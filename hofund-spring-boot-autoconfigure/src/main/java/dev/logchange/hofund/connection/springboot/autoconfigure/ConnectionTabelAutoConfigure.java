package dev.logchange.hofund.connection.springboot.autoconfigure;

import dev.logchange.hofund.connection.HofundConnectionsTable;
import dev.logchange.hofund.connection.HofundConnectionsProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class ConnectionTabelAutoConfigure {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "hofund.connections-tabel", havingValue = "true", matchIfMissing = true)
    public HofundConnectionsTable connectionsTable(List<HofundConnectionsProvider> hofundConnectionsProviders) {
        return new HofundConnectionsTable(hofundConnectionsProviders);
    }
}
