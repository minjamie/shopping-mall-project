package com.example.shopping.controller.user;

import com.example.shopping.dto.cart.AddCartRequest;
import com.example.shopping.dto.common.CommonResponse;
import com.example.shopping.dto.common.ResultDto;
import com.example.shopping.dto.user.UserOrderResponse;
import com.example.shopping.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/{userId}/order")
    public ResponseEntity<ResultDto<Void>> getOrderList(@PathVariable String userId){
        CommonResponse orderListResponse = userService.getOrderList(Integer.valueOf(userId));
        ResultDto<Void> result  = ResultDto.in(orderListResponse.getStatus(), orderListResponse.getMessage());
        return ResponseEntity.status(orderListResponse.getHttpStatus()).body(result);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/{userId}/cart")
    public ResponseEntity<ResultDto<Void>> getCartList(@PathVariable String userId){
        CommonResponse cartListResponse = userService.getCartList(Integer.valueOf(userId));
        ResultDto<Void> result  = ResultDto.in(cartListResponse.getStatus(), cartListResponse.getMessage());
        return ResponseEntity.status(cartListResponse.getHttpStatus()).body(result);
    }
}
