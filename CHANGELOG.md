<!-- @formatter:off -->
<!-- noinspection -->
<!-- Prevents auto format, for JetBrains IDE File > Settings > Editor > Code Style (Formatter Tab) > Turn formatter on/off with markers in code comments  -->

<!-- This file is automatically generate by logchange tool 🌳 🪓 => 🪵 -->
<!-- Visit https://github.com/logchange/logchange and leave a star 🌟 -->
<!-- !!! ⚠️ DO NOT MODIFY THIS FILE, YOUR CHANGES WILL BE LOST ⚠️ !!! -->


[2.1.0] - 2024-07-14
--------------------

### Added (2 changes)

- Added `SimpleHofundHttpConnection` to simplified configuration of HTTP connections and added ability to override `getRequestMethod()` in `AbstractHofundBasicHttpConnection` to allow using `POST` method. !42 ([Peter Zmilczak](https://github.com/marwin1991) @marwin1991)
- Added `HofundConnectionsTable` to allow printing HofundConnection to terminal during booting up. !43 #26 ([Peter Zmilczak](https://github.com/marwin1991) @marwin1991)


[2.0.0] - 2024-06-30
--------------------

### Changed (1 change)

- Adjust project for Spring Boot 3.3 and Java 17. Hofund-Core still is based on Java 8. !33 ([Peter Zmilczak](https://github.com/marwin1991) @marwin1991)


[1.0.2] - 2024-06-26
--------------------

### Fixed (1 change)

- Fixed problem with missing missing url setting when building 'HofundConnection' from 'AbstractHofundBasicHttpConnection' and added missing 'micrometer-registry-prometheus-simpleclient' dependency. !34 ([Mateusz Piekarczyk](https://github.com/Athi) @Athi)


[1.0.1] - 2024-02-22
--------------------

### Fixed (1 change)

- Fixed problem with missing bean of type `HofundDefaultGitInfoProperties`. !29 ([Peter Zmilczak](https://github.com/marwin1991) @marwin1991)


[1.0.0] - 2024-02-21
--------------------

### Added (3 changes)

- Added new metric `hofund_os_info` with information about name, version arch of the os running application. !23 #22 ([Peter Zmilczak](https://github.com/marwin1991) @marwin1991)
- Added new metric `hofund_java_info` with information about version, vendor and jvm which is running application. !23 #22 ([Peter Zmilczak](https://github.com/marwin1991) @marwin1991)
- Added new metric `hofund_web_server_info` with information about type and version of web server. !27 #26 ([Peter Zmilczak](https://github.com/marwin1991) @marwin1991)

### Changed (1 change)

- Simplified configuration of metric `hofund_git_info` by reading values from `git.properties` file instead of defining `hofund.git-info`. You can still use this to define custom values for this metric. !25 #24 ([Peter Zmilczak](https://github.com/marwin1991) @marwin1991)


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



