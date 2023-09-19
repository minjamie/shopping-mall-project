package com.example.shopping.repository.image;

import com.example.shopping.domain.Image;
import com.example.shopping.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    Optional<Image> findImageByProduct(Product product);
}
