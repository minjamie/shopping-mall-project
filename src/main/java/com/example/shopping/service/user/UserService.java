package com.example.shopping.service.user;

import com.example.shopping.dto.cart.AddCartRequest;
import com.example.shopping.dto.common.CommonResponse;
import com.example.shopping.dto.user.OrderListResponse;
import com.example.shopping.repository.user.UserRepository;
import com.example.shopping.service.error.ErrorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final ErrorService errorService;
    private final UserRepository userRepository;

    public CommonResponse getOrderList(Integer userId) {
//       List<OrderListResponse> data = userRepository.findOrderList(userId);
        return errorService.createSuccessResponse("주문 목록 조회 완료했습니다.", HttpStatus.OK, "");
    }

    public CommonResponse getCartList(Integer userId) {
//        List<OrderListResponse> data = userRepository.findCartList(userId);
        return errorService.createSuccessResponse("장바구니 목록 조회 완료했습니다.", HttpStatus.OK, "");
    }
}
