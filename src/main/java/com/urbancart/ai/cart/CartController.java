package com.urbancart.ai.cart;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartItemDto>> getCart() {
        return ResponseEntity.ok(cartService.getCart());
    }

    @PostMapping("/add")
    public ResponseEntity<List<CartItemDto>> add(@Valid @RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(cartService.add(request));
    }

    @PostMapping("/update")
    public ResponseEntity<List<CartItemDto>> update(@Valid @RequestBody UpdateCartRequest request) {
        return ResponseEntity.ok(cartService.update(request));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<List<CartItemDto>> remove(@PathVariable Long productId) {
        return ResponseEntity.ok(cartService.remove(productId));
    }
}
