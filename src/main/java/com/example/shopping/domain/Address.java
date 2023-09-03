package com.example.shopping.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String main;

    @Column(nullable = true)
    private String detail;
    private Boolean isOrder;
    @Column(name = "zip_code")
    private String zieCode;


    @Column(nullable = true, columnDefinition = "DEFAULT false")
    private Boolean isDefault;
}
