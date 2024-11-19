package dev.logchange.hofund.os;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class HofundOsInfoMeter implements MeterBinder {

    private static final String NAME = "hofund.os.info";
    private static final String DESCRIPTION = "Basic information about operating system that is running this application";

    private final HofundOsInfo info;
    private final AtomicInteger atomicInteger;

    public HofundOsInfoMeter() {
        this.info = HofundOsInfo.get();
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
        tags.add(Tag.of("manufacturer", info.getManufacturer()));
        tags.add(Tag.of("version", info.getVersion()));
        tags.add(Tag.of("arch", info.getArch()));

        return tags;
    }

}
