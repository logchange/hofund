package dev.logchange.hofund.web;


import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.util.ServerInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

@Slf4j
@AutoConfigureObservability
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HofundWebServerInfoE2ETest {

    private final TestRestTemplate template = new TestRestTemplate();

    @LocalServerPort
    private int port;

    @Test
    void shouldContainsHofundOsInfo() {
        //given:
        String path = "http://localhost:" + port + "/actuator/prometheus";

        String version = ServerInfo.getServerNumber();

        String expected =
                "# HELP hofund_webserver_info Basic information about web server that is running this application\n" +
                "# TYPE hofund_webserver_info gauge\n" +
                "hofund_webserver_info{name=\"Apache Tomcat\",version=\"{version}\"} 1"
                .replace("{version}", version);

        //when:
        String response = template.getForObject(path, String.class);

        //then:
        log.info("Expecting: \n{}\nResponse: \n{}", expected, response);
        Assertions.assertTrue(response.contains(expected));
    }
}
