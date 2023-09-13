package com.example.shopping.controller.product;


import com.example.shopping.dto.auth.AuthInfoUserId;
import com.example.shopping.dto.category.CategoryListResponseDto;
import com.example.shopping.dto.category.SubCategoryListResponseDto;
import com.example.shopping.dto.common.CommonResponse;
import com.example.shopping.dto.common.ResultDto;
import com.example.shopping.dto.product.*;
import com.example.shopping.security.JwtTokenProvider;
import com.example.shopping.service.product.ProductSellerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/seller")
@Api(tags = "Seller Product APIs")
public class ProductSellerController {

    private final ProductSellerService productSellerService;
    private final JwtTokenProvider jwtTokenProvider;
    public static final String TOKEN_PREFIX = "Bearer ";


    @ApiOperation(value = "판매자 상품등록 API", notes = "판매자 상품등록")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/products")
    public ResponseEntity<ResultDto<Void>> registerProduct(
            @RequestHeader("ACCESS-TOKEN") String accessToken,
            @RequestBody InsertProductRequestDto insertProductDto) {

        AuthInfoUserId user = jwtTokenProvider.getUserId(accessToken.substring(TOKEN_PREFIX.length()));
        CommonResponse commonResponse = productSellerService.insertProduct(user.getUserId(), insertProductDto);
        ResultDto<Void> result = ResultDto.in(commonResponse.getStatus(), commonResponse.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    @ApiOperation(value = "판매자 상품조회 API", notes = "판매자 상품조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/products/{productId}")
    public ResponseEntity<ResultDto<ProductResponseDto>> productEdit(
            @RequestHeader("ACCESS-TOKEN") String accessToken,
            @PathVariable Integer productId) {

        AuthInfoUserId user = jwtTokenProvider.getUserId(accessToken.substring(TOKEN_PREFIX.length()));
        CommonResponse productInfoResponse = productSellerService.productEdit(user.getUserId(), productId);
        ResultDto<ProductResponseDto> result = ResultDto.in(productInfoResponse.getStatus(), productInfoResponse.getMessage());
        result.setData((ProductResponseDto) productInfoResponse.getData());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    @ApiOperation(value = "판매자 상품수정 API", notes = "판매자 상품수정")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/products/{productId}")
    public ResponseEntity<ResultDto<Void>> updateProduct(
            @RequestHeader("ACCESS-TOKEN") String accessToken,
            @PathVariable Integer productId, @RequestBody UpdateProductRequestDto updateProductDto) {

        AuthInfoUserId user = jwtTokenProvider.getUserId(accessToken.substring(TOKEN_PREFIX.length()));
        CommonResponse updateProductResponse = productSellerService.updateProduct(user.getUserId(), productId, updateProductDto);
        ResultDto<Void> result = ResultDto.in(updateProductResponse.getStatus(), updateProductResponse.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    // 미해결
    @ApiOperation(value = "판매자 상품 목록조회 API", notes = "판매자 상품 목록조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/proudcts")
    public ResponseEntity<ResultDto<ProductListResponseDto>> getSellerProductList(
            @RequestHeader("ACCESS-TOKEN") String accessToken,
            @RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "10") int size){

        AuthInfoUserId user = jwtTokenProvider.getUserId(accessToken.substring(TOKEN_PREFIX.length()));
        PageRequest pageable = PageRequest.of(page-1, size);
        CommonResponse productListResponse = productSellerService.getProductList(user.getUserId(), pageable);
        ResultDto<ProductListResponseDto> result = ResultDto.in(productListResponse.getStatus(), productListResponse.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }



    @ApiOperation(value = "상품 카테고리 목록조회 API", notes = "상품 등록시에 조회, 카테고리 목록 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/products/categories")
    public ResponseEntity<ResultDto<CategoryListResponseDto>> getCategoryList() {

        CommonResponse categoryListResponse = productSellerService.getCategoryList();
        ResultDto<CategoryListResponseDto> result = ResultDto.in(categoryListResponse.getStatus(), categoryListResponse.getMessage());
        result.setData((CategoryListResponseDto) categoryListResponse.getData());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    @ApiOperation(value = "상품 서브카테고리 목록조회 API", notes = "상품 등록시 조회, 서브카테고리 목록조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/products/subcategory")
    public ResponseEntity<ResultDto<SubCategoryListResponseDto>> getSubCategoryList(@RequestParam("categoryId") Integer categoryId) {
        CommonResponse subCategoryListResponse = productSellerService.getSubCategoryList(categoryId);
        ResultDto<SubCategoryListResponseDto> result = ResultDto.in(subCategoryListResponse.getStatus(), subCategoryListResponse.getMessage());
        result.setData((SubCategoryListResponseDto) subCategoryListResponse.getData());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


}

