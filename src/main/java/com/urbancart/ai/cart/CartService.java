package com.urbancart.ai.cart;

import com.urbancart.ai.common.exception.ResourceNotFoundException;
import com.urbancart.ai.products.ProductDto;
import com.urbancart.ai.products.ProductEntity;
import com.urbancart.ai.products.ProductRepository;
import com.urbancart.ai.users.UserEntity;
import com.urbancart.ai.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public List<CartItemDto> getCart() {
        UserEntity user = currentUser();
        return cartRepository.findByUser(user)
                .stream()
                .map(item -> new CartItemDto(item.getId(), ProductDto.from(item.getProduct()), item.getQuantity()))
                .toList();
    }

    public List<CartItemDto> add(AddToCartRequest request) {
        UserEntity user = currentUser();
        ProductEntity product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        CartItemEntity item = cartRepository.findByUserAndProductId(user, product.getId())
                .orElse(CartItemEntity.builder().user(user).product(product).quantity(0).build());
        item.setQuantity(item.getQuantity() + request.quantity());
        cartRepository.save(item);
        return getCart();
    }

    public List<CartItemDto> update(UpdateCartRequest request) {
        UserEntity user = currentUser();
        CartItemEntity item = cartRepository.findByUserAndProductId(user, request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        item.setQuantity(request.quantity());
        cartRepository.save(item);
        return getCart();
    }

    public List<CartItemDto> remove(Long productId) {
        UserEntity user = currentUser();
        cartRepository.deleteByUserAndProductId(user, productId);
        return getCart();
    }

    public BigDecimal totalAmount(List<CartItemEntity> items) {
        return items.stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private UserEntity currentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow();
    }
}
