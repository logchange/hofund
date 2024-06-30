package dev.logchange.hofund.git;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class HofundGitInfoMeter implements MeterBinder {

    private static final String NAME = "hofund.git.info";
    private static final String DESCRIPTION = "Basic information about application based on git";

    private final HofundGitInfoProvider provider;
    private final AtomicInteger atomicInteger;

    public HofundGitInfoMeter(HofundGitInfoProvider provider) {
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

        tags.add(Tag.of("commit_id", provider.getCommitId()));
        tags.add(Tag.of("dirty", provider.dirty()));
        tags.add(Tag.of("branch", provider.getBranch()));
        tags.add(Tag.of("build_host", provider.getBuildHost()));
        tags.add(Tag.of("build_time", provider.getBuildTime()));

        return tags;
    }

}
