package com.urbancart.ai.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateCartRequest(@NotNull Long productId, @Min(1) Integer quantity) {}
