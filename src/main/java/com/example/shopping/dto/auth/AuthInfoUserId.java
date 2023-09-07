package com.example.shopping.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class AuthInfoUserId {
    @Override
    public String toString() {
        return "AuthInfoUserId{" +
                "userId=" + userId +
                '}';
    }

    private Integer userId;
}
