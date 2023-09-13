package com.example.shopping.repository.role;

import com.example.shopping.domain.Role;
import com.example.shopping.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    void deleteByUserId(Integer userId);
    List<Role> findByUser(User user);

}
