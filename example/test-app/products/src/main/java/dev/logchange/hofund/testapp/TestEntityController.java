package dev.logchange.hofund.testapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class TestEntityController {

    private final TestEntityRepository repository;

    @Autowired
    public TestEntityController(TestEntityRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/test/all")
    Iterable<TestEntity> all() {
        return repository.findAll();
    }

    @GetMapping("/test/{id}")
    TestEntity userById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/user/save")
    TestEntity save(@RequestBody TestEntity user) {
        return repository.save(user);
    }

}