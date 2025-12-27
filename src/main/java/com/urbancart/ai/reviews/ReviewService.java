package com.urbancart.ai.reviews;

import com.urbancart.ai.common.exception.ResourceNotFoundException;
import com.urbancart.ai.products.ProductEntity;
import com.urbancart.ai.products.ProductRepository;
import com.urbancart.ai.users.UserEntity;
import com.urbancart.ai.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public List<ReviewDto> getReviews(Long productId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return reviewRepository.findByProduct(product).stream().map(ReviewDto::from).toList();
    }

    public ReviewDto addReview(Long productId, ReviewRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(email).orElseThrow();
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        ReviewEntity review = ReviewEntity.builder()
                .product(product)
                .user(user)
                .rating(request.rating())
                .comment(request.comment())
                .createdAt(Instant.now())
                .build();
        return ReviewDto.from(reviewRepository.save(review));
    }
}
