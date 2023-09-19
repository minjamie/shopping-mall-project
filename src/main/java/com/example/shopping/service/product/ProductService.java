package com.example.shopping.service.product;


import com.example.shopping.domain.*;
import com.example.shopping.dto.common.CommonResponse;
import com.example.shopping.dto.product.ProductResponseDto;
import com.example.shopping.repository.brand.BrandRepository;
import com.example.shopping.repository.category.CategoryRepository;
import com.example.shopping.repository.image.ImageRepository;
import com.example.shopping.repository.product.ProductRepository;
import com.example.shopping.service.error.ErrorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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


/*
    public CommonResponse getCategoryProductList(Integer categoryId, Pageable pageable){
        Optional<Category> category = categoryRepository.findById(categoryId);
        if(category.isEmpty()){
            return errorService.createErrorResponse("해당 카테고리를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, null);
        }
        return errorService.createSuccessResponse("카테고리 상품목록 조회 완료하였습니다.", HttpStatus.OK, null);
    }
*/



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

        ProductResponseDto productResponseDto = ProductResponseDto.of(product.get(), brand.get(), image.get(), productOptionList);
        return errorService.createSuccessResponse("판매자 상품조회 완료하였습니다.", HttpStatus.OK, productResponseDto);
    }


    public CommonResponse getProducts(PageRequest pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        if (products.isEmpty()){
            return errorService.createErrorResponse("해당 상품들을 찾을 수 없습니다.", HttpStatus.NOT_FOUND, null);
        } else {
            return errorService.createSuccessResponse("상품 리스트 조회 완료하였습니다.", HttpStatus.OK, products);
        }
    }
}
