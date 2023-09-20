// ProductService
package com.example.shopping.service.product;


import com.example.shopping.domain.*;
import com.example.shopping.dto.common.CommonResponse;
import com.example.shopping.dto.common.Pagination;
import com.example.shopping.dto.product.ProductDetailResponseDto;
import com.example.shopping.dto.product.ProductListResponseDto;
import com.example.shopping.dto.product.ProductResponseDto;
import com.example.shopping.repository.brand.BrandRepository;
import com.example.shopping.repository.category.CategoryRepository;
import com.example.shopping.repository.image.ImageRepository;
import com.example.shopping.repository.product.ProductRepository;
import com.example.shopping.service.error.ErrorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ErrorService errorService;
    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;



    public CommonResponse getProductList(Pageable pageable){
        Page<ProductResponseDto> result = productRepository.getProductListAll(pageable);
        Pagination pagination = new Pagination(result.getTotalPages(), result.getTotalElements(),  result.getNumber()+1, result.isLast());
        ProductListResponseDto productListResponseDto = new ProductListResponseDto(pagination, result.getContent());
        return errorService.createSuccessResponse("상품 목록 조회 완료하였습니다.", HttpStatus.OK, productListResponseDto);
    }


    public CommonResponse getProduct(Integer productId){
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()) {
            return errorService.createErrorResponse("해당 상품을 찾을 수 없습니다.", HttpStatus.NOT_FOUND, null);
        }
        List<ProductOption> productOptionList = product.get().getProductOptions();
        // List<Brand> brandList = product.get().getBrands();
        Optional<Image> image = imageRepository.findImageByProduct(product.get());
        if (image.isEmpty()){
            return errorService.createErrorResponse("해당 상품 이미지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, null);
        }
        Optional<Brand> brand = brandRepository.findBrandByProduct(product.get());
        if (brand.isEmpty()){
            return errorService.createErrorResponse("해당 상품 브랜드를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, null);
        }

        ProductDetailResponseDto productDetailResponseDto = ProductDetailResponseDto.of(product.get(), brand.get(), image.get(), productOptionList);
        return errorService.createSuccessResponse("상품 조회 완료하였습니다.", HttpStatus.OK, productDetailResponseDto);
    }


}