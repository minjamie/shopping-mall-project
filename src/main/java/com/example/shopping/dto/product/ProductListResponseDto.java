package com.example.shopping.dto.product;


import com.example.shopping.dto.common.Pagination;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProductListResponseDto {
    private Pagination pagination;
    private List<ProductResponseDto> productList;

}
