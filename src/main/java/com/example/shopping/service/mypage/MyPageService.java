package com.example.shopping.service.mypage;

import com.example.shopping.domain.Address;
import com.example.shopping.domain.User;
import com.example.shopping.dto.common.CommonResponse;
import com.example.shopping.dto.mypage.MyPageResponse;
import com.example.shopping.repository.address.AddressRepository;
import com.example.shopping.repository.login.LoginRepository;
import com.example.shopping.repository.role.RoleRepository;
import com.example.shopping.repository.user.UserRepository;
import com.example.shopping.service.error.ErrorService;
import com.example.shopping.service.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final LoginRepository loginRepository;
    private final RoleRepository roleRepository;
    private final ErrorService errorService;




    // 내 정보 조회
    public CommonResponse myInformation(String requestAccessToken, Integer userId) {
        String email = tokenService.resolveTokenEmail(requestAccessToken);
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return errorService.createErrorResponse("해당 유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, null);
        }

        User user = userOptional.get();

        if (user.isWithdrawal()) {
            return errorService.createErrorResponse("회원 탈퇴를 한 유저 입니다.", HttpStatus.BAD_REQUEST, null);
        }

        Integer requestUserId = user.getId();

        if (!requestUserId.equals(userId)) {
            return errorService.createErrorResponse("유저의 정보가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED, null);
        }


        List<Address> addressList = addressRepository.findAllByUserId(requestUserId);

        if (addressList.isEmpty()) {
            return errorService.createErrorResponse("해당 유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, null);
        }

        MyPageResponse myPageResponse = MyPageResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .sex(user.getSex())
                .profileImgUrl(user.getProfileImgUrl())
                .introduce(user.getIntroduce())
                .mainAddress(addressList.stream().map(Address::getMain).collect(Collectors.toList()))
                .detailAddress(addressList.stream().map(Address::getDetail).collect(Collectors.toList()))
                .build();

        return errorService.createSuccessResponse("내 정보 조회에 성공했습니다.", HttpStatus.OK, myPageResponse);

    }

    
    // 회원 탈퇴
    @Transactional
    public CommonResponse withdrawal(String requestAccessToken, Integer userId) {
        String email = tokenService.resolveTokenEmail(requestAccessToken);

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return errorService.createErrorResponse("해당 유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, null);
        }

        User user = userOptional.get();

        Integer requestUserId = user.getId();

        if (!requestUserId.equals(userId)) {
            return errorService.createErrorResponse("유저의 정보가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED, null);
        }

        user.setWithdrawal(true);

        return errorService.createSuccessResponse("회원탈퇴에 성공했습니다.", HttpStatus.OK, null);
    }
}
