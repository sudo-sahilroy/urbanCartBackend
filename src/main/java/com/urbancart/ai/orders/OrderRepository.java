package com.urbancart.ai.orders;

import com.urbancart.ai.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByUserOrderByCreatedAtDesc(UserEntity user);
}
