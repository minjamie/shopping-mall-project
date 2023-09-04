package com.example.shopping.controller.cart;

import com.example.shopping.dto.cart.AddCartRequest;
import com.example.shopping.dto.common.CommonResponse;
import com.example.shopping.dto.cart.OrderCartRequest;
import com.example.shopping.dto.cart.UpdatedCartRequest;
import com.example.shopping.dto.common.ResultDto;
import com.example.shopping.service.cart.CartService;
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
        CommonResponse addCommonResponse = cartService.addCart(Integer.valueOf(productId), addCartRequest);
        ResultDto<Void> result  = ResultDto.in(addCommonResponse.getStatus(), addCommonResponse.getMessage());
        return ResponseEntity.status(addCommonResponse.getHttpStatus()).body(result);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{cartId}")
    public ResponseEntity<ResultDto<Void>> updateCart(@PathVariable String cartId, @RequestBody UpdatedCartRequest updateCartRequest){
        CommonResponse updatedCommonResponse = cartService.updateCart(Integer.valueOf(cartId), updateCartRequest);
        ResultDto<Void> result  = ResultDto.in(updatedCommonResponse.getStatus(), updatedCommonResponse.getMessage());
        return ResponseEntity.status(updatedCommonResponse.getHttpStatus()).body(result);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/order")
    public ResponseEntity<ResultDto<Void>> orderCart(@RequestParam("cartIds") List<Integer> cartIds, @RequestBody OrderCartRequest orderCartRequest){
        CommonResponse orderCommonResponse = cartService.orderCart(cartIds, orderCartRequest);
        ResultDto<Void> result  = ResultDto.in(orderCommonResponse.getStatus(), orderCommonResponse.getMessage());
        return ResponseEntity.status(orderCommonResponse.getHttpStatus()).body(result);
    }
}
