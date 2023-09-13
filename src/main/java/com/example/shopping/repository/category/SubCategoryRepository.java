package com.example.shopping.repository.category;

import com.example.shopping.domain.Category;
import com.example.shopping.domain.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Integer> {
    List<SubCategory> findSubCategoriesByCategory(Category category);
}
