package com.example.shopping.controller.product;

import com.example.shopping.dto.common.CommonResponse;
import com.example.shopping.dto.common.ResultDto;
import com.example.shopping.dto.product.ProductListResponseDto;
import com.example.shopping.dto.product.ProductResponseDto;
import com.example.shopping.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

/*    @GetMapping("")
    public ResponseEntity<ResultDto<ProductListResponseDto>> getProductList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size){
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }*/

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

    @GetMapping("/{productId}")
    public ResponseEntity<ResultDto<ProductResponseDto>> getProduct(@PathVariable Integer productId){
        CommonResponse productInfoResponse = productService.getProduct(productId);
        ResultDto<ProductResponseDto> result = ResultDto.in(productInfoResponse.getStatus(), productInfoResponse.getMessage());
        result.setData((ProductResponseDto) productInfoResponse.getData());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


}
