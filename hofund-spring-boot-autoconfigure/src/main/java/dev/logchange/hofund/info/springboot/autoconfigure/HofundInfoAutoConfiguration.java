package dev.logchange.hofund.info.springboot.autoconfigure;

import dev.logchange.hofund.StringUtils;
import dev.logchange.hofund.info.HofundInfoMeter;
import dev.logchange.hofund.info.HofundInfoProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.micrometer.metrics.autoconfigure.export.ConditionalOnEnabledMetricsExport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnEnabledMetricsExport(value="prometheus")
@ConditionalOnProperty(name = "hofund.enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(HofundInfoProperties.class)
public class HofundInfoAutoConfiguration {

    private final HofundInfoProperties properties;

    public HofundInfoAutoConfiguration(HofundInfoProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public HofundInfoMeter hofundInfoMeter(HofundInfoProvider infoProvider) {
        return new HofundInfoMeter(infoProvider);
    }


    @Bean
    @ConditionalOnMissingBean
    public HofundInfoProvider hofundInfoProvider() {
        return new HofundInfoProvider() {
            @Override
            public String getApplicationName() {
                return properties.getApplication().getName().toLowerCase();
            }

            @Override
            public String getApplicationVersion() {
                return properties.getApplication().getVersion();
            }

            @Override
            public String getApplicationType() {
                if (StringUtils.isEmpty(properties.getApplication().getType())){
                    return "app";
                }

                return properties.getApplication().getType();
            }

            @Override
            public String getApplicationIcon() {
                if (StringUtils.isEmpty(properties.getApplication().getIcon())){
                    return "docker";
                }

                if (properties.getApplication().getIcon().equalsIgnoreCase("empty")){
                    return "";
                }

                return properties.getApplication().getIcon();
            }
        };
    }


}
