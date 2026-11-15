# Keeping connection probes off the boot thread

Status: proposal, nothing implemented. Line numbers refer to the source at the time of writing.

## Symptom

An application starting with an unreachable dependency logs this **on `main`**, before it is ready:

```
[main] WARN d.l.h.c.AbstractHofundBasicHttpConnection - Error testing connection to:
       http://localhost:8080/reporting-api/health/info finished with status code: 404
```

One line per registration, per connection that is `ACTIVE`. With N dependencies the boot thread pays
N x (connect timeout + read timeout) before `ApplicationReadyEvent`. Hofund's defaults are 1000 ms + 1000 ms,
so ten unreachable dependencies are twenty seconds of startup.

This is unrelated to logging the connections table. An application that never prints the table, or prints it
from a virtual thread, still pays it.

## Why it happens

`detected_version` is a **tag** of the `hofund_connection` gauge, and Micrometer resolves tags when the meter
is registered, not when it is scraped. So:

```java
// HofundConnection.java:110
tags.add(Tag.of("detected_version", getFun().get().getConnection().getVersion().toString()));
```

```java
// HofundConnectionMeter.java:26-29
connections.forEach(connection -> Gauge.builder(NAME, connection, con -> con.getFun().get().getConnection().getStatus().getValue())
        .description(DESCRIPTION)
        .tags(connection.getTags(infoProvider))   // <- evaluated now, on the registering thread
        .register(meterRegistry));
```

The gauge's *value* function is lazy and correct. The *tags* argument is eager, and `getTags` probes. Under
Spring Boot the registering thread is the one refreshing the context: `main`.

## What the background-refresh work does and does not cover

`CachedConnectionFunction` and `HofundConnectionsRefresher` move probing off the **scrape** thread. They do
not move it off the boot thread, for two reasons:

1. `CachedConnectionFunction` probes synchronously in its constructor:

   ```java
   // CachedConnectionFunction.java:33
   this.last = new AtomicReference<>(probe(delegate));
   ```

   and `HofundConnectionsRefresher` constructs one per connection inside its own constructor, which is a
   `@Bean` (`HofundConnectionAutoConfiguration:36-40`). The probes move from `getTags()` to the refresher -
   same thread, same total cost, now paid for every connection rather than only at each registry bind.

2. `hofundConnectionsRefresher` and `hofundConnectionMeter` are independent beans with no ordering between
   them, and `bindTo` is driven by Spring's `MeterRegistryPostProcessor`. When the meter binds first,
   `getTags()` probes uncached and the cache buys nothing.

## Change 1 - make the meter read the cache

Order the two beans so the functions are already wrapped when the meter registers. `getTags()` then reads
`CachedConnectionFunction.getConnection()`, which is an `AtomicReference.get()`.

```java
@Bean
@ConditionalOnMissingBean
@ConditionalOnBean(HofundInfoProvider.class)
@DependsOn("hofundConnectionsRefresher")
public HofundConnectionMeter hofundConnectionMeter(HofundInfoProvider infoProvider, List<HofundConnectionsProvider> hofundConnectionsProviders) {
    return new HofundConnectionMeter(infoProvider, hofundConnectionsProviders);
}
```

- Nothing about the metric contract changes: same tags, same values.
- It removes the bind-time probe entirely, including the duplicate paid once per `MeterRegistry`.
- It does nothing on its own when `HOFUND_CONNECTIONS_REFRESH_DISABLED=true`, where the function stays
  unwrapped and `getTags()` probes as before. That is the documented opt-out, so it is consistent.

## Change 2 - stop probing in the cache constructor

Seed the cache instead of filling it, and let the first scheduled run do the work:

```java
public CachedConnectionFunction(ConnectionFunction delegate, ScheduledExecutorService scheduler, long intervalMillis) {
    this.last = new AtomicReference<>(HofundConnectionResult.http(Status.DOWN, UNKNOWN));
    this.refresh = scheduler.scheduleWithFixedDelay(
            () -> last.set(probe(delegate)), 0, intervalMillis, TimeUnit.MILLISECONDS);
}
```

`initialDelay` of `0` starts the first probe immediately, on a refresh thread rather than on `main`. The seed
is what `probe()` already returns when a probe throws, so the shape is not new.

**This one is a design decision, not a cleanup.** The eager first probe is deliberate: the class javadoc says
the value is ready as soon as the function exists, and `CachedConnectionFunctionTest` asserts the dependency
was hit only by the initial probe. Both would have to change.

What it costs:

- For roughly the duration of one probe after startup, `hofund_connection` reports DOWN for every target
  rather than the truth. A scrape landing in that window sees a false DOWN.
- The seed carries version `UNKNOWN`, so a `DATABASE` or `QUEUE` connection briefly reports `UNKNOWN` where
  it would otherwise report `N/A`.

Change 1 alone removes the duplicate probes and needs no such trade. Change 2 is what removes the last of
them from `main`.

## Deliberately not addressed here

`detected_version` stays frozen at whatever was read when the meter registered, because Micrometer tags are
immutable for the life of a meter. A dependency that was down at startup, or upgraded later, keeps its
boot-time value until the application restarts - and with Change 2 that value would be the seed until the
meter is re-registered.

Making it current means either dropping it from the tag set or re-registering the gauge when the version
changes. Both are metric-contract changes and belong in their own discussion. In-tree, only
`HofundConnectionsTable` reads the version at all (`checkVersions`, lines 66-74) and it re-probes on every
`print()`, so it is unaffected either way.
