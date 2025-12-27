package com.urbancart.ai.cart;

import com.urbancart.ai.products.ProductDto;

public record CartItemDto(Long id, ProductDto product, Integer quantity) {}
