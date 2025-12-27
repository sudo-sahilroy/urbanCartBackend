package com.urbancart.ai.recommendation;

import jakarta.validation.constraints.NotBlank;

public record RecommendationRequest(@NotBlank String query) {}
