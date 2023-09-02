package com.example.shopping.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "deliverys")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    @Column(name="contact_a")
    private String contactA;
    @Column(name="contact_b")
    private String contactB;
    private String recipient;
    private String request;
    @Column(name = "zip_code")
    private String zieCode;

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    private Address address;
}
