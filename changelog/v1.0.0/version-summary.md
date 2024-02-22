<!-- @formatter:off -->
<!-- noinspection -->
<!-- Prevents auto format, for JetBrains IDE File > Settings > Editor > Code Style (Formatter Tab) > Turn formatter on/off with markers in code comments  -->

[1.0.0] - 2024-02-21
--------------------

### Added (3 changes)

- Added new metric `hofund_os_info` with information about name, version arch of the os running application. !23 #22 ([Peter Zmilczak](https://github.com/marwin1991) @marwin1991)
- Added new metric `hofund_java_info` with information about version, vendor and jvm which is running application. !23 #22 ([Peter Zmilczak](https://github.com/marwin1991) @marwin1991)
- Added new metric `hofund_web_server_info` with information about type and version of web server. !27 #26 ([Peter Zmilczak](https://github.com/marwin1991) @marwin1991)

### Changed (1 change)

- Simplified configuration of metric `hofund_git_info` by reading values from `git.properties` file instead of defining `hofund.git-info`. You can still use this to define custom values for this metric. !25 #24 ([Peter Zmilczak](https://github.com/marwin1991) @marwin1991)


