package com.example.shopping.domain;

import com.example.shopping.domain.common.BaseTimeEntity;
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
@Table(name = "users")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String sex;
    private String profileImgUrl;
    private String introduce;
    private Integer addressId;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private boolean isAuth;
    private boolean isWithdrawal;

    @OneToOne(mappedBy = "user")
    private Pay pay;
}
