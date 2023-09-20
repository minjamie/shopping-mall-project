package com.example.shopping.dto.product;


import com.example.shopping.dto.common.Pagination;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductListResponseDto {
    private Pagination pagination;
    private List<ProductResponseDto> productList;
}
