<!-- @formatter:off -->
<!-- noinspection -->
<!-- Prevents auto format, for JetBrains IDE File > Settings > Editor > Code Style (Formatter Tab) > Turn formatter on/off with markers in code comments  -->

<!-- This file is automatically generate by logchange tool 🌳 🪓 => 🪵 -->
<!-- Visit https://github.com/logchange/logchange and leave a star 🌟 -->
<!-- !!! ⚠️ DO NOT MODIFY THIS FILE, YOUR CHANGES WILL BE LOST ⚠️ !!! -->


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


