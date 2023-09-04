package com.example.shopping.dto.user;

import com.example.shopping.dto.common.Pagination;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class OrderListResponse {
    private List<OrderResponse> orderList;
    private Pagination pagination;
}
