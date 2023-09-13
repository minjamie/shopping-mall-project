package com.example.shopping.service.product;

import com.example.shopping.domain.*;
import com.example.shopping.domain.Enum.RoleType;
import com.example.shopping.dto.category.CategoryListResponseDto;
import com.example.shopping.dto.category.CategoryResponseDto;
import com.example.shopping.dto.category.SubCategoryListResponseDto;
import com.example.shopping.dto.category.SubCategoryResponseDto;
import com.example.shopping.dto.common.CommonResponse;
import com.example.shopping.dto.product.*;
import com.example.shopping.repository.brand.BrandRepository;
import com.example.shopping.repository.cart.ProductOptionRepository;
import com.example.shopping.repository.category.CategoryRepository;
import com.example.shopping.repository.category.SubCategoryRepository;
import com.example.shopping.repository.image.ImageRepository;
import com.example.shopping.repository.option.OptionRepository;
import com.example.shopping.repository.product.ProductRepository;
import com.example.shopping.repository.role.RoleRepository;
import com.example.shopping.repository.user.UserRepository;
import com.example.shopping.service.error.ErrorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductSellerService {

    private final ErrorService errorService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ImageRepository imageRepository;
    private final BrandRepository brandRepository;

    @Transactional
    public CommonResponse insertProduct(Integer userId, InsertProductRequestDto insertProductDto){

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return errorService.createErrorResponse("해당 유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, null);
        }
        List<Role> roleList = roleRepository.findByUser(user.get());
        Optional<Role> optionalRole  = roleList.stream()
                .filter(role -> role.getName().equals(RoleType.ROLE_SELLER.toString()))
                .findFirst();

        if (optionalRole.isEmpty()) {
            return errorService.createErrorResponse("해당 유저는 판매자가 아닙니다.", HttpStatus.NOT_FOUND, null);
        }

        Optional<Category> category = categoryRepository.findById(insertProductDto.getCategoryId());
        if (category.isEmpty()) {
            return errorService.createErrorResponse("해당 카테고리를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, null);
        }
        List<SubCategory> subCategoryList = subCategoryRepository.findSubCategoriesByCategory(category.get());

        Optional<SubCategory> optionalSubCategory = subCategoryList.stream()
                .filter(subCategory-> subCategory.getId() == insertProductDto.getSubcategoryId())
                .findFirst();
        if(optionalSubCategory.isEmpty()){
            return errorService.createErrorResponse("해당 서브카테고리는 존재하지 않습니다.", HttpStatus.NOT_FOUND, null);
        }

        Product product = insertProductDto.toEntity();
        Product createProduct = Product.createProduct(category.get(), product);
        productRepository.save(createProduct);

        Image image = Image.builder()
                .type(insertProductDto.getImageType())
                .url(insertProductDto.getImageUrl())
                .product(createProduct)
                .build();
        imageRepository.save(image);

        Optional<Option> option = optionRepository.findById(insertProductDto.getOptionId());
        if (option.isEmpty()) {
            return errorService.createErrorResponse("해당 옵션을 찾을 수 없습니다.", HttpStatus.NOT_FOUND, null);
        }
        ProductOption productOption = ProductOption.builder()
                .product(createProduct)
                .option(option.get())
                .stock(insertProductDto.getStock())
                .build();
        productOptionRepository.save(productOption);
        return errorService.createSuccessResponse("판매자 상품등록 완료하였습니다.", HttpStatus.OK, null);
    }


    @Transactional
    public CommonResponse updateProduct(Integer userId, Integer productId, UpdateProductRequestDto updateProductDto){

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return errorService.createErrorResponse("해당 유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, null);
        }
        List<Role> roleList = roleRepository.findByUser(user.get());
        Optional<Role> optionalRole  = roleList.stream()
                .filter(role -> role.getName().equals(RoleType.ROLE_SELLER.toString()))
                .findFirst();

        if (optionalRole.isEmpty()) {
            return errorService.createErrorResponse("해당 유저는 판매자가 아닙니다.", HttpStatus.NOT_FOUND, null);
        }

        Optional<Category> category = categoryRepository.findById(updateProductDto.getCategoryId());
        if (category.isEmpty()) {
            return errorService.createErrorResponse("해당 카테고리를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, null);
        }
        List<SubCategory> subCategoryList = subCategoryRepository.findSubCategoriesByCategory(category.get());

        Optional<SubCategory> optionalSubCategory = subCategoryList.stream()
                .filter(subCategory-> subCategory.getId() == updateProductDto.getSubcategoryId())
                .findFirst();
        if(optionalSubCategory.isEmpty()){
            return errorService.createErrorResponse("해당 서브카테고리는 존재하지 않습니다.", HttpStatus.NOT_FOUND, null);
        }

        Product updateProduct = updateProductDto.toEntity();
        Optional<Product> product = productRepository.findById(productId);
        if(product.isEmpty()){
            return errorService.createErrorResponse("해당 상품은 존재하지 않습니다.", HttpStatus.NOT_FOUND, null);
        }else{
            product.get().updateProduct(updateProduct);
        }

        Optional<Image> image = imageRepository.findImageByProduct(product.get());
        if(image.isEmpty()){
            return errorService.createErrorResponse("해당 상품의 이미지가 존재하지 않습니다.", HttpStatus.NOT_FOUND, null);
        }else{
            image.get().updateImage(updateProductDto.getImageUrl(), updateProductDto.getImageType());
        }


        // 옵션 재고수량 변경
        Optional<ProductOption> productOption = productOptionRepository.findByProductIdAndOptionId(updateProductDto.getProductId(), updateProductDto.getOptionId());
        if(productOption.isEmpty()){
            return errorService.createErrorResponse("해당 상품의 옵션을 찾을 수 없습니다.", HttpStatus.NOT_FOUND, null);
        }
        productOption.get().updateStock(updateProductDto.getStock());

        return errorService.createSuccessResponse("판매자 상품수정 완료하였습니다.", HttpStatus.OK, null);
    }



    public CommonResponse getCategoryList(){
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty() && categories.size() <= 0) {
            return errorService.createErrorResponse("상품 카테고리 목록을 찾을수 없습니다.", HttpStatus.NOT_FOUND, null);
        }

        List<CategoryResponseDto> categoryResponseList = categories.stream()
                .map(category -> CategoryResponseDto.of(category)).collect(Collectors.toList());

        CategoryListResponseDto categoryList
                = CategoryListResponseDto.builder().categoryList(categoryResponseList).build();
        return errorService.createSuccessResponse("상품 카테고리 목록조회 완료하였습니다.", HttpStatus.OK, categoryList);
    }


    public CommonResponse getSubCategoryList(Integer categoryId){
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            return errorService.createErrorResponse("카테고리를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, null);
        }
        List<SubCategory> subCategories = subCategoryRepository.findSubCategoriesByCategory(category.get());
        if (subCategories.isEmpty() && subCategories.size() <= 0) {
            return errorService.createErrorResponse("해당 상품의 서브카테고리를 찾을수 없습니다.", HttpStatus.NOT_FOUND, null);
        }

        List<SubCategoryResponseDto> subCategoryResponseList = subCategories.stream()
                .map(subCategory -> SubCategoryResponseDto.of(subCategory)).collect(Collectors.toList());

        SubCategoryListResponseDto subCategoryList
                = SubCategoryListResponseDto.builder()
                .subCategoryList(subCategoryResponseList).build();
        return errorService.createSuccessResponse( category.get().getName() +" 서브카테고리 조회 완료하였습니다.", HttpStatus.OK, subCategoryList);
    }


    public CommonResponse productEdit(Integer userId, Integer productId){
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return errorService.createErrorResponse("해당 유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, null);
        }
        List<Role> roleList = roleRepository.findByUser(user.get());
        Optional<Role> optionalRole  = roleList.stream()
                .filter(role -> role.getName().equals(RoleType.ROLE_SELLER.toString()))
                .findFirst();
        if (optionalRole.isEmpty()) {
            return errorService.createErrorResponse("해당 유저는 판매자가 아닙니다.", HttpStatus.NOT_FOUND, null);
        }


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


    public CommonResponse getProductList(Integer userId, Pageable pageable){
        return errorService.createErrorResponse("판매자 상품목록 조회 완료하였습니다.", HttpStatus.OK, null);
    }









}
