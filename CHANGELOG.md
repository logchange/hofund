<!-- @formatter:off -->
<!-- noinspection -->
<!-- Prevents auto format, for JetBrains IDE File > Settings > Editor > Code Style (Formatter Tab) > Turn formatter on/off with markers in code comments  -->

[0.6.0] - 2023-02-27
--------------------

### Added (1 change)

- Added inactive connections, in metric `hofund_connection` as `-1.0` value. You can take advantage of this feature by overriding `getCheckingStatus` method in `AbstractHofundBasicHttpConnection` ([Peter Zmilczak](https://github.com/marwin1991) @marwin1991)


[0.5.0] - 2023-02-21
--------------------

### Fixed (1 change)

- Fix release on maven central. ([Peter Zmilczak](https://github.com/marwin1991) @marwin1991)


[0.4.0] - 2023-02-20
--------------------

### Added (4 changes)

- Added base Grafana dashboard to `grafana-dashboard` directory. ([Peter Zmilczak](https://github.com/marwin1991) @marwin1991)
- Added git based information to metrics in `hofund_git_info` !12 ([Peter Zmilczak](https://github.com/marwin1991) @marwin1991)
- Added yaml based examples in readme !13 #9 ([Peter Zmilczak](https://github.com/marwin1991) @marwin1991)
- Added your custom connection by extending `AbstractHofundBasicHttpConnection` see README for more details. ([Peter Zmilczak](https://github.com/marwin1991) @marwin1991)

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


[0.3.0] - 2022-08-26
--------------------

### Added (1 change)

- Added new metrics `hofund_node` and `hofund_edge`. ([Peter Zmilczak](https://github.com/marwin1991) @marwin1991)

### Changed (1 change)

- Removed status word from metrics `hofund_info_status` and `hofund_connection_status`. ([Peter Zmilczak](https://github.com/marwin1991) @marwin1991)


[0.2.0] - 2022-08-22
--------------------

### Added (1 change)

- Auto detection of data sources in spring boot projects and included metrics for oracle connections. !4 #2 ([Peter Zmilczak](https://github.com/marwin1991) @marwin1991)

### Changed (1 change)

- Changed application name and id in `hofund_info_status` to be always lower case. ([Peter Zmilczak](https://github.com/marwin1991) @marwin1991)

### Fixed (1 change)

- Fixed getting database name for postgres datasource when url is with params. ([Peter Zmilczak](https://github.com/marwin1991) @marwin1991)


[0.1.0] - 2022-08-16
--------------------

### Added (2 changes)

- Using hofund allows you to specify version and name of your application in prometheus metric. ([Peter Zmilczak](https://github.com/marwin1991) @marwin1991)
- Auto detection of data sources in spring boot projects and included metrics for postgres connections. ([Peter Zmilczak](https://github.com/marwin1991) @marwin1991)

### Configuration changes

| Type: application.properties                                                                                                                                                                                                        |
| ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| <ul><li>Added `hofund.info.application.name` with default value: ``</li><li>Description: The name of your application, for maven project you can use `@project.name@` and this will set the name from pom.xml</li></ul>             |
| <ul><li>Added `hofund.info.application.version` with default value: ``</li><li>Description: The version of your application, for maven project you can use `@project.version@` and this will set the version from pom.xml</li></ul> |



