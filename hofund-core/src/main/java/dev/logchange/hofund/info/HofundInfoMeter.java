package dev.logchange.hofund.info;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class HofundInfoMeter implements MeterBinder {

    private static final String NAME = "hofund_info";
    private static final String DESCRIPTION = "Basic information about application";

    private final HofundInfoProvider provider;
    private final AtomicInteger atomicInteger;

    public HofundInfoMeter(HofundInfoProvider provider) {
        this.provider = provider;
        this.atomicInteger = new AtomicInteger(1);
    }

    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        Gauge.builder(NAME, atomicInteger, AtomicInteger::doubleValue)
                .description(DESCRIPTION)
                .tags(tags())
                .baseUnit("status")
                .register(meterRegistry);
    }

    private List<Tag> tags() {
        List<Tag> tags = new LinkedList<>();

        tags.add(Tag.of("id", provider.getApplicationName()));
        tags.add(Tag.of("application_name", provider.getApplicationName()));
        tags.add(Tag.of("application_version", provider.getApplicationVersion()));

        return tags;
    }
}
