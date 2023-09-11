package com.example.shopping.repository.login;

import com.example.shopping.domain.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<Login, Integer> {

    Login findByRefreshToken(String refreshToken);

    Optional<Login> findByUserId(Integer userId);

    void deleteByUserId(Integer userId);

    Optional<Login> findByUserEmail(String email);
}
