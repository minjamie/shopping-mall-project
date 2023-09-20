package com.example.shopping.dto.product;

import com.example.shopping.domain.Brand;
import com.example.shopping.domain.Image;
import com.example.shopping.domain.Product;
import com.example.shopping.domain.ProductOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ProductDetailResponseDto {
    private String name;
    private String brand;
    private Integer price;
    private String description;
    private Integer discountRate;
    private Boolean isDiscount;
    private Boolean isNew;
    private Integer deliveryPrice;
    private LocalDateTime saleStartDate;
    private LocalDateTime saleEndDate;
    private String imageType;
    private String imageUrl;
    private List<ProductOptionDto> optionList;


    @Getter @Setter
    @Builder @AllArgsConstructor
    public static class ProductOptionDto{
        private Integer optionId;
        private String category; // color, size
        private String name;
        private Integer stock; // 재고
    }


    public static ProductDetailResponseDto of(Product product, Brand brand, Image image, List<ProductOption> options){
        List<ProductDetailResponseDto.ProductOptionDto> optionDtoList = options.stream()
                .map(option -> ProductOptionDto.builder()
                        .optionId(option.getId())
                        .category(option.getOption().getCategory())
                        .name(option.getOption().getName())
                        .stock(option.getStock())
                        .build())
                .collect(Collectors.toList());


        return ProductDetailResponseDto.builder()
                .name(product.getName())
                .brand(brand.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .discountRate(product.getDiscountRate())
                .isDiscount(product.getIsDiscount())
                .isNew(product.getIsNew())
                .deliveryPrice(product.getDeliveryPrice())
                .saleStartDate(product.getSaleStartDate())
                .saleEndDate(product.getSaleEndDate())
                .imageUrl(image.getUrl())
                .imageType(image.getType())
                .optionList(optionDtoList)
                .build();
    }
}
