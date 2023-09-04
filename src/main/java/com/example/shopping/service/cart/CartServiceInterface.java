package com.example.shopping.service.cart;

import com.example.shopping.dto.cart.AddCartRequest;
import com.example.shopping.dto.common.CommonResponse;
import com.example.shopping.dto.cart.UpdatedCartRequest;

public interface CartServiceInterface {
    CommonResponse addCart(Integer productId, AddCartRequest addCartRequest);

    CommonResponse updateCart(Integer productId, UpdatedCartRequest updateCartRequest);
}
