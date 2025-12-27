package com.urbancart.ai.cart;

import com.urbancart.ai.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartItemEntity, Long> {
    List<CartItemEntity> findByUser(UserEntity user);
    Optional<CartItemEntity> findByUserAndProductId(UserEntity user, Long productId);
    void deleteByUserAndProductId(UserEntity user, Long productId);
}
