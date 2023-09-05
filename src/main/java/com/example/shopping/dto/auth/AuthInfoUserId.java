package com.example.shopping.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class AuthInfoUserId {
    private Integer userId;
}
