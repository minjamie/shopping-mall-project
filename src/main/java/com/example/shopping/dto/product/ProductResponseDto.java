package com.example.shopping.dto.product;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {
    private Integer productId;
    private String name;
    private String brand;
    private Boolean isDiscount;
    private Boolean isNew;
    private Integer discountRate;
    private Integer price;
    private String imageUrl;
}
