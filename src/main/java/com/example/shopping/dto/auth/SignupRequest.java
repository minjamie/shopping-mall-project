package com.example.shopping.dto.auth;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class SignupRequest {

    @ApiModelProperty(name = "name", value = "user name", example = "홍길동")
    String name;
    @ApiModelProperty(name = "email", value = "user email", example = "abcd@gmail.com")
    String email;
    @ApiModelProperty(name = "password", value = "user password", example = "abcd1234")
    String password;
    @ApiModelProperty(name = "phoneNumber", value = "user phoneNumber", example = "01000000000")
    String phoneNumber;
    @ApiModelProperty(name = "sex", value = "user sex", example = "male")
    String sex;
    @ApiModelProperty(name = "profileImgUrl", value = "user profileImgUrl", example = "Img Url")
    String profileImgUrl;
    @ApiModelProperty(name = "introduce", value = "user introduce", example = "안녕하세요! 저는 홍길동 입니다.")
    String introduce;
    @ApiModelProperty(name = "mainAddress", value = "user mainAddress", example = "서울시 강남구")
    String mainAddress;
    @ApiModelProperty(name = "detailAddress", value = "user detailAddress", example = "도곡로 1길 10")
    String detailAddress;
}
