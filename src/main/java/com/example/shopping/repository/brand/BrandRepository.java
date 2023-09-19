package com.example.shopping.repository.brand;

import com.example.shopping.domain.Brand;
import com.example.shopping.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
    Optional<Brand> findBrandByProduct(Product product);
}
