package dev.logchange.hofund.connection;

import dev.logchange.hofund.info.HofundInfoProvider;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class HofundConnectionMeter implements MeterBinder {

    private static final String NAME = "hofund.connection";
    private static final String DESCRIPTION = "Current status of given connection";

    private final HofundInfoProvider infoProvider;
    private final List<HofundConnection> connections;

    public HofundConnectionMeter(HofundInfoProvider infoProvider, List<HofundConnectionsProvider> connectionsProviders) {
        this.infoProvider = infoProvider;
        this.connections = connectionsProviders.stream()
                .map(HofundConnectionsProvider::getConnections)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }


    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        connections.forEach(connection -> Gauge.builder(NAME, connection, con -> con.getFun().get().getConnection().getStatus().getValue())
                .description(DESCRIPTION)
                .tags(connection.getTags(infoProvider))
                .register(meterRegistry));
    }
}
