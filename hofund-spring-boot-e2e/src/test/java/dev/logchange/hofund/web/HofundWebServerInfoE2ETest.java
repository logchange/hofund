package dev.logchange.hofund.web;


import org.apache.catalina.util.ServerInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.slf4j.LoggerFactory.getLogger;

@AutoConfigureObservability
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HofundWebServerInfoE2ETest {

    private static final Logger log = getLogger(HofundWebServerInfoE2ETest.class);

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
