package com.example.shopping.dto.user;

import com.example.shopping.dto.cart.CartResponse;
import com.example.shopping.dto.common.Pagination;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CartListResponse {
    private List<CartResponse> cartList;
    private Pagination pagination;
}
