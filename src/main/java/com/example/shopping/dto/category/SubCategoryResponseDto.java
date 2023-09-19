package com.example.shopping.dto.category;

import com.example.shopping.domain.SubCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class SubCategoryResponseDto {
    private Integer id;
    private String name;

    public static SubCategoryResponseDto of(SubCategory subCategory){
        return SubCategoryResponseDto.builder()
                .id(subCategory.getId())
                .name(subCategory.getName())
                .build();
    }
}
