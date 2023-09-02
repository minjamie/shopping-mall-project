package com.example.shopping.dto.cart;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
public class CartResponse {
    private String status;
    private String message;
    private HttpStatus httpStatus;
}
