package com.urbancart.ai.recommendation;

import com.urbancart.ai.products.ProductDto;

import java.util.List;

public record RecommendationResponse(List<ProductDto> recommendations) {}
