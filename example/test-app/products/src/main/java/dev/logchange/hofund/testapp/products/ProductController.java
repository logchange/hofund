package dev.logchange.hofund.testapp.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ProductController {

    private final ProductRepository repository;

    @Autowired
    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/products")
    Iterable<Product> all() {
        return repository.findAll();
    }

    @GetMapping("/products/{id}")
    Product userById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/products")
    Product save(@RequestBody Product product) {
        return repository.save(product);
    }

}