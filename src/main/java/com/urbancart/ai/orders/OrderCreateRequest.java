package com.urbancart.ai.orders;

import jakarta.validation.constraints.NotBlank;

public record OrderCreateRequest(
        @NotBlank String shippingAddress
) {}
