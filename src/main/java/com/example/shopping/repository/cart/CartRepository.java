package com.example.shopping.repository.cart;

import com.example.shopping.domain.Cart;
import com.example.shopping.domain.Product;
import com.example.shopping.dto.cart.CartResponse;
import com.example.shopping.dto.cart.OrderCartResponse;
import com.example.shopping.dto.user.OrderListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByIdAndUserIdAndIsDeleteFalse(Integer id, Integer userId);

    @Query("SELECT new com.example.shopping.dto.cart.OrderCartResponse(c, p.price) " +
            "FROM Cart c " +
            "INNER JOIN c.productOption po " +
            "INNER JOIN po.product p " +
            "WHERE c.id IN :ids AND c.isDelete = FALSE "
     )
    List<OrderCartResponse> findAllProductByIds(@Param("ids") List<Integer> ids);

    @Query("SELECT new com.example.shopping.dto.cart.CartResponse(p.name AS productName, b.name AS brandName, " +
            "c.count, c.count * c.productOption.product.price AS price, p.deliveryPrice, " +
            "po.option.category AS optionCategory, po.option.name AS optionName) " +
            "FROM Cart  c " +
            "JOIN c.productOption po " +
            "JOIN po.product p " +
            "JOIN p.brands b " +
            "WHERE c.user.id = :userId")
    Page<CartResponse> findCartList(Integer userId, Pageable pageable);
}
