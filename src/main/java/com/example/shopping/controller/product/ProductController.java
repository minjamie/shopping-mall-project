package com.example.shopping.controller.product;

import com.example.shopping.domain.Product;
import com.example.shopping.dto.common.CommonResponse;
import com.example.shopping.dto.common.ResultDto;
import com.example.shopping.dto.product.ProductResponseDto;
import com.example.shopping.service.product.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    @ApiOperation(value = "상품리스트 조회 API", notes = "상품리스트 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("")
    public ResponseEntity<ResultDto<Page<Product>>> getProductList(
           @Parameter(description = "조회 페이지", required = true, example = "1")  @RequestParam(value = "page", defaultValue = "1") int page,
           @Parameter(description = "조회 사이즈", required = true, example = "20")  @RequestParam(value = "size", defaultValue = "20") int size)
   {
     PageRequest pageable = PageRequest.of(page-1, size);
     CommonResponse resultPage = productService.getProducts(pageable);
       ResultDto<Page<Product>> result = ResultDto.in(resultPage.getStatus(), resultPage.getMessage());
       result.setData((Page<Product>) resultPage.getData());
       return ResponseEntity.status(HttpStatus.OK).body(result);
   }
/*    @GetMapping("/category/{cageroryId}")
    public ResponseEntity<ResultDto<ProductListResponseDto>> getCategoryProductList(
            @PathVariable Integer categoryId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size){
        PageRequest pageable = PageRequest.of(page-1, size);
        CommonResponse productListResponse = productService.getCategoryProductList(categoryId, pageable);
        ResultDto<Void> result = ResultDto.in(productListResponse.getStatus(), productListResponse.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }*/


    @ApiOperation(value = "상품조회 API", notes = "상품 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{productId}")
    public ResponseEntity<ResultDto<ProductResponseDto>> getProduct(@PathVariable Integer productId) {
        CommonResponse productInfoResponse = productService.getProduct(productId);
        ResultDto<ProductResponseDto> result = ResultDto.in(productInfoResponse.getStatus(), productInfoResponse.getMessage());
        result.setData((ProductResponseDto) productInfoResponse.getData());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


}
