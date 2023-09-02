package com.example.shopping.service.cart;

import com.example.shopping.domain.Cart;
import com.example.shopping.domain.Product;
import com.example.shopping.domain.User;
import com.example.shopping.domain.ProductOption;
import com.example.shopping.dto.cart.AddCartRequest;
import com.example.shopping.dto.cart.CartResponse;
import com.example.shopping.dto.cart.OrderCartRequest;
import com.example.shopping.dto.cart.UpdatedCartRequest;
import com.example.shopping.repository.cart.CartRepository;
import com.example.shopping.repository.cart.ProductOptionRepository;
import com.example.shopping.repository.product.ProductRepository;
import com.example.shopping.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService implements CartServiceInterface {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;

    @Transactional
    @Override
    public CartResponse addCart(Integer productId, AddCartRequest addCartRequest) {
        Optional<User> userOptional = userRepository.findById(addCartRequest.getUserId());
        User user = userOptional.get();

        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            return createErrorResponse("해당 상품을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }
        Product product = productOptional.get();

        Optional<ProductOption> productOption = this.findOptionById(product, addCartRequest.getOptionId());

        if(productOption.isEmpty()) return createErrorResponse("해당 옵션을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
        if(productOption.isPresent() &&  productOption.get().getStock() < addCartRequest.getCount()) return createErrorResponse("재고가 부족합니다.", HttpStatus.BAD_REQUEST);

        Optional<Cart> existedCart = cartRepository.findByIdAndUserId(productOption.get().getId(), user.getId());
        if(existedCart.isPresent()) {
            return createErrorResponse("이미 등록된 장바구니 상품입니다.", HttpStatus.BAD_REQUEST);
        } else {
            Cart cart = new Cart()
                    .builder()
                    .user(user)
                    .count(addCartRequest.getCount())
                    .productOption(productOption.get())
                    .build();
            cartRepository.save(cart);
            return createSuccessResponse("장바구니 등록 완료했습니다.", HttpStatus.CREATED);
        }
    }

    public CartResponse updateCart(Integer cartId, UpdatedCartRequest updateCartRequest) {
        Optional<User> userOptional = userRepository.findById(updateCartRequest.getUserId());
        User user = userOptional.get();

        Optional<Cart> cartOptional = cartRepository.findById(cartId);
        if(cartOptional.get().getProductOption().getProduct().getId() != updateCartRequest.getProductId()) {
            return createErrorResponse("장바구니 내 상품을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        } else if(cartOptional.isPresent()) {
            Optional<Product> productOptional = productRepository.findById(updateCartRequest.getProductId());
            Product product = productOptional.get();

            Optional<ProductOption> productOption = productOptionRepository.findByProductIdAndOptionId(product.getId(), updateCartRequest.getOptionId());

            if(productOption.isEmpty()) return createErrorResponse("해당 옵션을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
            if(productOption.isPresent() &&  productOption.get().getStock() < updateCartRequest.getCount()) return createErrorResponse("재고가 부족합니다.", HttpStatus.BAD_REQUEST);

            Cart cart = cartOptional.get();
            cart.setProductOption(cartOptional.get().getProductOption());
            cart.setCount(updateCartRequest.getCount());
            cart.setDelete(updateCartRequest.getIsDelete());
            cart.setUser(user);
            cartRepository.save(cart);
        }
        return createSuccessResponse(String.format("장바구니 %s 완료했습니다", updateCartRequest.getIsDelete() ? "삭제" : "수정"), HttpStatus.OK);
    }


    public CartResponse orderCart(String cartIds, OrderCartRequest orderCartRequest) {
        return createErrorResponse("주문 완료했습니다.", HttpStatus.CREATED);
    }

    private CartResponse createSuccessResponse(String message, HttpStatus httpStatus) {
        return CartResponse.builder()
                .message(message)
                .status("success")
                .httpStatus(httpStatus)
                .build();
    }
    private CartResponse createErrorResponse(String message, HttpStatus httpStatus) {
        return CartResponse.builder()
                .message(message)
                .status("fail")
                .httpStatus(httpStatus)
                .build();
    }
    private Optional<ProductOption> findOptionById(Product product, Integer optionId) {
        return product.getProductOptions().stream()
                .filter(option -> option.getId().equals(optionId))
                .findFirst();
    }
}
