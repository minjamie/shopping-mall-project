package com.example.shopping.domain;

import com.example.shopping.domain.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
// TODO : 화정 AuditingEntityListener 기능 추가

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "carts")
public class Cart extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer productId;
    private Integer userId;
    private Integer count;
}
