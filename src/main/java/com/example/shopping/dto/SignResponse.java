package com.example.shopping.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignResponse {
    private String status;
    private String message;
    private List<String> data;
}
