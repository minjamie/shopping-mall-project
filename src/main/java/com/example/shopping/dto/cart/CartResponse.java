package com.example.shopping.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CartResponse {
    private String productName;
    private String brandName;
    private Integer count;
    private Integer price;
    private Integer deliveryPrice;
    private String optionCategory;
    private String optionName;
}
