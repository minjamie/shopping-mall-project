package com.example.shopping.dto.product;

import com.example.shopping.domain.Enum.SellStatus;
import com.example.shopping.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductRequestDto {
    private Integer productId;
    private Integer categoryId;
    private Integer subcategoryId;
    private Integer optionId;
    private String name;
    private Integer price;
    private Integer stock;
    private String description;
    private SellStatus sellStatus;
    private Integer discountRate;
    private Boolean isDiscount;
    private Boolean isNew;
    private Integer deliveryPrice;
    private LocalDateTime saleStartDate;
    private LocalDateTime saleEndDate;
    private String imageType;
    private String imageUrl;

    public Product toEntity(){
        return Product.builder()
                .name(name)
                .price(price)
                .description(description)
                .sellStatus(sellStatus)
                .discountRate(discountRate)
                .isDiscount(isDiscount)
                .isNew(isNew)
                .deliveryPrice(deliveryPrice)
                .saleStartDate(saleStartDate)
                .saleEndDate(saleEndDate)
                .build();
    }
}
