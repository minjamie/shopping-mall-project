package com.example.shopping.service;

import com.example.shopping.domain.Address;
import com.example.shopping.domain.Role;
import com.example.shopping.domain.User;
import com.example.shopping.dto.SignResponse;
import com.example.shopping.dto.SignupRequest;
import com.example.shopping.exception.NotFoundException;
import com.example.shopping.repository.AddressRepository;
import com.example.shopping.repository.RoleRepository;
import com.example.shopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignResponse signup(SignupRequest signupRequest) {
        String email = signupRequest.getEmail();
        String password = signupRequest.getPassword();
        String phoneNumber = signupRequest.getPhoneNumber();

        // 이메일 중복 오류 (exception으로 변경 해야함)
        if (userRepository.existsByEmail(email)) {
            return SignResponse.builder().status("fail")
                    .message("이메일 중복입니다.")
                    .build();
        }


        // 비밀번호 길이 오류 (exception으로 변경 해야함)
        if (password.length() <= 8 || password.length() >= 20) {
            return  SignResponse.builder().status("fail")
                    .message("비밀번호가 8자 이하 20자 이상 입니다.")
                    .build();
        }

        // 핸드폰 번호 길이 오류 (exception으로 변경 해야함)
        if (phoneNumber.length() != 11) {
            return SignResponse.builder().status("fail")
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

        return SignResponse.builder()
                .status("success")
                .message("회원가입에 성공했습니다.").build();
    }
}
