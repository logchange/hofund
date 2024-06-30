package dev.logchange.hofund.graph.edge;

import dev.logchange.hofund.connection.HofundConnection;
import dev.logchange.hofund.connection.HofundConnectionMeter;
import dev.logchange.hofund.connection.HofundConnectionsProvider;
import dev.logchange.hofund.info.HofundInfoProvider;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * <a href="https://grafana.com/docs/grafana/latest/visualizations/node-graph/#node-parameters">Grafana Node Graph</a>
 */
public class HofundEdgeMeter implements MeterBinder {

    private static final String NAME = "hofund.edge";
    private static final String DESCRIPTION = "Information about hofund edge, value is always 1.0, to check status use hofund_connection";

    private final HofundInfoProvider infoProvider;
    private final List<HofundConnection> connections;
    private final AtomicInteger atomicInteger;

    public HofundEdgeMeter(HofundInfoProvider infoProvider, List<HofundConnectionsProvider> connectionsProviders) {
        this.infoProvider = infoProvider;
        this.connections = connectionsProviders.stream()
                .map(HofundConnectionsProvider::getConnections)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        this.atomicInteger = new AtomicInteger(1);
    }

    @Override
    public void bindTo(MeterRegistry meterRegistry) {

        connections.forEach(connection -> {
            Gauge.builder(NAME, atomicInteger, AtomicInteger::doubleValue)
                    .description(DESCRIPTION)
                    .tags(HofundConnectionMeter.tags(infoProvider, connection))
                    .register(meterRegistry);
        });

    }
}
