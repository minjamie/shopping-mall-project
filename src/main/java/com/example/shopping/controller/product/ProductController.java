package com.example.shopping.controller.product;

import com.example.shopping.dto.common.CommonResponse;
import com.example.shopping.dto.common.ResultDto;
import com.example.shopping.dto.product.ProductListResponseDto;
import com.example.shopping.dto.product.ProductDetailResponseDto;
import com.example.shopping.service.product.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@Api(tags = "Product APIs")
public class ProductController {

    private final ProductService productService;

    @ApiOperation(value = "상품목록 조회 API", notes = "상품목록 조회")
    @GetMapping("")
    public ResponseEntity<ResultDto<ProductListResponseDto>> getProductList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size){

      PageRequest pageable = PageRequest.of(page-1, size);
      CommonResponse productListResponse = productService.getProductList(pageable);
      ResultDto<ProductListResponseDto> result = ResultDto.in(productListResponse.getStatus(), productListResponse.getMessage());
      result.setData((ProductListResponseDto) productListResponse.getData());
      return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    @ApiOperation(value = "상품조회 API", notes = "상품 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{productId}")
    public ResponseEntity<ResultDto<ProductDetailResponseDto>> getProduct(@PathVariable Integer productId) {

        CommonResponse productInfoResponse = productService.getProduct(productId);
        ResultDto<ProductDetailResponseDto> result = ResultDto.in(productInfoResponse.getStatus(), productInfoResponse.getMessage());
        result.setData((ProductDetailResponseDto) productInfoResponse.getData());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
