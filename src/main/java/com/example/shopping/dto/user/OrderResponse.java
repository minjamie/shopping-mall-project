package com.example.shopping.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponse {
    private String productName;
    private String brandName;
    private Integer count;
    private Integer price;
    private String optionCategory;
    private String optionName;
    private Integer deliveryPrice;
    private LocalDateTime orderDate;
}
