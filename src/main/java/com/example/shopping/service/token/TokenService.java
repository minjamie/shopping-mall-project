package com.example.shopping.service.token;


import com.example.shopping.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;

    private static final String TOKEN_PREFIX = "Bearer ";


    // "Bearer {AT}" 에서 {AT} 추출
    // ACCESS-TOKEN에서 user emaill 값 가져오기
    public String resolveTokenEmail(String accessTokenInHeader) {
        if (accessTokenInHeader != null && accessTokenInHeader.startsWith(TOKEN_PREFIX)) {
            String token = accessTokenInHeader.substring(TOKEN_PREFIX.length());
            return jwtTokenProvider.getUserEmail(token);
        }
        return null;
    }


    public String resolveToken(String accessTokenInHeader) {
        if (accessTokenInHeader != null && accessTokenInHeader.startsWith(TOKEN_PREFIX)) {
            return accessTokenInHeader.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
