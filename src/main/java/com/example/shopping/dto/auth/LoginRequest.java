package com.example.shopping.dto.auth;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class LoginRequest {

    @ApiModelProperty(name = "email", value = "login email", example = "abcd@gmail.com")
    private String email;
    @ApiModelProperty(name = "password", value = "login password", example = "abcd1234")
    private String password;
}
