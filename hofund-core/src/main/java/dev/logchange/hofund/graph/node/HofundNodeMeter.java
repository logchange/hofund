package dev.logchange.hofund.graph.node;

import dev.logchange.hofund.connection.HofundConnection;
import dev.logchange.hofund.connection.HofundConnectionsProvider;
import dev.logchange.hofund.info.HofundInfoProvider;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * <a href="https://grafana.com/docs/grafana/latest/visualizations/node-graph/#node-parameters">Grafana Node Graph</a>
 */
public class HofundNodeMeter implements MeterBinder {

    private static final String NAME = "hofund.node";
    private static final String DESCRIPTION = "Information about hofund nodes, value is always 1.0, to check status use hofund_info";

    private final HofundInfoProvider infoProvider;
    private final List<HofundConnection> connections;
    private final AtomicInteger atomicInteger;

    public HofundNodeMeter(HofundInfoProvider infoProvider, List<HofundConnectionsProvider> connectionsProviders) {
        this.infoProvider = infoProvider;
        this.connections = connectionsProviders.stream()
                .map(HofundConnectionsProvider::getConnections)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        this.atomicInteger = new AtomicInteger(1);

        checkIdCollision();
    }

    private void checkIdCollision() {
        List<String> ids = new LinkedList<>();

        connections.forEach(connection -> {
            if (ids.contains(connection.toTargetTag())) {
                throw new IllegalArgumentException("Connection target id must be unique! Connection target id is: " + connection.toTargetTag() + " and already defined connection target ids are: " + ids);
            }
            ids.add(connection.toTargetTag());
        });

        if (ids.contains(infoProvider.getApplicationName())) {
            throw new IllegalArgumentException("Application name must be unique against connections! App name is: " + infoProvider.getApplicationName() + " and connection target ids are: " + ids);
        }
    }

    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        Gauge.builder(NAME, atomicInteger, AtomicInteger::doubleValue)
                .description(DESCRIPTION)
                .tags(tagsForInfo())
                .register(meterRegistry);

        connections.forEach(connection -> Gauge.builder(NAME, atomicInteger, AtomicInteger::doubleValue)
                .description(DESCRIPTION)
                .tags(tagsForConnection(connection))
                .register(meterRegistry));

    }

    private List<Tag> tagsForInfo() {
        List<Tag> tags = new LinkedList<>();

        tags.add(Tag.of("id", infoProvider.getApplicationName()));
        tags.add(Tag.of("title", infoProvider.getApplicationName()));
        tags.add(Tag.of("subtitle", infoProvider.getApplicationVersion()));
        tags.add(Tag.of("type", infoProvider.getApplicationType()));
        tags.add(Tag.of("icon", infoProvider.getApplicationIcon()));

        return tags;
    }

    private List<Tag> tagsForConnection(HofundConnection connection) {
        List<Tag> tags = new LinkedList<>();
        tags.add(Tag.of("id", connection.toTargetTag()));
        tags.add(Tag.of("title", connection.getTarget() + "_" + connection.getType()));

        String subtitle = Objects.equals(connection.getDescription(), "") ?
                connection.getType().toString()
                : String.format("%s (%s)", connection.getType(), connection.getDescription());

        tags.add(Tag.of("subtitle", subtitle));
        tags.add(Tag.of("type", connection.getType().toString()));
        tags.add(Tag.of("icon", connection.getIcon()));
        return tags;
    }
}
