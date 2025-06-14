package dev.logchange.hofund.os;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static dev.logchange.hofund.StringUtils.emptyIfNull;
import static org.slf4j.LoggerFactory.getLogger;

@AutoConfigureObservability
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HofundOsInfoE2ETest {

    private static final Logger log = getLogger(HofundOsInfoE2ETest.class);

    private final TestRestTemplate template = new TestRestTemplate();

    @LocalServerPort
    private int port;

    @Test
    void shouldContainsHofundOsInfo() {
        //given:
        String path = "http://localhost:" + port + "/actuator/prometheus";

        String osName = System.getProperty("os.name");
        String osVersion = System.getProperty("os.version");
        String osArch = System.getProperty("os.arch");

        String name = getOsFamily(osName);
        String manufacturer = getManufacturer(osName);
        String version = emptyIfNull(osVersion);
        String arch = emptyIfNull(osArch);

        String expected =
                "# HELP hofund_os_info Basic information about operating system that is running this application\n" +
                "# TYPE hofund_os_info gauge\n" +
                "hofund_os_info{arch=\"{arch}\",manufacturer=\"{manufacturer}\",name=\"{name}\",version=\"{version}\"} 1"
                .replace("{arch}", arch)
                .replace("{name}", name)
                .replace("{manufacturer}", manufacturer)
                .replace("{version}", version);

        //when:
        String response = template.getForObject(path, String.class);

        //then:
        log.info("Expecting: \n{}\nResponse: \n{}", expected, response);
        Assertions.assertTrue(response.contains(expected));
    }

    private String getOsFamily(String osName) {
        if (osName == null) {
            return "";
        }

        osName = osName.toLowerCase();

        if (osName.contains("windows")) {
            return "Windows";
        } else if (osName.contains("mac") || osName.contains("darwin")) {
            return "macOS";
        } else if (osName.contains("linux")) {
            return "Linux";
        } else if (osName.contains("unix")) {
            return "Unix";
        } else if (osName.contains("sun") || osName.contains("solaris")) {
            return "Solaris";
        } else if (osName.contains("freebsd")) {
            return "FreeBSD";
        } else {
            return emptyIfNull(osName);
        }
    }

    private String getManufacturer(String osName) {
        if (osName == null) {
            return "";
        }

        osName = osName.toLowerCase();

        if (osName.contains("windows")) {
            return "Microsoft";
        } else if (osName.contains("mac") || osName.contains("darwin")) {
            return "Apple";
        } else if (osName.contains("linux")) {
            return "GNU/Linux";
        } else if (osName.contains("sun") || osName.contains("solaris")) {
            return "Oracle";
        } else if (osName.contains("freebsd")) {
            return "FreeBSD Foundation";
        } else {
            return "Unknown";
        }
    }
}
