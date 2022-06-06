package dev.logchange.hofund.info;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class HofundInfo implements MeterBinder {

    private static final String NAME = "hofund_info";
    private static final String DESCRIPTION = "TODO";

    private final HofundInfoProvider provider;
    private final AtomicInteger atomicInteger;

    public HofundInfo(HofundInfoProvider provider) {
        this.provider = provider;
        this.atomicInteger = new AtomicInteger(1);
    }

    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        Gauge.builder(NAME, atomicInteger, AtomicInteger::doubleValue)
                .description(DESCRIPTION)
                .tags(tags())
                .register(meterRegistry);
    }

    private List<Tag> tags() {
        List<Tag> tags = new LinkedList<>();

        if(provider.getAppName().isPresent()){
            tags.add(Tag.of("application_name", provider.getAppName().get()));
        }

        if(provider.getAppVersion().isPresent()){
            tags.add(Tag.of("application_version", provider.getAppVersion().get()));
        }

        return tags;
    }
}
