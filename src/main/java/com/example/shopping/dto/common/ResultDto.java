package com.example.shopping.dto.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor(staticName = "in")
public class ResultDto<Data> {
    private final String status;
    private final String message;
    private Data data;
}
