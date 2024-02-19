//package dev.logchange.hofund.connection.spring.datasources.oracle;
//
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.test.web.server.LocalServerPort;
//
//@Slf4j
//@AutoConfigureObservability
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
//        properties = {
//                "spring.datasource.url=jdbc:oracle:thin:@localhost:1521:xe",
//                "spring.datasource.username=system",
//                "spring.datasource.password=password",
//                "spring.datasource.driver-class-name=oracle.jdbc.OracleDriver"
//        })
//public class OracleDataSourceE2ETest {
//    private final TestRestTemplate template = new TestRestTemplate();
//
//    @LocalServerPort
//    private int port;
//
//    @Test
//    void shouldContainsHofundConnectionForOracleDatabase() {
//        //given:
//        String path = "http://localhost:" + port + "/actuator/prometheus";
//
//        String expected = "# HELP hofund_info Basic information about application\n" +
//                "# TYPE hofund_info gauge\n" +
//                "hofund_info{application_name=\"testapp\",application_version=\"1.2.3\",id=\"testapp\",} 1.0";
//
//        //when:
//        String response = template.getForObject(path, String.class);
//
//        //then:
//        log.info("Expecting: \n{}\nResponse: \n{}", expected, response);
//        Assertions.assertTrue(response.contains(expected));
//    }
//}
