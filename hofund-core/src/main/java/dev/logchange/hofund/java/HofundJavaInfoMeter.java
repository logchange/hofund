package dev.logchange.hofund.java;

import dev.logchange.hofund.os.HofundOsInfo;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class HofundJavaInfoMeter implements MeterBinder {

    private static final String NAME = "hofund.java.info";
    private static final String DESCRIPTION = "Basic information about java that is running this application";

    private final HofundJavaInfo info;
    private final AtomicInteger atomicInteger;

    public HofundJavaInfoMeter() {
        this.info = HofundJavaInfo.get();
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

        tags.add(Tag.of("version", info.getVersion()));

        tags.add(Tag.of("vendor_name", info.getVendor().getName()));
        tags.add(Tag.of("vendor_version", info.getVendor().getVersion()));

        tags.add(Tag.of("runtime_name", info.getRuntime().getName()));
        tags.add(Tag.of("runtime_version", info.getRuntime().getVersion()));

        tags.add(Tag.of("jvm_name", info.getJvm().getName()));
        tags.add(Tag.of("jvm_vendor", info.getJvm().getVendor()));
        tags.add(Tag.of("jvm_version", info.getJvm().getVersion()));

        Collections.reverse(tags);

        return tags;
    }

}
