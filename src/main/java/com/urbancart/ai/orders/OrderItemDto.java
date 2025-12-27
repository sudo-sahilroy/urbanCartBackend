package com.urbancart.ai.orders;

import com.urbancart.ai.products.ProductDto;

import java.math.BigDecimal;

public record OrderItemDto(Long id, ProductDto product, BigDecimal price, Integer quantity) {}
