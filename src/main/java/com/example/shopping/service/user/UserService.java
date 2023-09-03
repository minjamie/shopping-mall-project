package com.example.shopping.service.user;

import com.example.shopping.dto.cart.CartResponse;
import com.example.shopping.dto.common.CommonResponse;
import com.example.shopping.dto.common.Pagination;
import com.example.shopping.dto.user.CartListResponse;
import com.example.shopping.dto.user.OrderListResponse;
import com.example.shopping.dto.user.OrderResponse;
import com.example.shopping.repository.cart.CartRepository;
import com.example.shopping.repository.order.OrderRepository;
import com.example.shopping.service.error.ErrorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final ErrorService errorService;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;


    @Transactional(readOnly = true)
    public CommonResponse getOrderList(Integer userId, Pageable pageable) {
       Page<OrderResponse> data = orderRepository.findOrderList(1, pageable);
        Pagination pageInfo = new Pagination(data.getTotalPages(), data.getTotalElements(), data.getNumber() + 1,data.isLast());
        OrderListResponse result = new OrderListResponse(data.getContent(), pageInfo);
        return errorService.createSuccessResponse("주문 목록 조회 완료했습니다.", HttpStatus.OK, result);
    }

    @Transactional(readOnly = true)
    public CommonResponse getCartList(Integer userId, Pageable pageable) {
        Page<CartResponse> data = cartRepository.findCartList(1, pageable);
        Pagination pageInfo = new Pagination(data.getTotalPages(), data.getTotalElements(), data.getNumber() + 1,data.isLast());
        CartListResponse result = new CartListResponse(data.getContent(), pageInfo);
        return errorService.createSuccessResponse("장바구니 목록 조회 완료했습니다.", HttpStatus.OK, result);
    }
}
