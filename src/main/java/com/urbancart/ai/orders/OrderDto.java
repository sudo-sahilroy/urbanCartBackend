package com.urbancart.ai.orders;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderDto(Long id, BigDecimal totalAmount, String shippingAddress, Instant createdAt, List<OrderItemDto> items) {}
