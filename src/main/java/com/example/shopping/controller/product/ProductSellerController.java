package com.example.shopping.controller.product;


import com.example.shopping.dto.auth.AuthInfoUserId;
import com.example.shopping.dto.category.CategoryListResponseDto;
import com.example.shopping.dto.category.SubCategoryListResponseDto;
import com.example.shopping.dto.common.CommonResponse;
import com.example.shopping.dto.common.ResultDto;
import com.example.shopping.dto.product.*;
import com.example.shopping.security.JwtTokenProvider;
import com.example.shopping.service.product.ProductSellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/seller")
public class ProductSellerController {

    private final ProductSellerService productSellerService;
    private final JwtTokenProvider jwtTokenProvider;
    public static final String TOKEN_PREFIX = "Bearer ";



    @PostMapping("/products")
    public ResponseEntity<ResultDto<Void>> registerProduct(
            @RequestHeader("ACCESS-TOKEN") String accessToken, @RequestBody ProductRequestDto insertProductDto){
        AuthInfoUserId user = jwtTokenProvider.getUserId(accessToken.substring(TOKEN_PREFIX.length()));
        CommonResponse commonResponse = productSellerService.insertProduct(user.getUserId(), insertProductDto);
        ResultDto<Void> result = ResultDto.in(commonResponse.getStatus(), commonResponse.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    @GetMapping("/products/{productId}")
    public ResponseEntity<ResultDto<ProductResponseDto>> productEdit(
            @RequestHeader("ACCESS-TOKEN") String accessToken, @PathVariable Integer productId){
        AuthInfoUserId user = jwtTokenProvider.getUserId(accessToken.substring(TOKEN_PREFIX.length()));
        CommonResponse productInfoResponse = productSellerService.productEdit(user.getUserId(), productId);
        ResultDto<ProductResponseDto> result = ResultDto.in(productInfoResponse.getStatus(), productInfoResponse.getMessage());
        result.setData((ProductResponseDto) productInfoResponse.getData());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    @PutMapping("/proudcts/{productId}")
    public ResponseEntity<ResultDto<Void>> updateProduct(
            @RequestHeader("ACCESS-TOKEN") String accessToken, @PathVariable Integer productId, @RequestBody ProductRequestDto updateProductDto){
        AuthInfoUserId user = jwtTokenProvider.getUserId(accessToken.substring(TOKEN_PREFIX.length()));
        CommonResponse  updateProductResponse= productSellerService.updateProduct(user.getUserId(), productId ,updateProductDto);
        ResultDto<Void> result = ResultDto.in(updateProductResponse.getStatus(), updateProductResponse.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    @GetMapping("/products/categories")
    public ResponseEntity<ResultDto<CategoryListResponseDto>> getCategoryList(){
        CommonResponse categoryListResponse = productSellerService.getCategoryList();
        ResultDto<CategoryListResponseDto> result = ResultDto.in(categoryListResponse.getStatus(), categoryListResponse.getMessage());
        result.setData((CategoryListResponseDto) categoryListResponse.getData());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    @GetMapping("/products/subcategory")
    public ResponseEntity<ResultDto<SubCategoryListResponseDto>> getSubCategoryList(@RequestParam("categoryId") Integer categoryId){
        CommonResponse subCategoryListResponse = productSellerService.getSubCategoryList(categoryId);
        ResultDto<SubCategoryListResponseDto> result = ResultDto.in(subCategoryListResponse.getStatus(), subCategoryListResponse.getMessage());
        result.setData((SubCategoryListResponseDto) subCategoryListResponse.getData());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


/*
    @GetMapping("/proudcts")
    public ResponseEntity<ResultDto<ProductListResponseDto>> getSellerProductList(
            @RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "10") int size){
        PageRequest pageable = PageRequest.of(page-1, size);
        CommonResponse productListResponse = productSellerService.getProductList(1, pageable);
        ResultDto<ProductListResponseDto> result = ResultDto.in(productListResponse.getStatus(), productListResponse.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
*/





}
