package com.example.shopping.service.error;

import com.example.shopping.dto.common.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ErrorService {
    public CommonResponse createSuccessResponse(String message, HttpStatus httpStatus, Object data) {
        return CommonResponse.builder()
                .message(message)
                .status("success")
                .httpStatus(httpStatus)
                .data(data)
                .build();
    }
    public CommonResponse createErrorResponse(String message, HttpStatus httpStatus, Object data) {
        return CommonResponse.builder()
                .message(message)
                .status("fail")
                .httpStatus(httpStatus)
                .data(data)
                .build();
    }
}
