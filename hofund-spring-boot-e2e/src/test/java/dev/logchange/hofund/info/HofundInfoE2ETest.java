package dev.logchange.hofund.info;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.slf4j.LoggerFactory.getLogger;

@AutoConfigureObservability
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "hofund.info.application.name=TestApp",
                "hofund.info.application.version=1.2.3"
        }
)
public class HofundInfoE2ETest {

    private static final Logger log = getLogger(HofundInfoE2ETest.class);

    private final TestRestTemplate template = new TestRestTemplate();

    @LocalServerPort
    private int port;

    @Test
    void shouldContainsHofundInfoWithValuesFromProperties() {
        //given:
        String path = "http://localhost:" + port + "/actuator/prometheus";

        String expected = "# HELP hofund_info Basic information about application\n" +
                "# TYPE hofund_info gauge\n" +
                "hofund_info{application_name=\"testapp\",application_version=\"1.2.3\",id=\"testapp\"} 1";

        //when:
        String response = template.getForObject(path, String.class);

        //then:
        log.info("Expecting: \n{}\nResponse: \n{}", expected, response);
        Assertions.assertTrue(response.contains(expected));
    }
}
