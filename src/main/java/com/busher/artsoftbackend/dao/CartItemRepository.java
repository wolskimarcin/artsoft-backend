package com.busher.artsoftbackend.dao;

import com.busher.artsoftbackend.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByIdAndCartUserId(Long id, Long userId);

    boolean existsByIdAndCartUserId(Long itemId, Long userId);
}
