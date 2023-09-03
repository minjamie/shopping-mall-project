package com.example.shopping.domain;

import com.example.shopping.domain.common.BaseTimeEntity;
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

    @Column
    private Integer discount; // 할인금액

    @Column(nullable = false)
    private Boolean isDiscount; // 할인여부

    @Column(nullable = false)
    private Boolean isNew; // 신상여부

    @Column(nullable = false)
    private Integer deliveryPrice; // 배송비

    private LocalDateTime saleEndDate; // 할인종료날짜

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<ProductOption> productOptions = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<Brand> brands = new ArrayList<>();
}
