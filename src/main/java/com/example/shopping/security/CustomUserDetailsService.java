package com.example.shopping.security;

import com.example.shopping.domain.Address;
import com.example.shopping.domain.Role;
import com.example.shopping.domain.User;
import com.example.shopping.exception.NotFoundException;
import com.example.shopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Primary
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("해당하는 email이 없습니다."));

        CustomUserDetails customUserDetails = CustomUserDetails.builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .sex(user.getSex())
                .profileImgUrl(user.getProfileImgUrl())
                .introduce(user.getIntroduce())
                .addressIds(user.getAddressIds().stream()
                        .map(Address::getId).collect(Collectors.toList()))
                .authorities(user.getRoles()
                        .stream()
                        .map(Role::getName)
                        .collect(Collectors.toList()))
                .isAuth(user.isAuth())
                .isWithdrawal(user.isWithdrawal())
                .build();
        return customUserDetails;
    }
}
