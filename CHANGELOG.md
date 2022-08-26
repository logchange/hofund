<!-- @formatter:off -->
<!-- noinspection -->
<!-- Prevents auto format, for JetBrains IDE File > Settings > Editor > Code Style (Formatter Tab) > Turn formatter on/off with markers in code comments  -->

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



