package com.example.shopping.domain;
// TODO: 민재

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "options")
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    private String category;

    private String name;

    @OneToMany(mappedBy = "option")
    List<ProductOption> productOptions = new ArrayList<>();
}
