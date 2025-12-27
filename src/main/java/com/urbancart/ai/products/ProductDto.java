package com.urbancart.ai.products;

import java.math.BigDecimal;
import java.util.List;

public record ProductDto(Long id, String title, String description, BigDecimal price, String category, Double rating, List<String> imageUrls) {
    public static ProductDto from(ProductEntity entity) {
        return new ProductDto(entity.getId(), entity.getTitle(), entity.getDescription(), entity.getPrice(), entity.getCategory(), entity.getRating(), entity.getImageUrls());
    }
}
