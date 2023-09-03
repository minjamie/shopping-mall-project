package com.example.shopping.repository.cart;

import com.example.shopping.domain.Cart;
import com.example.shopping.domain.Product;
import com.example.shopping.dto.cart.OrderCartResponse;
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
}
