package com.example.shopping.domain;


import com.example.shopping.domain.Enum.SellStatus;
import com.example.shopping.domain.common.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // 상품아이디

    @Column(nullable = false, length = 100)
    private String name; // 상품명

    @Column(nullable = false)
    private Integer price; // 가격

    @Lob
    @Column(nullable = false)
    private String description; // 설명

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SellStatus sellStatus; // 판매상태

    @Column
    private Integer discountRate; // 할인율
  
    @Column(nullable = false)
    private Boolean isDiscount; // 할인여부

    @Column(nullable = false)
    private Boolean isNew; // 신상여부

    @Column(nullable = false)
    private Integer deliveryPrice; // 배송비

    private LocalDateTime saleStartDate; // 할인시작일자
    private LocalDateTime saleEndDate;   // 할인종료날짜


    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<ProductOption> productOptions = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<Brand> brands = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<Image> images = new ArrayList<>();

    public static Product createProduct(Category category, Product product){
        return Product.builder()
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .sellStatus(product.getSellStatus())
                .discountRate(product.getDiscountRate())
                .isDiscount(product.getIsDiscount())
                .isNew(product.getIsNew())
                .deliveryPrice(product.getDeliveryPrice())
                .saleStartDate(product.getSaleStartDate())
                .saleEndDate(product.getSaleEndDate())
                .category(category)
                .build();
    }

    public void updateProduct(Product updateProduct){
        this.name = updateProduct.getName();
        this.price = updateProduct.getPrice();
        this.description = updateProduct.getDescription();
        this.sellStatus = updateProduct.getSellStatus();
        this.discountRate = updateProduct.getDiscountRate();
        this.isDiscount = updateProduct.getIsDiscount();
        this.isNew = updateProduct.getIsNew();
        this.deliveryPrice = updateProduct.getDeliveryPrice();
        this.saleStartDate = updateProduct.getSaleStartDate();
        this.saleEndDate = updateProduct.getSaleEndDate();
    }



}
