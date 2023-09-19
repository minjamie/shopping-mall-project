package com.example.shopping.domain;

// TODO: 민혁님

import lombok.*;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "login")
public class Login {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    private String refreshToken;

    private int count;

    public void increaseCount() {
        this.count += 1;
    }

}
