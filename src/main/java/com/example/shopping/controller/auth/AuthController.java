package com.example.shopping.controller.auth;

import com.example.shopping.dto.auth.LoginRequest;
import com.example.shopping.dto.auth.SignupRequest;
import com.example.shopping.dto.auth.TokenDto;
import com.example.shopping.dto.common.CommonResponse;
import com.example.shopping.dto.common.ResultDto;
import com.example.shopping.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class AuthController {

    private final AuthService authService;
    public static final String TOKEN_PREFIX = "Bearer ";

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/sign")
    public ResponseEntity<ResultDto<Void>>  signup(@RequestBody SignupRequest signupRequest) {
        CommonResponse signCommonResponse = authService.signup(signupRequest);
        ResultDto<Void> result = ResultDto.in(signCommonResponse.getStatus(), signCommonResponse.getMessage());
        return ResponseEntity.status(signCommonResponse.getHttpStatus()).body(result);
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/login")
    public ResponseEntity<ResultDto<Void>> login(@RequestBody LoginRequest loginRequest,
                                                     HttpServletResponse httpServletResponse) {
        CommonResponse loginCommonResponse = authService.login(loginRequest);

        ResultDto<Void> result = ResultDto.in(loginCommonResponse.getStatus(), loginCommonResponse.getMessage());
        TokenDto tokenDto = (TokenDto) loginCommonResponse.getData();

        if (tokenDto != null) {
            httpServletResponse.setHeader("ACCESS-TOKEN", TOKEN_PREFIX + tokenDto.getAccessToken());
        }
        return ResponseEntity.status(loginCommonResponse.getHttpStatus()).body(result);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/sign/{email}/exists")
    public ResponseEntity<ResultDto<Void>> emailExists(@PathVariable String email) {
        CommonResponse existsCommonResponse = authService.emailExists(email);
        ResultDto<Void> result = ResultDto.in(existsCommonResponse.getStatus(), existsCommonResponse.getMessage());
        return ResponseEntity.status(existsCommonResponse.getHttpStatus()).body(result);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/logout")
    public ResponseEntity<ResultDto<Void>> logout(@RequestHeader("ACCESS-TOKEN") String requestAccessToken) {
        CommonResponse logoutCommonResponse = authService.logout(requestAccessToken);
        ResultDto<Void> result = ResultDto.in(logoutCommonResponse.getStatus(), logoutCommonResponse.getMessage());

        return ResponseEntity.status(logoutCommonResponse.getHttpStatus()).body(result);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/validate")
    public ResponseEntity<ResultDto<Void>> validate(@RequestHeader("ACCESS-TOKEN") String requestAccessToken) {

        CommonResponse validateCommonResponse = authService.validate(requestAccessToken);
        ResultDto<Void> result = ResultDto.in(validateCommonResponse.getStatus(), validateCommonResponse.getMessage());
        return ResponseEntity.status(validateCommonResponse.getHttpStatus()).body(result);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/reissue")
    public ResponseEntity<ResultDto<Void>> reissue(@RequestHeader("ACCESS-TOKEN") String requestAccessToken,
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
