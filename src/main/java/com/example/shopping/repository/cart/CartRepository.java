package com.example.shopping.repository.cart;

import com.example.shopping.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByIdAndUserId(Integer id, Integer userId);
}
