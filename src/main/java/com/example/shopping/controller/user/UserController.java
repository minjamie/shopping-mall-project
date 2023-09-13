package com.example.shopping.controller.user;

import com.example.shopping.dto.auth.AuthInfoUserId;
import com.example.shopping.dto.common.CommonResponse;
import com.example.shopping.dto.common.ResultDto;
import com.example.shopping.dto.user.CartListResponse;
import com.example.shopping.dto.user.OrderListResponse;
import com.example.shopping.security.CustomUserDetails;
import com.example.shopping.security.JwtTokenProvider;
import com.example.shopping.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Api(tags = "User APIs")
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    public static final String TOKEN_PREFIX = "Bearer ";

    @ApiOperation(value = "주문 목록 API", notes = "주문 목록 조회")
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/order")
    public ResponseEntity<ResultDto<OrderListResponse>> getOrderList(
             @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
             @RequestHeader("ACCESS-TOKEN") String accessToken
    ){
        AuthInfoUserId user = jwtTokenProvider.getUserId(accessToken.substring(TOKEN_PREFIX.length()));
        if(user != null){
            PageRequest pageable = PageRequest.of(page-1, size);

        CommonResponse orderListResponse = userService.getOrderList(user.getUserId(), pageable);
        ResultDto<OrderListResponse> result = ResultDto.in(
                orderListResponse.getStatus(),
                orderListResponse.getMessage()
        );
        result.setData((OrderListResponse) orderListResponse.getData());
        return ResponseEntity.status(orderListResponse.getHttpStatus()).body(result);
        } else {
            return ResponseEntity.status(403).body(null);
        }
    }

    @ApiOperation(value = "장바구니 목록 API", notes = "장바구니 목록 조회")
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/cart")
    public ResponseEntity<ResultDto<CartListResponse>> getCartList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestHeader("ACCESS-TOKEN") String accessToken
    ) {
        AuthInfoUserId user = jwtTokenProvider.getUserId(accessToken.substring(TOKEN_PREFIX.length()));
        if(user != null){
        PageRequest pageable = PageRequest.of(page-1, size);
        CommonResponse cartListResponse = userService.getCartList(user.getUserId(), pageable);
        ResultDto<CartListResponse> result  = ResultDto.in(
                cartListResponse.getStatus(),
                cartListResponse.getMessage()
        );
        result.setData((CartListResponse) cartListResponse.getData());
        return ResponseEntity.status(cartListResponse.getHttpStatus()).body(result);
        } else {
            return ResponseEntity.status(403).body(null);
        }
    }


    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/seller")
    public ResponseEntity<ResultDto<Void>> registerSeller(@RequestHeader("ACCESS-TOKEN") String accessToken){
        AuthInfoUserId user = jwtTokenProvider.getUserId(accessToken.substring(TOKEN_PREFIX.length()));
        CommonResponse insertSellerResponse = userService.insertSeller(user.getUserId());
        ResultDto<Void> result = ResultDto.in(
                insertSellerResponse.getStatus(),
                insertSellerResponse.getMessage()
        );
        return ResponseEntity.status(insertSellerResponse.getHttpStatus()).body(result);

    }
}
