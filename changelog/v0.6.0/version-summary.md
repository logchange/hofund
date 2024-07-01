<!-- @formatter:off -->
<!-- noinspection -->
<!-- Prevents auto format, for JetBrains IDE File > Settings > Editor > Code Style (Formatter Tab) > Turn formatter on/off with markers in code comments  -->

<!-- This file is automatically generate by logchange tool 🌳 🪓 => 🪵 -->
<!-- Visit https://github.com/logchange/logchange and leave a star 🌟 -->
<!-- !!! ⚠️ DO NOT MODIFY THIS FILE, YOUR CHANGES WILL BE LOST ⚠️ !!! -->


[0.6.0] - 2023-02-27
--------------------

### Added (1 change)

- Added inactive connections, in metric `hofund_connection` as `-1.0` value. You can take advantage of this feature by overriding `getCheckingStatus` method in `AbstractHofundBasicHttpConnection` ([Peter Zmilczak](https://github.com/marwin1991) @marwin1991)


