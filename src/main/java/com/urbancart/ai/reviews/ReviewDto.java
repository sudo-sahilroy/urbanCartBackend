package com.urbancart.ai.reviews;

import java.time.Instant;

public record ReviewDto(Long id, String userName, Integer rating, String comment, Instant createdAt) {
    public static ReviewDto from(ReviewEntity entity) {
        return new ReviewDto(entity.getId(), entity.getUser().getFullName(), entity.getRating(), entity.getComment(), entity.getCreatedAt());
    }
}
