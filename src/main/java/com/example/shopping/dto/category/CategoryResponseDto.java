package com.example.shopping.dto.category;


import com.example.shopping.domain.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class CategoryResponseDto {
    private Integer id;
    private String name;

    public static CategoryResponseDto of(Category category){
        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
