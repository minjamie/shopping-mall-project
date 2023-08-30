package com.example.shopping.domain;

import com.example.shopping.domain.common.BaseTimeEntity;
import io.swagger.models.auth.In;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "pays")
public class Pay {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;
        private Integer amount;
        @OneToOne
        private User user;
}
