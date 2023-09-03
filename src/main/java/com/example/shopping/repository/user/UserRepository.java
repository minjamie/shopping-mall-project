package com.example.shopping.repository.user;

import com.example.shopping.domain.User;
import com.example.shopping.dto.user.OrderListResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

//    List<OrderListResponse> findOrderList(Integer userId);
//    List<OrderListResponse> findCartList(Integer userId);
}
