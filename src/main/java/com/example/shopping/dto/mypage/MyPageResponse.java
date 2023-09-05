package com.example.shopping.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class MyPageResponse {
    private String name;
    private String email;
    private String phoneNumber;
    private String sex;
    private String profileImgUrl;
    private String introduce;
    private String mainAddress;
    private String detailAddress;
}
