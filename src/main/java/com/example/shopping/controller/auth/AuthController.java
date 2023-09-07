package com.example.shopping.controller.auth;

import com.example.shopping.dto.auth.LoginRequest;
import com.example.shopping.dto.auth.SignupRequest;
import com.example.shopping.dto.auth.TokenDto;
import com.example.shopping.dto.common.CommonResponse;
import com.example.shopping.dto.common.ResultDto;
import com.example.shopping.service.auth.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Api(tags = "Auth APIs")
public class AuthController {

    private final AuthService authService;
    public static final String TOKEN_PREFIX = "Bearer ";


    @ApiOperation(value = "회원가입 API", notes = "유저 회원 가입 진행")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/sign")
    public ResponseEntity<ResultDto<Void>>  signup(@RequestBody SignupRequest signupRequest) {
        CommonResponse signCommonResponse = authService.signup(signupRequest);
        ResultDto<Void> result = ResultDto.in(signCommonResponse.getStatus(), signCommonResponse.getMessage());
        return ResponseEntity.status(signCommonResponse.getHttpStatus()).body(result);
    }


    @ApiOperation(value = "로그인 API", notes = "유저 로그인 진행")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/login")
    public ResponseEntity<ResultDto<Map>> login(@RequestBody LoginRequest loginRequest,
                                                HttpServletResponse httpServletResponse) {
        CommonResponse loginCommonResponse = authService.login(loginRequest);

        ResultDto<Map> result = ResultDto.in(
                loginCommonResponse.getStatus(),
                loginCommonResponse.getMessage());
        TokenDto tokenDto = (TokenDto) loginCommonResponse.getData();

        Map<String, Integer> userId = new HashMap<>();

        if (tokenDto != null) {
            userId.put("userId", tokenDto.getUserId());
            result.setData(userId);
            httpServletResponse.setHeader("ACCESS-TOKEN", TOKEN_PREFIX + tokenDto.getAccessToken());
        }
        return ResponseEntity.status(loginCommonResponse.getHttpStatus()).body(result);
    }

    @ApiOperation(value = "이메일 중복 확인 API", notes = "유저의 이메일 중복을 확인")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/sign/{email}/exists")
    public ResponseEntity<ResultDto<Void>> emailExists(@ApiParam(name = "email", value = "유저 이메일", example = "abcd@gmail.com") @PathVariable String email) {
        CommonResponse existsCommonResponse = authService.emailExists(email);
        ResultDto<Void> result = ResultDto.in(existsCommonResponse.getStatus(), existsCommonResponse.getMessage());
        return ResponseEntity.status(existsCommonResponse.getHttpStatus()).body(result);
    }

    @ApiOperation(value = "로그아웃 API", notes = "유저 로그아웃 진행")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/logout")
    public ResponseEntity<ResultDto<Void>> logout(@ApiIgnore @RequestHeader("ACCESS-TOKEN") String requestAccessToken) {
        CommonResponse logoutCommonResponse = authService.logout(requestAccessToken);
        ResultDto<Void> result = ResultDto.in(logoutCommonResponse.getStatus(), logoutCommonResponse.getMessage());

        return ResponseEntity.status(logoutCommonResponse.getHttpStatus()).body(result);
    }

    @ApiOperation(value = "토큰 검증 API", notes = "토큰 만료 시간 확인")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/validate")
    public ResponseEntity<ResultDto<Void>> validate(@ApiIgnore @RequestHeader("ACCESS-TOKEN") String requestAccessToken) {

        CommonResponse validateCommonResponse = authService.validate(requestAccessToken);
        ResultDto<Void> result = ResultDto.in(validateCommonResponse.getStatus(), validateCommonResponse.getMessage());
        return ResponseEntity.status(validateCommonResponse.getHttpStatus()).body(result);
    }


    @ApiOperation(value = "토큰 재발급 API", notes = "토큰 재발급 진행")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/reissue")
    public ResponseEntity<ResultDto<Void>> reissue(@ApiIgnore @RequestHeader("ACCESS-TOKEN") String requestAccessToken,
                                                       HttpServletResponse httpServletResponse) {
        CommonResponse reissueCommonResponse = authService.reissue(requestAccessToken);

        ResultDto<Void> result = ResultDto.in(reissueCommonResponse.getStatus(), reissueCommonResponse.getMessage());

        TokenDto reissuedTokenDto = (TokenDto) reissueCommonResponse.getData();

        if (reissuedTokenDto != null) {
            httpServletResponse.setHeader("ACCESS-TOKEN", TOKEN_PREFIX + reissuedTokenDto.getAccessToken());
        }
        return ResponseEntity.status(reissueCommonResponse.getHttpStatus()).body(result);

    }
}
