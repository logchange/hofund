package dev.logchange.hofund.git;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.slf4j.LoggerFactory.getLogger;

@AutoConfigureObservability
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HofundDefaultGitInfoE2ETest {

    private static final Logger log = getLogger(HofundDefaultGitInfoE2ETest.class);

    private final TestRestTemplate template = new TestRestTemplate();

    @LocalServerPort
    private int port;

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("git.commit.id", () -> 123456);
        registry.add("git.commit.id.abbrev", () -> "abcdef");
        registry.add("git.dirty", () -> false);
        registry.add("git.branch", () -> "alaMaKota");
        registry.add("git.build.host", () -> "someHostName");
        registry.add("git.build.time", () -> "11:12:33T17-02-2023");
    }

    @Test
    void shouldContainsHofundDefaultGitInfoWithValuesFromGitProperties() {
        //given:
        String path = "http://localhost:" + port + "/actuator/prometheus";

        String expected = "# HELP hofund_git_info Basic information about application based on git\n" +
                "# TYPE hofund_git_info gauge\n" +
                "hofund_git_info{branch=\"alaMaKota\",build_host=\"someHostName\",build_time=\"11:12:33T17-02-2023\",commit_id=\"abcdef\",dirty=\"false\"} 1";

        //when:
        String response = template.getForObject(path, String.class);

        //then:
        log.info("Expecting: \n{}\nResponse: \n{}", expected, response);
        Assertions.assertTrue(response.contains(expected));
    }
}
