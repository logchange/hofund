package dev.logchange.hofund.java;


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
public class HofundJavaInfoE2ETest {

    private final TestRestTemplate template = new TestRestTemplate();

    @LocalServerPort
    private int port;

    @Test
    void shouldContainHofundJavaInfo() {
        //given:
        String path = "http://localhost:" + port + "/actuator/prometheus";

        String jvmName = System.getProperty("java.vm.name");
        String jvmVendor = System.getProperty("java.vm.vendor");
        String jvmVersion = System.getProperty("java.vm.version");

        String runtimeName = System.getProperty("java.runtime.name");
        String runtimeVersion = System.getProperty("java.runtime.version");

        String vendor = System.getProperty("java.vendor");
        String vendorVersion = System.getProperty("java.vendor.version");

        String javaVersion = System.getProperty("java.version");

        String expected =
                "# HELP hofund_java_info Basic information about java that is running this application\n" +
                "# TYPE hofund_java_info gauge\n" +
                "hofund_java_info{jvm_name=\"{jvmName}\",jvm_vendor=\"{jvmVendor}\",jvm_version=\"{jvmVersion}\",runtime_name=\"{runtimeName}\",runtime_version=\"{runtimeVersion}\",vendor_name=\"{vendor}\",vendor_version=\"{vendorVersion}\",version=\"{javaVersion}\",} 1.0"
                .replace("{jvmName}", jvmName)
                .replace("{jvmVendor}", jvmVendor)
                .replace("{jvmVersion}", jvmVersion)
                .replace("{runtimeName}", runtimeName)
                .replace("{runtimeVersion}", runtimeVersion)
                .replace("{vendor}", vendor)
                .replace("{vendorVersion}", vendorVersion)
                .replace("{javaVersion}", javaVersion);

        //when:
        String response = template.getForObject(path, String.class);

        //then:
        log.info("Expecting: \n{}", expected);
        Assertions.assertTrue(response.contains(expected));
    }
}
