package dev.logchange.hofund.os;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import oshi.SystemInfo;
import oshi.software.os.OperatingSystem;

@Slf4j
@AutoConfigureObservability
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HofundOsInfoE2ETest {

    private final TestRestTemplate template = new TestRestTemplate();

    @LocalServerPort
    private int port;

    @Test
    void shouldContainsHofundOsInfo() {
        //given:
        String path = "http://localhost:" + port + "/actuator/prometheus";

        OperatingSystem os = new SystemInfo().getOperatingSystem();

        String name = os.getFamily();
        String manufacturer = os.getManufacturer();
        String version = os.getVersionInfo().toString();
        String arch = System.getProperty("os.arch");

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
}
