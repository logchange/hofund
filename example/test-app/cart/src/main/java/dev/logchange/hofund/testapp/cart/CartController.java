package dev.logchange.hofund.testapp.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class CartController {

    private final CartRepository repository;

    @Autowired
    public CartController(CartRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/carts")
    Iterable<Cart> all() {
        return repository.findAll();
    }

    @GetMapping("/carts/{id}")
    Cart userById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/carts")
    Cart save(@RequestBody Cart cart) {
        return repository.save(cart);
    }

}