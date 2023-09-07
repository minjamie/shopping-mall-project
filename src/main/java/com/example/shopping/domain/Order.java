package com.example.shopping.domain;

// TODO: 민재
// TODO : 화정 AuditingEntityListener 기능 추가

import com.example.shopping.domain.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer count;

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    private Delivery delivery;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;
}
