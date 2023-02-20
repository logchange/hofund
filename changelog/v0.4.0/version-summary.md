<!-- @formatter:off -->
<!-- noinspection -->
<!-- Prevents auto format, for JetBrains IDE File > Settings > Editor > Code Style (Formatter Tab) > Turn formatter on/off with markers in code comments  -->

[0.4.0] - 2023-02-20
--------------------

### Added (3 changes)

- Added base Grafana dashboard to `grafana-dashboard` directory. ([Peter Zmilczak](https://github.com/marwin1991) @marwin1991)
- Added git based information to metrics in `hofund_git_info` !12 ([Peter Zmilczak](https://github.com/marwin1991) @marwin1991)
- Added yaml based examples in readme !13 #9 ([Peter Zmilczak](https://github.com/marwin1991) @marwin1991)

### Fixed (1 change)

- Spring Boot 3 compatibility !14 [source](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0.0-M5-Release-Notes#auto-configuration-registration) ([Peter Zmilczak](https://github.com/marwin1991) @marwin1991) (@jfoder [LINK](https://github.com/jfoder))

### Configuration changes

| Type: application.properties                                                                                                                                                                                       |
| ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| <ul><li>Added `hofund.git-info.branch` with default value: ``</li><li>Description: Short git commit id, set it to `@git.branch@` and configure `git-commit-id-plugin` see README.md.</li></ul>                     |
| <ul><li>Added `hofund.git-info.build.host` with default value: ``</li><li>Description: Short git commit id, set it to `@git.build.host@` and configure `git-commit-id-plugin` see README.md.</li></ul>             |
| <ul><li>Added `hofund.git-info.build.time` with default value: ``</li><li>Description: Short git commit id, set it to `@git.build.time@` and configure `git-commit-id-plugin` see README.md.</li></ul>             |
| <ul><li>Added `hofund.git-info.commit.id` with default value: ``</li><li>Description: Git commit id, set it to `@git.commit.id@` and configure `git-commit-id-plugin`, see README.md.</li></ul>                    |
| <ul><li>Added `hofund.git-info.commit.id-abbrev` with default value: ``</li><li>Description: Short git commit id, set it to `@git.commit.id.abbrev@` and configure `git-commit-id-plugin` see README.md.</li></ul> |
| <ul><li>Added `hofund.git-info.dirty` with default value: ``</li><li>Description: Short git commit id, set it to `@git.dirty@` and configure `git-commit-id-plugin` see README.md.</li></ul>                       |


