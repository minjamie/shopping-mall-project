package com.example.shopping.security;

import com.example.shopping.dto.auth.AuthInfoUserId;
import com.example.shopping.dto.auth.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider implements InitializingBean {

    private final UserDetailsService userDetailsService;

    private static final String AUTHORITIES_KEY = "roles";
    private static final String EMAIL_KEY = "email";
    private static final String USER_ID = "userId";
    public static final String TOKEN_PREFIX = "Bearer ";


    @Value("${jwt.secretKey}")
    private String secretKey;
    private static Key signingKey;

    @Value("${jwt.access-token-validity-in-seconds}")
    private Long accessTokenValidityInSeconds;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private Long refreshTokenValidityInSeconds;


    // 시크릿 키 설정
    // 지정된 키 바이트 배열을 기반으로 HMAC-SHA 알고리즘과 함께 사용할 새 SecretKey 인스턴스를 만듭니다.
    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] secretKeyBytes = Decoders.BASE64.decode(secretKey);
        signingKey = Keys.hmacShaKeyFor(secretKeyBytes);
    }

    public String resolveAccessToken(HttpServletRequest httpServletRequest) {
        String bearerToken =  httpServletRequest.getHeader("ACCESS-TOKEN");
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    // AT, RT 생성
    public TokenDto createToken(Integer userId, String email, List<String> authorities) {
        Date now = new Date();

        String accessToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS512")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidityInSeconds))
                .setSubject("access-token")
                .claim(EMAIL_KEY, email)
                .claim(USER_ID, userId)
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();

        String refreshToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS512")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidityInSeconds))
                .setSubject("refresh-token")
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();

        return new TokenDto(accessToken, refreshToken);
    }

    // 토큰 정보 추출
    public Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) { // AT
            return e.getClaims();
        }
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserEmail(String token) {
        return getClaims(token).get(EMAIL_KEY).toString();
    }

    public AuthInfoUserId getUserId(String token) {
        return AuthInfoUserId.of((Integer) getClaims(token).get(USER_ID));
    }

    public long getTokenExpirationTime(String token) {
        return getClaims(token).getExpiration().getTime();
    }

    // refresh-token 검증
    public boolean validateRefreshToken(String refreshToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(refreshToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.");
        } catch (NullPointerException e){
            log.error("JWT Token is empty.");
        }
        return false;
    }


    // filter에서 사용
    // access-token 검증
    public boolean validateAccessToken(String accessToken) {
        try {
            Date now = new Date();
            return getClaims(accessToken).getExpiration().after(now);
        } catch (Exception e) {
            return false;
        }
    }

    // 재발급 검증 API에서 사용
    public boolean validateAccessTokenOnlyExpired(String accessToken) {
        try {
            Date now = new Date();
            return getClaims(accessToken)
                    .getExpiration()
                    .before(now);
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
