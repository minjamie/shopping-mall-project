package com.example.shopping.repository.delivery;

import com.example.shopping.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {
    Delivery findByAddressId(Integer id);
}
