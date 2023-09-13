package com.example.shopping.dto.category;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class SubCategoryListResponseDto {
    private List<SubCategoryResponseDto> subCategoryList;
}
