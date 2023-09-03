package com.example.shopping.repository.order;

import com.example.shopping.domain.Order;
import com.example.shopping.dto.user.OrderListResponse;
import com.example.shopping.dto.user.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT new com.example.shopping.dto.user.OrderResponse(p.name AS productName, b.name AS brandName, " +
            "c.count, c.count * c.productOption.product.price AS price, " +
            "po.option.category AS optionCategory, po.option.name AS optionName, " +
            "p.deliveryPrice, o.createdAt AS orderDate) " +
            "FROM Order  o " +
            "JOIN o.cart c " +
            "JOIN c.productOption po " +
            "JOIN po.product p " +
            "JOIN p.brands b " +
            "WHERE c.user.id = :userId")
    Page<OrderResponse> findOrderList(Integer userId, Pageable page);
}
