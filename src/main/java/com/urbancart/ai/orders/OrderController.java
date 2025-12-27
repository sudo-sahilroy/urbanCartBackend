package com.urbancart.ai.orders;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderDto> create(@Valid @RequestBody OrderCreateRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @GetMapping("/me")
    public ResponseEntity<List<OrderDto>> mine() {
        return ResponseEntity.ok(orderService.myOrders());
    }
}
