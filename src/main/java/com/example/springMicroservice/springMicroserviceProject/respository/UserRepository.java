package com.example.springMicroservice.springMicroserviceProject.respository;

import com.example.springMicroservice.springMicroserviceProject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
}
