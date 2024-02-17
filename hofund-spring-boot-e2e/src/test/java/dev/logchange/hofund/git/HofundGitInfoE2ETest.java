package dev.logchange.hofund.git;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

@Slf4j
@AutoConfigureObservability
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "hofund.git-info.commit.id=SomeIdForTest",
        "hofund.git-info.commit.id-abbrev=someAbbrevIdForE2eTest",
        "hofund.git-info.dirty=true",
        "hofund.git-info.branch=feature-1-for-test-e2e",
        "hofund.git-info.build.host=someHostForTest",
        "hofund.git-info.build.time=14:12:13T11-11-2023"
})
public class HofundGitInfoE2ETest {

    private final TestRestTemplate template = new TestRestTemplate();

    @LocalServerPort
    private int port;

    @Test
    void shouldContainsHofundDefaultGitInfoWithValuesFromGitProperties() {
        //given:
        String path = "http://localhost:" + port + "/actuator/prometheus";

        String expected =
                "# HELP hofund_git_info Basic information about application based on git\n" +
                "# TYPE hofund_git_info gauge\n" +
                "hofund_git_info{branch=\"feature-1-for-test-e2e\",build_host=\"someHostForTest\",build_time=\"14:12:13T11-11-2023\",commit_id=\"someAbbrevIdForE2eTest\",dirty=\"true\",} 1.0";

        //when:
        String response = template.getForObject(path, String.class);

        //then:
        log.info("Expecting: \n{}\nResponse: \n{}", expected, response);
        Assertions.assertTrue(response.contains(expected));
    }
}
