package com.example.shopping.controller.user;

import com.example.shopping.dto.common.CommonResponse;
import com.example.shopping.dto.common.ResultDto;
import com.example.shopping.dto.user.CartListResponse;
import com.example.shopping.dto.user.OrderListResponse;
import com.example.shopping.security.CustomUserDetails;
import com.example.shopping.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/order")
    public ResponseEntity<ResultDto<OrderListResponse>> getOrderList(
             @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ){
        PageRequest pageable = PageRequest.of(page-1, size);

        CommonResponse orderListResponse = userService.getOrderList(1, pageable);
        ResultDto<OrderListResponse> result = ResultDto.in(
                orderListResponse.getStatus(),
                orderListResponse.getMessage()
        );
        result.setData((OrderListResponse) orderListResponse.getData());
        return ResponseEntity.status(orderListResponse.getHttpStatus()).body(result);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/cart")
    public ResponseEntity<ResultDto<CartListResponse>> getCartList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ){
        PageRequest pageable = PageRequest.of(page-1, size);
        CommonResponse cartListResponse = userService.getCartList(1, pageable);
        ResultDto<CartListResponse> result  = ResultDto.in(
                cartListResponse.getStatus(),
                cartListResponse.getMessage()
        );
        result.setData((CartListResponse) cartListResponse.getData());
        return ResponseEntity.status(cartListResponse.getHttpStatus()).body(result);
    }

    /**
     * 판매자등록(화정)
     * @param
     * @return
     */
    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/seller")
    public ResponseEntity<ResultDto<Void>> registerSeller(@AuthenticationPrincipal CustomUserDetails userDetails){
        CommonResponse insertSellerResponse = userService.insertSeller(userDetails.getEmail());
        ResultDto<Void> result = ResultDto.in(
                insertSellerResponse.getStatus(),
                insertSellerResponse.getMessage()
        );
        return ResponseEntity.status(insertSellerResponse.getHttpStatus()).body(result);

    }

}
