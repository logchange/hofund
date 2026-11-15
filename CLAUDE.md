# CLAUDE.md

Context for AI sessions working on hofund. Findings here were verified against the source at the time of
writing; every claim names the file and line so it can be rechecked rather than trusted.

## Orientation

Hofund publishes what an application depends on as Prometheus metrics: `hofund_connection` (one gauge per
monitored dependency) plus `hofund_info`, `hofund_node` and `hofund_edge` for the node graph.

- `hofund-core` - no Spring, and **compiled to Java 8** (`hofund-core/pom.xml:19-21`) while the reactor is
  on 17 (`pom.xml:60-61`). Nothing above Java 8 may be used in this module.
- `hofund-spring`, `hofund-spring-boot-autoconfigure`, `hofund-spring-boot-starter`, `hofund-spring-boot-e2e`.
- Changelog entries are logchange YAML files added under `changelog/unreleased/`, one per change.
  `CHANGELOG.md` is generated - never hand-edited.

## Probes run on three different threads, for three different reasons

This is the thing most easily got wrong, because "the probe runs on scrape" is only one of the three.

1. **The scrape thread.** The gauge's value function, `HofundConnectionMeter.bindTo` (line 26), evaluated
   per scrape, per registry.
2. **The thread that registers the meter - `main` under Spring Boot.** `detected_version` is a *tag*, and
   `HofundConnection.getTags()` (line 110) fills it with
   `getFun().get().getConnection().getVersion()` - a real probe. `bindTo` passes `getTags(...)` to
   `Gauge.builder(...).tags(...)` (line 28), which is evaluated eagerly at registration, i.e. during context
   refresh. N dependencies x their timeouts land on the boot thread before the application is ready.
3. **Whoever calls `print()`.** `HofundConnectionsTable.print()` (lines 32-34) probes every connection on the
   calling thread. `toString()` delegates to it (line 77), so an interpolated log statement such as
   `log.debug("...{}", connectionsTable)` fires every probe. An application logging the table from a
   `CommandLineRunner` pays for it on `main`; printing from a bare virtual thread avoids that.

## Tags are frozen at registration

Micrometer tags are immutable for the life of a meter, so `detected_version` keeps whatever was read when the
gauge registered. A dependency unreachable at startup, or upgraded afterwards, is not reflected until the
application restarts. The gauge *value* stays fresh on every scrape; the tag never does. Any fix that keeps
`detected_version` as a tag has to re-register the meter to make it current.

## What `detected_version` is actually used for

Checked across the whole repository:

- the tag itself - `HofundConnection.java:110`, the only occurrence in production code;
- `HofundConnectionsTable` - a `VERSION` column, and the **only version logic in the codebase**:
  `checkVersions` (lines 66-74) logs `ERROR` when the detected version is lower than the required one;
- three assertions in `HofundConnectionTest`.

The bundled dashboard `grafana-dashboards/hofund-node-graph.json` queries `hofund_connection` but joins
`on (id)`; the string `version` does not appear in that file at all. Nothing in hofund branches on the value -
a connection's status comes from `getStatus()` alone.

Consumers outside this repository cannot be checked from here, so the tag is still part of the published
metric contract: removing or renaming it breaks somebody's dashboard or alert even though nothing in-tree
reads it.

## In-flight: background connection refresh

`CachedConnectionFunction`, `HofundConnectionsRefresher` and
`changelog/unreleased/background-connection-refresh.yml` move probing off the **scrape** thread, so a slow
dependency can no longer push the endpoint past Prometheus `scrape_timeout`. Two things that work does not
address, worth knowing before assuming boot-time probing is solved by it:

- `CachedConnectionFunction` probes **synchronously in its constructor** (line 33), and
  `HofundConnectionsRefresher` does that for every connection in its own constructor, which is a `@Bean`
  (`HofundConnectionAutoConfiguration:36-40`). N probes therefore still land on the boot thread - relocated
  from `getTags()`, not removed. The eager first probe is deliberate: the javadoc states the value is ready as
  soon as the function exists, and `CachedConnectionFunctionTest` asserts the dependency was hit only by the
  initial probe. Changing it is a design decision, not a cleanup.
- `hofundConnectionsRefresher` and `hofundConnectionMeter` are independent beans with no ordering between
  them, and `bindTo` is driven by Spring's `MeterRegistryPostProcessor`. If the meter binds first, `getTags()`
  probes uncached.

Both are written up, with the two candidate changes and their trade-offs, in
[docs/boot-thread-probing.md](docs/boot-thread-probing.md). Nothing there is implemented.

## Guards already in the code

`HofundConnection`'s constructor rejects a `null` URL and one ending in `/prometheus` (lines 42-47), the
latter so applications cannot scrape each other recursively. `HofundConnectionsTable.print()` wraps each
connection in its own `try`/`catch` (lines 33-61), so one failing probe degrades to a DOWN row instead of
losing the whole table.
