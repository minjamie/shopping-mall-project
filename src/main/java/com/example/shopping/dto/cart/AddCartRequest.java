package com.example.shopping.dto.cart;

import lombok.Getter;
@Getter
public class AddCartRequest {
    private Integer userId;
    private Integer optionId;
    private Integer count;
}
