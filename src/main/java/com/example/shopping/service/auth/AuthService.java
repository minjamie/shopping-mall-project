package com.example.shopping.service.auth;

import com.example.shopping.domain.*;
import com.example.shopping.domain.Enum.RoleType;
import com.example.shopping.dto.auth.LoginRequest;
import com.example.shopping.dto.auth.SignupRequest;
import com.example.shopping.dto.auth.TokenDto;
import com.example.shopping.dto.common.CommonResponse;
import com.example.shopping.repository.address.AddressRepository;
import com.example.shopping.repository.login.LoginRepository;
import com.example.shopping.repository.role.RoleRepository;
import com.example.shopping.repository.user.UserRepository;
import com.example.shopping.security.JwtTokenProvider;
import com.example.shopping.service.error.ErrorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
    private final ErrorService errorService;

    private static final String TOKEN_PREFIX = "Bearer ";


    // 회원가입
    @Transactional
    public CommonResponse signup(SignupRequest signupRequest) {
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

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return errorService.createErrorResponse("회원 가입에 실패했습니다.", HttpStatus.NOT_FOUND, null);
        }

        User userFound = userOptional.get();


        addressRepository.save(
                Address.builder()
                        .user(userFound)
                        .main(signupRequest.getMainAddress())
                        .detail(signupRequest.getDetailAddress())
                        .isOrder(false)
                        .isDefault(true)
                        .build());

        roleRepository.save(
                Role.builder()
                        .user(userFound)
                        .name(RoleType.ROLE_USER.name())
                        .build());

        return errorService.createSuccessResponse("회원가입에 성공했습니다.", HttpStatus.CREATED, null);
    }


    // 회원 가입 이메일 중복 확인
    public CommonResponse emailExists(String email) {
        boolean isExists = userRepository.existsByEmail(email);
        if (isExists) return errorService.createErrorResponse("중복된 이메일 입니다.", HttpStatus.BAD_REQUEST, null);
        else return errorService.createSuccessResponse("사용 가능한 이메일 입니다.", HttpStatus.OK, null);
    }


    // 로그인
    @Transactional
    public CommonResponse login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));

            SecurityContextHolder.getContext().setAuthentication(authentication);


            Optional<User> userOptional = userRepository.findByEmail(email);

            if (userOptional.isEmpty()) {
                return errorService.createErrorResponse("해당 유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, null);
            }

            User user = userOptional.get();

            Integer userId = user.getId();

            Login loginFound = loginRepository.findByUserId(userId);

            List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
            TokenDto tokenDto = jwtTokenProvider.createToken(userId, email, roles);

            String refreshToken = tokenDto.getRefreshToken();

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


            return errorService.createSuccessResponse("로그인에 성공했습니다.", HttpStatus.CREATED, tokenDto);

        } catch (Exception e) {
            e.printStackTrace();
            return errorService.createErrorResponse("로그인 할 수 없습니다.", HttpStatus.NOT_ACCEPTABLE, null);
        }
    }


    // 로그아웃
    @Transactional
    public CommonResponse logout(String requestAccessToken) {
        String email = resolveTokenEmail(requestAccessToken);

        Optional<Login> loginOptional = loginRepository.findByUserEmail(email);

        if (loginOptional.isEmpty()) return errorService.createErrorResponse("로그아웃에 실패했습니다.", HttpStatus.NOT_FOUND, null);

        Login login = loginOptional.get();

        login.setRefreshToken(null);

        return errorService.createSuccessResponse("로그아웃에 성공했습니다.", HttpStatus.OK, null);
    }

    // AT가 만료 검증
    public CommonResponse validate(String requestAccessToken) {
        String accessToken = resolveToken(requestAccessToken);
        boolean isValidate = jwtTokenProvider.validateAccessTokenOnlyExpired(accessToken);

        if (isValidate) return errorService.createErrorResponse("토큰 재발급이 필요하지 않습니다.", HttpStatus.BAD_REQUEST, null);
        else return errorService.createSuccessResponse("토큰 재발급이 필요합니다.", HttpStatus.OK, null);


    }


    // 토큰 재발급: validate method가 ture 반환 때만 사용 -> AT, RT 재발급
    @Transactional
    public CommonResponse reissue(String requestAccessToken) {
        String email = resolveTokenEmail(requestAccessToken);
        String accessToken = resolveToken(requestAccessToken);

        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return errorService.createErrorResponse("해당 유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, null);
        }

        User user = userOptional.get();

        Integer userId = user.getId();

        Login login = loginRepository.findByUserId(userId);

        String foundRefreshToken = login.getRefreshToken();

        if (foundRefreshToken == null) { // 토큰이 없을 때
            return errorService.createErrorResponse("토큰이 없습니다. 재로그인 해주세요.", HttpStatus.NOT_FOUND, null); // 재로그인 요청
        }

        if (!jwtTokenProvider.validateRefreshToken(foundRefreshToken)) { // 토큰이 유효하지 않을 때
            return errorService.createErrorResponse("토큰이 유효하지 않습니다. 재로그인 해주세요.", HttpStatus.UNAUTHORIZED, null); // 재로그인 요청
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());

        TokenDto tokenDto = jwtTokenProvider.createToken(userId, email, roles);

        String refreshToken = tokenDto.getRefreshToken();
        login.setRefreshToken(refreshToken);
        return errorService.createSuccessResponse("토큰 재발급에 성공했습니다.", HttpStatus.CREATED, tokenDto);
    }

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
