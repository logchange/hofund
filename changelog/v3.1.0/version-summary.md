<!-- @formatter:off -->
<!-- noinspection -->
<!-- Prevents auto format, for JetBrains IDE File > Settings > Editor > Code Style (Formatter Tab) > Turn formatter on/off with markers in code comments  -->

<!-- This file is automatically generate by logchange tool 🌳 🪓 => 🪵 -->
<!-- Visit https://github.com/logchange/logchange and leave a star 🌟 -->
<!-- !!! ⚠️ DO NOT MODIFY THIS FILE, YOUR CHANGES WILL BE LOST ⚠️ !!! -->


[3.1.0] - 2026-08-21
--------------------

### Added (1 change)

- Introduced `SimpleHofundQueueConnection` and `AbstractHofundBasicQueueConnection` for testing queue, topic and message broker connections. Hofund stays free of any messaging client: the application supplies a `QueueProbe`, hofund maps it to UP/DOWN, handles `CheckingStatus`, the `HOFUND_CONNECTION_<TARGET>_DISABLED` environment variable and the logging. !175 ([Peter Zmilczak](https://github.com/marwin1991) @marwin1991)

### Fixed (1 change)

- A `HofundConnectionsProvider` returning `null`, or a list containing `null`, no longer breaks metric binding. Such an entry used to reach the meters and fail the whole binding with a `NullPointerException`, taking every other connection down with it; it is now skipped and logged. !175 ([Peter Zmilczak](https://github.com/marwin1991) @marwin1991)

### Dependency updates (9 changes)

- Upgraded org.springframework.boot:spring-boot-dependencies from 4.0.1 to 4.0.2 ([logchange-bot](team@logchange.dev) @logchange-bot)
- Upgraded org.springframework.boot:spring-boot-dependencies from 4.0.2 to 4.0.3 ([logchange-bot](team@logchange.dev) @logchange-bot)
- Upgraded org.springframework.boot:spring-boot-maven-plugin from 4.0.1 to 4.0.2 ([logchange-bot](team@logchange.dev) @logchange-bot)
- Upgraded org.springframework.boot:spring-boot-maven-plugin from 4.0.2 to 4.0.3 ([logchange-bot](team@logchange.dev) @logchange-bot)
- Upgraded renovatebot/github-action from v44.2.4 to v44.2.6 ([logchange-bot](team@logchange.dev) @logchange-bot)
- Upgraded renovatebot/github-action from v44.2.6 to v46.0.1 ([logchange-bot](team@logchange.dev) @logchange-bot)
- Upgraded renovatebot/github-action from v46.0.1 to v46.0.2 ([logchange-bot](team@logchange.dev) @logchange-bot)
- Upgraded renovatebot/github-action from v46.0.2 to v46.1.2 ([logchange-bot](team@logchange.dev) @logchange-bot)
- Upgraded renovatebot/github-action from v46.1.2 to v46.1.3 ([logchange-bot](team@logchange.dev) @logchange-bot)


