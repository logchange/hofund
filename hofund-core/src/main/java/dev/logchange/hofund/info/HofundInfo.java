package dev.logchange.hofund.info;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.MeterBinder;

import java.util.LinkedList;
import java.util.List;

public class HofundInfo implements MeterBinder {

    private static final String NAME = "hofund_info";
    private static final String DESCRIPTION = "TODO";

    private final HofundInfoProvider provider;

    public HofundInfo(HofundInfoProvider provider) {
        this.provider = provider;
    }

    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        Gauge.builder(NAME, null, value -> 1.0)
                .description(DESCRIPTION)
                .tags(tags())
                .register(meterRegistry);
    }

    private List<Tag> tags() {
        List<Tag> tags = new LinkedList<>();

        if(provider.getAppName().isPresent()){
            tags.add(Tag.of("app_name", provider.getAppName().get()));
        }

        if(provider.getAppVersion().isPresent()){
            tags.add(Tag.of("app_version", provider.getAppVersion().get()));
        }

        return tags;
    }
}
