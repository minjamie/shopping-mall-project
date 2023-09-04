package com.example.shopping.dto.cart;

import com.example.shopping.domain.Cart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@AllArgsConstructor
@Setter
public class OrderCartResponse {
    private Cart cart;
    private Integer price; // 또는 필요한 데이터 타입으로 변경
}
