package com.urbancart.ai.reviews;

import com.urbancart.ai.products.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    List<ReviewEntity> findByProduct(ProductEntity product);
}
