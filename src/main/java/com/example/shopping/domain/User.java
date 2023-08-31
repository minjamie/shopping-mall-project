package com.example.shopping.domain;

import com.example.shopping.domain.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
// 화정: AuditingEntityListener 기능 추가

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@Builder
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;           // 이름
    private String email;          // 이메일 (로그인 시 사용)
    private String password;       // 비밀번호
    private String phoneNumber;    // 핸드폰 번호
    private String sex;            // 성별
    private String profileImgUrl; // 프로필 이미지 url
    private String introduce;   // 간단 소개

    @OneToMany(mappedBy = "user")
    private List<Address> addressIds; // 주소 ID 리스트 1:N

    // ROLE_{name} 형식
    @OneToMany(mappedBy = "user")
    private Collection<Role> roles;    // 역할 1:N

    private boolean isAuth;     // 인증 유무
    private boolean isWithdrawal;   // 탈퇴 유무
}
