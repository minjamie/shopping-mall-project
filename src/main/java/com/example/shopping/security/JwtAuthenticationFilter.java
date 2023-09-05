package com.example.shopping.security;

import io.jsonwebtoken.IncorrectClaimException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 토큰 추출
        String accessToken = jwtTokenProvider.resolveAccessToken(request);

        // 정상 토큰 검사
        try {
            if (accessToken != null && jwtTokenProvider.validateAccessToken(accessToken)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (IncorrectClaimException e) { // 잘못된 토큰
            SecurityContextHolder.clearContext();
            response.sendError(403, "토큰 정보가 잘못되었습니다.");
        } catch (UsernameNotFoundException e) { // 회원이 없을 경우
            SecurityContextHolder.clearContext();
            response.sendError(404, "해당 유저가 없습니다.");
        }

        filterChain.doFilter(request, response);
    }
}
