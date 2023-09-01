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
import com.example.shopping.repository.UserRepository;
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


    // 회원가입
    @Transactional
    public GlobalResponse signup(SignupRequest signupRequest) {
        String email = signupRequest.getEmail();
        String password = signupRequest.getPassword();
        String phoneNumber = signupRequest.getPhoneNumber();

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


    // 회원 가입 이메일 중복 확인
    public boolean check(String email) {
        return userRepository.existsByEmail(email);
    }


    // 로그인
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

            Integer userId = user.getId();

            Login loginFound = loginRepository.findByUserId(userId);

            List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
            Token token = jwtTokenProvider.createToken(email, roles);

            String refreshToken = token.getRefreshToken();

            if (loginFound == null) {
                loginRepository.save(
                        Login.builder()
                                .user(user)
                                .refreshToken(refreshToken)
                                .count(0)
                                .build());
            } else {
                loginFound.setRefreshToken(refreshToken);
            }

            // 추후 로그인 실패 카운트 증가 코드 작성

            return token;

        } catch (Exception e) {
            e.printStackTrace();
            throw new NotAcceptException("로그인 할 수 없습니다.");
        }
    }


    // 로그아웃
    @Transactional
    public void logout(String requestAccessToken) {
        String email = resolveToken(requestAccessToken);

        Login login = loginRepository.findByUserEmail(email)
                .orElseThrow(() -> new NotFoundException("해당 유저를 찾을 수 없습니다."));

        login.setRefreshToken(null);

    }

    // TODO : 리프레쉬 토큰으로 재발급
    // TODO : 토큰 재발급

    // "Bearer {AT}" 에서 {AT} 추출
    // ACCESS-TOKEN에서 user emaill 값 가져오기
    public String resolveToken(String accessTokenInHeader) {
        if (accessTokenInHeader != null && accessTokenInHeader.startsWith(TOKEN_PREFIX)) {
            String token = accessTokenInHeader.substring(TOKEN_PREFIX.length());
            return jwtTokenProvider.getUserEmail(token);
        }
        return null;
    }


}
