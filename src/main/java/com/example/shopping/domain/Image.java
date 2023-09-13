package com.example.shopping.domain;

import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 500)
    private String url; // 이미지 경로

    @Column(nullable = false, length = 20)
    private String type; // 이미지 종류(대표,서브)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public void updateImage(String url, String type){
        this.url = url;
        this.type = type;
    }

}
