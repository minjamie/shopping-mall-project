package com.example.shopping.controller.mypage;


import com.example.shopping.dto.common.CommonResponse;
import com.example.shopping.dto.common.ResultDto;
import com.example.shopping.service.mypage.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class MyPageController {

    private final MyPageService myPageService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}")
    public ResponseEntity<ResultDto<Void>> myInformation(@RequestHeader("ACCESS-TOKEN") String requestAccessToken,
                                                         @PathVariable Integer userId) {
        CommonResponse myInformationCommonResponse = myPageService.myInformation(requestAccessToken, userId);
        ResultDto<Void> result = ResultDto.in(myInformationCommonResponse.getStatus(), myInformationCommonResponse.getMessage());

        return ResponseEntity.status(myInformationCommonResponse.getHttpStatus()).body(result);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{userId}/withdrawal")
    public ResponseEntity<ResultDto<Void>> withdrawal(@RequestHeader("ACCESS-TOKEN") String requestAccessToken,
                                                      @PathVariable Integer userId) {
        CommonResponse withdrawalCommonResponse = myPageService.withdrawal(requestAccessToken, userId);
        ResultDto<Void> result = ResultDto.in(withdrawalCommonResponse.getStatus(), withdrawalCommonResponse.getMessage());

        return ResponseEntity.status(withdrawalCommonResponse.getHttpStatus()).body(result);

    }
}
