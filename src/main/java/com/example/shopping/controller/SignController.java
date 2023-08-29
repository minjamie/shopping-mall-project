package com.example.shopping.controller;

import com.example.shopping.dto.SignupRequest;
import com.example.shopping.dto.SignResponse;
import com.example.shopping.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class SignController {

    private final AuthService authService;

    @PostMapping("/sign")
    public SignResponse signup(@RequestBody SignupRequest signupRequest) {
        return authService.signup(signupRequest);
    }
}
