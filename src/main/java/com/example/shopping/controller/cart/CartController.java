package com.example.shopping.controller.cart;

import com.example.shopping.dto.cart.AddCartRequest;
import com.example.shopping.dto.cart.CartResponse;
import com.example.shopping.dto.cart.OrderCartRequest;
import com.example.shopping.dto.cart.UpdatedCartRequest;
import com.example.shopping.dto.common.ResultDto;
import com.example.shopping.service.cart.CartService;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping("/{productId}")
    public ResponseEntity<ResultDto<Void>> addCart(@PathVariable String productId, @RequestBody AddCartRequest addCartRequest){
        CartResponse addCartResponse = cartService.addCart(Integer.valueOf(productId), addCartRequest);
        ResultDto<Void> result  = ResultDto.in(addCartResponse.getStatus(), addCartResponse.getMessage());
        return ResponseEntity.status(addCartResponse.getHttpStatus()).body(result);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{cartId}")
    public ResponseEntity<ResultDto<Void>> updateCart(@PathVariable String cartId, @RequestBody UpdatedCartRequest updateCartRequest){
        CartResponse updatedCartResponse = cartService.updateCart(Integer.valueOf(cartId), updateCartRequest);
        ResultDto<Void> result  = ResultDto.in(updatedCartResponse.getStatus(), updatedCartResponse.getMessage());
        return ResponseEntity.status(updatedCartResponse.getHttpStatus()).body(result);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/order")
    public ResponseEntity<ResultDto<Void>> orderCart(@RequestParam("cartIds") List<Integer> cartIds, @RequestBody OrderCartRequest orderCartRequest){
        CartResponse orderCartResponse = cartService.orderCart(cartIds, orderCartRequest);
        ResultDto<Void> result  = ResultDto.in(orderCartResponse.getStatus(), orderCartResponse.getMessage());
        return ResponseEntity.status(orderCartResponse.getHttpStatus()).body(result);
    }
}
