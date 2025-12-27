package com.urbancart.ai.reviews;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products/{productId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<ReviewDto>> list(@PathVariable(name = "productId") Long productId) {
        return ResponseEntity.ok(reviewService.getReviews(productId));
    }

    @PostMapping
    public ResponseEntity<ReviewDto> create(@PathVariable(name = "productId") Long productId, @Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(reviewService.addReview(productId, request));
    }
}
