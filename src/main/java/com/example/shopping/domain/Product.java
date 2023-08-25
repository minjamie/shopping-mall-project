package com.example.shopping.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

// TODO : 화정 AuditingEntityListener 기능 추가
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer price;
    private String description;
    private Integer discount;
    private Integer stock;
    private Boolean isNew;
    private Integer deliveryPrice;
    private LocalDateTime saleEndDate;
    private Integer CategoryId;
}
