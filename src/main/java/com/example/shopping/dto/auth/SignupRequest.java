package com.example.shopping.dto.auth;

import lombok.Getter;

@Getter
public class SignupRequest {
    String name;
    String email;
    String password;
    String phoneNumber;
    String sex;
    String profileImgUrl;
    String introduce;
    String mainAddress;
    String detailAddress;
}
