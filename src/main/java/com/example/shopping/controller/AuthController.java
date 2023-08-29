package com.example.shopping.controller;

import com.example.shopping.dto.LoginRequest;
import com.example.shopping.dto.SignupRequest;
import com.example.shopping.dto.GlobalResponse;
import com.example.shopping.dto.Token;
import com.example.shopping.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class AuthController {
    // TODO : Controller 작성 후 Valid 생성
    // TODO : 예외처리 및 Response 형식 맞추기


    private final AuthService authService;
    public static final String TOKEN_PREFIX = "Bearer ";

    @PostMapping("/sign")
    public GlobalResponse signup(@RequestBody SignupRequest signupRequest) {
        return authService.signup(signupRequest);
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest,
                                                     HttpServletResponse httpServletResponse) {
        Map<String, String> response = new HashMap<>();
        Token token = authService.login(loginRequest);

        if (token != null) {
            httpServletResponse.setHeader("ACCESS-TOKEN", TOKEN_PREFIX + token.getAccessToken());
            httpServletResponse.setHeader("REFRESH-TOKEN", TOKEN_PREFIX + token.getRefreshToken());
            response.put("status", "success");
            response.put("message", "로그인에 성공했습니다.");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "fail");
            response.put("message", "로그인에 실패했습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
