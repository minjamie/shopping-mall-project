package com.example.shopping.dto.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
public class CommonResponse<Data> {
    private String status;
    private String message;
    private HttpStatus httpStatus;
    private Data data;
}
