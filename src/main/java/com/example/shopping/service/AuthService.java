package com.example.shopping.service;

import com.example.shopping.domain.Address;
import com.example.shopping.domain.Login;
import com.example.shopping.domain.Role;
import com.example.shopping.domain.User;
import com.example.shopping.dto.GlobalResponse;
import com.example.shopping.dto.LoginRequest;
import com.example.shopping.dto.SignupRequest;
import com.example.shopping.dto.Token;
import com.example.shopping.exception.NotAcceptException;
import com.example.shopping.exception.NotFoundException;
import com.example.shopping.repository.AddressRepository;
import com.example.shopping.repository.LoginRepository;
import com.example.shopping.repository.RoleRepository;
import com.example.shopping.repository.user.UserRepository;
import com.example.shopping.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final RoleRepository roleRepository;
    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    private static final String TOKEN_PREFIX = "Bearer ";

    @Transactional
    public GlobalResponse signup(SignupRequest signupRequest) {
        String email = signupRequest.getEmail();
        String password = signupRequest.getPassword();
        String phoneNumber = signupRequest.getPhoneNumber();

        // 이메일 중복 오류 (exception으로 변경 해야함)
        if (userRepository.existsByEmail(email)) {
            return GlobalResponse.builder().status("fail")
                    .message("이메일 중복입니다.")
                    .build();
        }


        // 비밀번호 길이 오류 (exception으로 변경 해야함)
        if (password.length() <= 8 || password.length() >= 20) {
            return  GlobalResponse.builder().status("fail")
                    .message("비밀번호가 8자 이하 20자 이상 입니다.")
                    .build();
        }

        // 핸드폰 번호 길이 오류 (exception으로 변경 해야함)
        if (phoneNumber.length() != 11) {
            return GlobalResponse.builder().status("fail")
                    .message("휴대폰 번호를 제대로 입력해 주세요.")
                    .build();
        }

        User user = User.builder()
                .name(signupRequest.getName())
                .email(email)
                .password(passwordEncoder.encode(password))
                .phoneNumber(phoneNumber)
                .sex(signupRequest.getSex())
                .profileImgUrl(signupRequest.getProfileImgUrl())
                .introduce(signupRequest.getIntroduce())
                .isAuth(false)
                .isWithdrawal(false)
                .build();

        userRepository.save(user);

        User userFound = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("email에 해당하는 유저가 없습니다."));

        addressRepository.save(
                Address.builder()
                        .user(userFound)
                        .main(signupRequest.getMainAddress())
                        .detail(signupRequest.getDetailAddress())
                        .isOrder(false)
                        .isDefault(true)
                        .build());

        // 역할 enum 교체 해야함
        roleRepository.save(
                Role.builder()
                        .user(userFound)
                        .name("ROLE_USER")
                        .build());

        return GlobalResponse.builder()
                .status("success")
                .message("회원가입에 성공했습니다.").build();
    }

    @Transactional
    public Token login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new NotFoundException("해당 유저를 찾을 수 없습니다."));

            List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());

            Token token = jwtTokenProvider.createToken(email, roles);

            loginRepository.save(
                    Login.builder()
                            .user(user)
                            .refreshToken(token.getRefreshToken())
                            .count(0)
                            .build());

            // 추후 로그인 실패 카운트 증가 코드 작성
            return token;

        } catch (Exception e) {
            e.printStackTrace();
            throw new NotAcceptException("로그인 할 수 없습니다.");
        }
    }

    @Transactional
    public void logout(String requestRefreshToken) {
        String refreshToken = resolveToken(requestRefreshToken);

        Login login = loginRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new NotFoundException("해당 토큰을 찾을 수 없습니다."));

        login.setRefreshToken(null);
    }

    // "Bearer {AT}" 에서 {AT} 추출
    public String resolveToken(String accessTokenInHeader) {
        if (accessTokenInHeader != null && accessTokenInHeader.startsWith(TOKEN_PREFIX)) {
            return accessTokenInHeader.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
