package com.example.shopping.controller;

import com.example.shopping.dto.LoginRequest;
import com.example.shopping.dto.SignupRequest;
import com.example.shopping.dto.GlobalResponse;
import com.example.shopping.dto.Token;
import com.example.shopping.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            response.put("status", "success");
            response.put("message", "로그인에 성공했습니다.");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "fail");
            response.put("message", "로그인에 실패했습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }


    @GetMapping("/sign/{email}/exists")
    public ResponseEntity<Map<String, String>> emailExists(@PathVariable String email) {
        Map<String, String> response = new HashMap<>();
        boolean isExists = authService.emailExists(email);

        if (isExists) {
            response.put("status", "fail");
            response.put("message", "이메일 중복 입니다.");
        } else {
            response.put("status", "success");
            response.put("message", "사용 가능한 이메일 입니다.");
        }
        return ResponseEntity.ok(response);
    }


    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestHeader("ACCESS-TOKEN") String requestAccessToken) {

           Map<String, String> response = new HashMap<>();
           authService.logout(requestAccessToken);

           response.put("status", "success");
           response.put("message", "로그아웃에 성공했습니다.");
           return ResponseEntity.ok(response);
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String, String>> validate(@RequestHeader("ACCESS-TOKEN") String requestAccessToken) {

        Map<String, String> response = new HashMap<>();
        boolean isValidate = authService.validate(requestAccessToken);

        if (isValidate) {
            response.put("status", "success"); // 재발급 필요
            response.put("message", "access-token이 유효합니다.");
        } else {
            response.put("status", "fail"); // 재발급 불필요
            response.put("message", "access-token이 만료되었습니다..");
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reissue")
    public ResponseEntity<Map<String, String>> reissue(@RequestHeader("ACCESS-TOKEN") String requestAccessToken,
                                                       HttpServletResponse httpServletResponse) {
        Map<String, String> response = new HashMap<>();
        Token reissuedToken = authService.reissue(requestAccessToken);

        if (reissuedToken != null) {
            httpServletResponse.setHeader("ACCESS-TOKEN", TOKEN_PREFIX + reissuedToken.getAccessToken());
            response.put("status", "success");
            response.put("message", "재발급에 성공했습니다.");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "fail");
            response.put("message", "재발급에 실패했습니다. 재로그인 해주세요.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

    }
}
