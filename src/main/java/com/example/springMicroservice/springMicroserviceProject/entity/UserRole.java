package com.example.springMicroservice.springMicroserviceProject.entity;

import com.example.springMicroservice.springMicroserviceProject.services.UserService;
import lombok.Getter;

@Getter
public enum UserRole {

    USER("user"),
    CUSTOMER("customer"),
    ADMIN("admin");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

}
