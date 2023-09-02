package com.example.shopping.service.cart;

import com.example.shopping.domain.Product;
import com.example.shopping.domain.ProductOption;
import com.example.shopping.dto.cart.AddCartRequest;
import com.example.shopping.dto.cart.CartResponse;
import com.example.shopping.dto.cart.UpdatedCartRequest;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public interface CartServiceInterface {
    CartResponse addCart(Integer productId, AddCartRequest addCartRequest);

    CartResponse updateCart(Integer productId, UpdatedCartRequest updateCartRequest);
}
