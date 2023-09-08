package com.example.shopping.controller.mypage;


import com.example.shopping.dto.common.CommonResponse;
import com.example.shopping.dto.common.ResultDto;
import com.example.shopping.dto.mypage.MyPageResponse;
import com.example.shopping.service.mypage.MyPageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Api(tags = "MyPage APIs")
public class MyPageController {

    private final MyPageService myPageService;

    @ApiOperation(value = "내 정보 조회 API", notes = "내 정보를 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}")
    public ResponseEntity<ResultDto<MyPageResponse>> myInformation(@ApiIgnore @RequestHeader("ACCESS-TOKEN") String requestAccessToken,
                                                                   @ApiParam(name = "id", value = "userId", example = "1") @PathVariable Integer userId) {
        CommonResponse myInformationCommonResponse = myPageService.myInformation(requestAccessToken, userId);
        ResultDto<MyPageResponse> result = ResultDto.in(myInformationCommonResponse.getStatus(), myInformationCommonResponse.getMessage());
        result.setData((MyPageResponse) myInformationCommonResponse.getData());

        return ResponseEntity.status(myInformationCommonResponse.getHttpStatus()).body(result);
    }

    @ApiOperation(value = "회원탈퇴 API", notes = "유저 회원탈퇴 진행")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{userId}/withdrawal")
    public ResponseEntity<ResultDto<Void>> withdrawal(@ApiIgnore @RequestHeader("ACCESS-TOKEN") String requestAccessToken,
                                                      @ApiParam(name = "id", value = "userId", example = "1") @PathVariable Integer userId) {
        CommonResponse withdrawalCommonResponse = myPageService.withdrawal(requestAccessToken, userId);
        ResultDto<Void> result = ResultDto.in(withdrawalCommonResponse.getStatus(), withdrawalCommonResponse.getMessage());

        return ResponseEntity.status(withdrawalCommonResponse.getHttpStatus()).body(result);

    }
}
