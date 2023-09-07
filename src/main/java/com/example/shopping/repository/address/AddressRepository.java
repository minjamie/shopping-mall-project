package com.example.shopping.repository.address;

import com.example.shopping.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    Optional<Address> findByUserId(Integer userId);
    List<Address> findAllByUserId(int UserId);

    void deleteByUserId(Integer userId);
}
