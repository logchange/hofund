package dev.logchange.hofund.graph.node;

import dev.logchange.hofund.connection.HofundConnection;
import dev.logchange.hofund.connection.HofundConnectionsProvider;
import dev.logchange.hofund.connection.Type;
import dev.logchange.hofund.info.HofundInfoProvider;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * <a href="https://grafana.com/docs/grafana/latest/visualizations/node-graph/#node-parameters">Grafana Node Graph</a>
 */
public class HofundNodeMeter implements MeterBinder {

    private static final String NAME = "hofund_node";
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
    }

    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        Gauge.builder(NAME, atomicInteger, AtomicInteger::doubleValue)
                .description(DESCRIPTION)
                .tags(tagsForInfo())
                .register(meterRegistry);

        connections.forEach(connection -> {
            Gauge.builder(NAME, atomicInteger, AtomicInteger::doubleValue)
                    .description(DESCRIPTION)
                    .tags(tagsForConnection(connection))
                    .register(meterRegistry);
        });

    }

    private List<Tag> tagsForInfo() {
        List<Tag> tags = new LinkedList<>();

        tags.add(Tag.of("id", infoProvider.getApplicationName()));
        tags.add(Tag.of("title", infoProvider.getApplicationName()));
        tags.add(Tag.of("subtitle", infoProvider.getApplicationVersion()));
        tags.add(Tag.of("type", "app"));

        return tags;
    }

    private List<Tag> tagsForConnection(HofundConnection connection) {
        List<Tag> tags = new LinkedList<>();

        if (connection.getType() == Type.DATABASE) {
            tags.add(Tag.of("id", connection.getTarget() + "_" + connection.getType()));
        } else {
            tags.add(Tag.of("id", connection.getTarget()));
        }
        tags.add(Tag.of("title", connection.getTarget() + "_" + connection.getType()));
        tags.add(Tag.of("subtitle", connection.getType().toString()));
        tags.add(Tag.of("type", connection.getType().toString()));

        return tags;
    }
}
