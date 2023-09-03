package com.example.shopping.repository.address;

import com.example.shopping.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {


    List<Address> findAllByUserId(int UserId);
}
