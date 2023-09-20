package com.example.shopping.repository.product;

import com.example.shopping.domain.Category;
import com.example.shopping.domain.Product;
import com.example.shopping.dto.cart.ProductOptionDto;
import com.example.shopping.dto.product.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findProductByCategory(Category category);

    @Query("select new com.example.shopping.dto.product.ProductResponseDto(p.id as productId, p.name as name, " +
            "b.name as brand, p.isDiscount as isDiscount, p.isNew as isNew, p.discountRate as discountRate, p.price as price, i.url as imageUrl) " +
            "from Product p " +
            "left join p.brands b " +
            "on p.id = b.product.id " +
            "left join p.images i " +
            "on b.product.id = i.product.id ")
    Page<ProductResponseDto> getProductListAll(Pageable pageable);


    @Query("SELECT new com.example.shopping.dto.cart.ProductOptionDto(p.id, po.stock) " +
            "FROM Product p " +
            "INNER JOIN p.productOptions po " +
            "WHERE p.id= :productId AND po.option.id = :optionId")
    Optional<ProductOptionDto> findByProductIdAndOptionId(@Param("productId") Integer productId, Integer optionId);
}
