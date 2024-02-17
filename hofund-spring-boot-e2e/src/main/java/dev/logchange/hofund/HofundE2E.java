package dev.logchange.hofund;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class HofundE2E {

    public static void main(String[] args) {
        SpringApplication.run(HofundE2E.class, args);
    }


    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
