package com.urbancart.ai.products;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Page<ProductEntity> findByCategoryIgnoreCase(String category, Pageable pageable);
    Page<ProductEntity> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query("""
            SELECT p FROM ProductEntity p
            WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :term, '%'))
               OR LOWER(p.description) LIKE LOWER(CONCAT('%', :term, '%'))
               OR LOWER(p.category) LIKE LOWER(CONCAT('%', :term, '%'))
            """)
    Page<ProductEntity> searchByTermAcrossFields(@Param("term") String term, Pageable pageable);
}