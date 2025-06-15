package dev.logchange.hofund;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

//    private final TestRepository testRepository;

    @GetMapping("/test")
    public String test() {
        return "test";
    }

//    @GetMapping("/testEntity")
//    public Iterable<TestEntity> testEntity() {
//        return testRepository.findAll();
//    }
}
