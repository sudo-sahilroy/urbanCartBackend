package com.urbancart.ai.orders;

import com.urbancart.ai.cart.CartItemEntity;
import com.urbancart.ai.cart.CartRepository;
import com.urbancart.ai.products.ProductDto;
import com.urbancart.ai.users.UserEntity;
import com.urbancart.ai.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public OrderDto createOrder(OrderCreateRequest request) {
        UserEntity user = currentUser();
        List<CartItemEntity> items = cartRepository.findByUser(user);
        BigDecimal total = items.stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        OrderEntity order = OrderEntity.builder()
                .user(user)
                .totalAmount(total)
                .shippingAddress(request.shippingAddress())
                .createdAt(Instant.now())
                .build();

        List<OrderItemEntity> orderItems = items.stream().map(item -> OrderItemEntity.builder()
                .order(order)
                .product(item.getProduct())
                .price(item.getProduct().getPrice())
                .quantity(item.getQuantity())
                .build()).toList();

        order.setItems(orderItems);
        orderRepository.save(order);
        cartRepository.deleteAll(items);

        return toDto(orderRepository.findById(order.getId()).orElseThrow());
    }

    public List<OrderDto> myOrders() {
        UserEntity user = currentUser();
        return orderRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(this::toDto)
                .toList();
    }

    private OrderDto toDto(OrderEntity order) {
        List<OrderItemDto> items = order.getItems().stream()
                .map(oi -> new OrderItemDto(oi.getId(), ProductDto.from(oi.getProduct()), oi.getPrice(), oi.getQuantity()))
                .toList();
        return new OrderDto(order.getId(), order.getTotalAmount(), order.getShippingAddress(), order.getCreatedAt(), items);
    }

    private UserEntity currentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow();
    }
}
