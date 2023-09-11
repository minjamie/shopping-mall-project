package com.example.shopping.service.auth;

import com.example.shopping.domain.Address;
import com.example.shopping.domain.Enum.RoleType;
import com.example.shopping.domain.Login;
import com.example.shopping.domain.Role;
import com.example.shopping.domain.User;
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
import com.example.shopping.service.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
    private final TokenService tokenService;




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

            if (user.isAuth()) {
                return errorService.createErrorResponse("비밀번호 5회 이상 틀려 계정 잠금 상태 입니다.", HttpStatus.LOCKED, null);
            }

            if (user.isWithdrawal()) {
                return errorService.createErrorResponse("회원 탈퇴를 한 유저 입니다.", HttpStatus.BAD_REQUEST, null);
            }

            Integer userId = user.getId();

            List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
            TokenDto tokenDto = jwtTokenProvider.createToken(userId, email, roles);

            String refreshToken = tokenDto.getRefreshToken();

            if (!passwordEncoder.matches(password, user.getPassword())) {
                return errorService.createErrorResponse("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST, null);
            }

            Optional<Login> loginOptional = loginRepository.findByUserId(user.getId());

            if (loginOptional.isEmpty()) {
                loginRepository.save(
                        Login.builder()
                                .user(user)
                                .refreshToken(refreshToken)
                                .count(0)
                                .build());
            } else {
                Login loginFound = loginOptional.get();
                loginFound.setRefreshToken(refreshToken);
            }

            return errorService.createSuccessResponse("로그인에 성공했습니다.", HttpStatus.CREATED, tokenDto);


        } catch (Exception e) {
            e.printStackTrace();
            return errorService.createErrorResponse("로그인 할 수 없습니다.", HttpStatus.NOT_ACCEPTABLE, null);
        }
    }

    // 계정 잠금 해제
    @Transactional
    public CommonResponse unlock(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return errorService.createErrorResponse("해당 유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, null);
        }

        User user = userOptional.get();

        if (user.isWithdrawal()) {
            return errorService.createErrorResponse("회원 탈퇴를 한 유저 입니다.", HttpStatus.BAD_REQUEST, null);
        }

        user.setAuth(false);
        return errorService.createSuccessResponse("계정 잠금을 해제 했습니다.", HttpStatus.OK, null);
    }


    // 로그아웃
    @Transactional
    public CommonResponse logout(String requestAccessToken) {
        String email = tokenService.resolveTokenEmail(requestAccessToken);

        Optional<Login> loginOptional = loginRepository.findByUserEmail(email);

        if (loginOptional.isEmpty()) return errorService.createErrorResponse("로그아웃에 실패했습니다.", HttpStatus.NOT_FOUND, null);

        Login login = loginOptional.get();

        login.setRefreshToken(null);

        return errorService.createSuccessResponse("로그아웃에 성공했습니다.", HttpStatus.OK, null);
    }

    // AT가 만료 검증
    public CommonResponse validate(String requestAccessToken) {
        String accessToken = tokenService.resolveToken(requestAccessToken);
        boolean isValidate = jwtTokenProvider.validateAccessTokenOnlyExpired(accessToken);

        if (isValidate) return errorService.createErrorResponse("토큰 재발급이 필요하지 않습니다.", HttpStatus.BAD_REQUEST, null);
        else return errorService.createSuccessResponse("토큰 재발급이 필요합니다.", HttpStatus.OK, null);


    }


    // 토큰 재발급: AT, RT 재발급
    @Transactional
    public CommonResponse reissue(String requestAccessToken) {
        String email = tokenService.resolveTokenEmail(requestAccessToken);
        String accessToken = tokenService.resolveToken(requestAccessToken);

        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return errorService.createErrorResponse("해당 유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, null);
        }

        User user = userOptional.get();


        Integer userId = user.getId();

        Optional<Login> loginOptional = loginRepository.findByUserId(userId);
        Login login = loginOptional.get();

        String foundRefreshToken = login.getRefreshToken();

        if (foundRefreshToken == null) { // 토큰이 없을 때
            return errorService.createErrorResponse("토큰이 없습니다. 재로그인 해주세요.", HttpStatus.BAD_REQUEST, null); // 재로그인 요청
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
}