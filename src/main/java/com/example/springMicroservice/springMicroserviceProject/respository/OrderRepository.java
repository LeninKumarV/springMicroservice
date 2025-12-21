package com.example.springMicroservice.springMicroserviceProject.respository;

import com.example.springMicroservice.springMicroserviceProject.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
