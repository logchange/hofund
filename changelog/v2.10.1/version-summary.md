<!-- @formatter:off -->
<!-- noinspection -->
<!-- Prevents auto format, for JetBrains IDE File > Settings > Editor > Code Style (Formatter Tab) > Turn formatter on/off with markers in code comments  -->

<!-- This file is automatically generate by logchange tool 🌳 🪓 => 🪵 -->
<!-- Visit https://github.com/logchange/logchange and leave a star 🌟 -->
<!-- !!! ⚠️ DO NOT MODIFY THIS FILE, YOUR CHANGES WILL BE LOST ⚠️ !!! -->


[2.10.1] - 2025-08-30
---------------------

### Added (2 changes)

- Add validation to reject URLs ending with `/prometheus` !108 #98 ([Peter Zmilczak](https://github.com/marwin1991) @marwin1991)
- Allow disabling connection checks for specific targets using environment variables. Set environment variable named `HOFUND_CONNECTION_<TARGET>_DISABLED` (where `<TARGET>` is the uppercase value of the target name) is set to either `true` (case-insensitive) or `1`. For example, to disable connection checks for a target named "payment-api", you would set the environment variable `HOFUND_CONNECTION_PAYMENT_API_DISABLED=true`. !109 ([Peter Zmilczak](https://github.com/marwin1991) @marwin1991)

### Fixed (1 change)

- Display "DOWN" status for broken connections in connections table !114 #48 ([Junie by JetBrains](https://github.com/jetbrains-junie) @jetbrains-junie[bot])

### Dependency updates (4 changes)

- Upgraded actions/setup-java from v4 to v5 ([logchange-bot](team@logchange.dev) @logchange-bot)
- Upgraded org.springframework.boot:spring-boot-dependencies from 3.5.4 to 3.5.5 ([logchange-bot](team@logchange.dev) @logchange-bot)
- Upgraded renovatebot/github-action from v43.0.7 to v43.0.8 ([logchange-bot](team@logchange.dev) @logchange-bot)
- Upgraded renovatebot/github-action from v43.0.8 to v43.0.9 ([logchange-bot](team@logchange.dev) @logchange-bot)


