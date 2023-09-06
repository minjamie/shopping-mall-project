package com.example.shopping.controller.cart;

import com.example.shopping.dto.auth.AuthInfoUserId;
import com.example.shopping.dto.cart.AddCartRequest;
import com.example.shopping.dto.common.CommonResponse;
import com.example.shopping.dto.cart.OrderCartRequest;
import com.example.shopping.dto.cart.UpdatedCartRequest;
import com.example.shopping.dto.common.ResultDto;
import com.example.shopping.security.CustomUserDetails;
import com.example.shopping.security.JwtTokenProvider;
import com.example.shopping.service.auth.AuthService;
import com.example.shopping.service.cart.CartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
@Api(tags = "Cart-Order APIs")
public class CartController {

    private final CartService cartService;
    private final JwtTokenProvider jwtTokenProvider;
    public static final String TOKEN_PREFIX = "Bearer ";

    @ApiOperation(value = "장바구니 등록 API", notes = "장바구니 추가")
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping("/{productId}")
    public ResponseEntity<ResultDto<Void>> addCart(
            @PathVariable String productId,
            @RequestBody AddCartRequest addCartRequest,
            @RequestHeader("ACCESS-TOKEN") String accessToken){
        AuthInfoUserId user = jwtTokenProvider.getUserId(accessToken.substring(TOKEN_PREFIX.length()));
        if(user != null){
            System.out.println(user);
            CommonResponse addCommonResponse = cartService.addCart(Integer.valueOf(productId), addCartRequest, user.getUserId());
            ResultDto<Void> result  = ResultDto.in(addCommonResponse.getStatus(), addCommonResponse.getMessage());
            return ResponseEntity.status(200).body(result);
        } else {
            return ResponseEntity.status(403).body(null);
        }
    }

    @ApiOperation(value = "장바구니 수정 API", notes = "장바구니 수정")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{cartId}")
    public ResponseEntity<ResultDto<Void>> updateCart(
            @PathVariable String cartId,
            @RequestBody UpdatedCartRequest updateCartRequest,
            @RequestHeader("ACCESS-TOKEN") String accessToken) {
        AuthInfoUserId user = jwtTokenProvider.getUserId(accessToken.substring(TOKEN_PREFIX.length()));
            if(user != null){
                CommonResponse updatedCommonResponse = cartService.updateCart(Integer.valueOf(cartId), updateCartRequest, user.getUserId());
                ResultDto<Void> result  = ResultDto.in(updatedCommonResponse.getStatus(), updatedCommonResponse.getMessage());
                return ResponseEntity.status(updatedCommonResponse.getHttpStatus()).body(result);
            } else {
                return ResponseEntity.status(403).body(null);
            }
    }

    @ApiOperation(value = "주문 등록 API", notes = "장바구니에 담긴 상품 주문하기")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/order")
    public ResponseEntity<ResultDto<Void>> orderCart(
            @RequestParam("cartIds") List<Integer> cartIds,
            @RequestBody OrderCartRequest orderCartRequest,
            @RequestHeader("ACCESS-TOKEN") String accessToken) {
        AuthInfoUserId user = jwtTokenProvider.getUserId(accessToken.substring(TOKEN_PREFIX.length()));
        if(user != null) {
            CommonResponse orderCommonResponse = cartService.orderCart(cartIds, orderCartRequest, user.getUserId());
            ResultDto<Void> result = ResultDto.in(orderCommonResponse.getStatus(), orderCommonResponse.getMessage());
            return ResponseEntity.status(orderCommonResponse.getHttpStatus()).body(result);
        }  else {
            return ResponseEntity.status(403).body(null);
        }
    }
}
