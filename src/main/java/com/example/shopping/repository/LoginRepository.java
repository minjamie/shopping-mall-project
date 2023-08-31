package com.example.shopping.repository;

import com.example.shopping.domain.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<Login, Integer> {

    void deleteByUserId(Integer userId);

    Login findByUserId(Integer userId);


    Optional<Login> findByUserEmail(String email);
}
