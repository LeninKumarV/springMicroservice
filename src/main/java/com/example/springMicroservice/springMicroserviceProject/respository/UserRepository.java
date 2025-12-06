package com.example.springMicroservice.springMicroserviceProject.respository;

import com.example.springMicroservice.springMicroserviceProject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
