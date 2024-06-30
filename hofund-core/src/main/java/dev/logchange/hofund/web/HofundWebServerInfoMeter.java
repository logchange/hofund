package dev.logchange.hofund.web;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class HofundWebServerInfoMeter implements MeterBinder {

    private static final String NAME = "hofund.webserver.info";
    private static final String DESCRIPTION = "Basic information about web server that is running this application";

    private final HofundWebServerInfo info;
    private final AtomicInteger atomicInteger;

    public HofundWebServerInfoMeter(HofundWebServerInfoProvider provider) {
        this.info = provider.get();
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

        tags.add(Tag.of("name", info.getName()));
        tags.add(Tag.of("version", info.getVersion()));

        return tags;
    }

}
