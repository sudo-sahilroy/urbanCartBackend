package com.urbancart.ai.products;

import com.urbancart.ai.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Page<ProductDto> getProducts(String category, String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductEntity> productPage;
        if (category != null && !category.isBlank()) {
            productPage = productRepository.findByCategoryIgnoreCase(category, pageable);
        } else if (search != null && !search.isBlank()) {
            productPage = productRepository.findByTitleContainingIgnoreCase(search, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }
        return productPage.map(ProductDto::from);
    }

    public ProductDto getProduct(Long id) {
        return productRepository.findById(id)
                .map(ProductDto::from)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    public List<ProductDto> getRecommended(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return productRepository.findAll(pageable).stream().map(ProductDto::from).toList();
    }
}
