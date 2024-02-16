package dev.logchange.hofund.os;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

@Slf4j
@AutoConfigureObservability
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HofundOsInfoE2ETest {

    private final TestRestTemplate template = new TestRestTemplate();

    @LocalServerPort
    private int port;

    @Test
    void shouldContainHofundOsInfo() {
        //given:
        String path = "http://localhost:" + port + "/actuator/prometheus";

        String name = System.getProperty("os.name");
        String version = System.getProperty("os.version");
        String arch = System.getProperty("os.arch");

        String expected =
                "# HELP hofund_os_info Basic information about operating system that is running this application\n" +
                "# TYPE hofund_os_info gauge\n" +
                "hofund_os_info{arch=\"{arch}\",name=\"{name}\",version=\"{version}\",} 1.0"
                .replace("{arch}", arch)
                .replace("{name}", name)
                .replace("{version}", version);

        //when:
        String response = template.getForObject(path, String.class);

        //then:
        log.info("Expecting: \n{}", expected);
        Assertions.assertTrue(response.contains(expected));
    }
}
