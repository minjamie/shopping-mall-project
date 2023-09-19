package com.example.shopping.repository.product;

import com.example.shopping.domain.Category;
import com.example.shopping.domain.Product;
import com.example.shopping.dto.cart.ProductOptionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findProductByCategory(Category category);

    @Query("SELECT new com.example.shopping.dto.cart.ProductOptionDto(p.id, po.stock) " +
            "FROM Product p " +
            "INNER JOIN p.productOptions po " +
            "WHERE p.id= :productId AND po.option.id = :optionId")
    Optional<ProductOptionDto> findByProductIdAndOptionId(@Param("productId") Integer productId, Integer optionId);
}
