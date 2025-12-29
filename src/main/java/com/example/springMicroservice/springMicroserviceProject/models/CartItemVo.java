package com.example.springMicroservice.springMicroserviceProject.models;

import com.example.springMicroservice.springMicroserviceProject.entity.Products;
import com.example.springMicroservice.springMicroserviceProject.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemVo {

    private UUID cartItemId;
    private UUID userId;
    private UUID productId;
    private UserVo user;
    private ProductsVo products;
    private BigDecimal price;
    private BigInteger quantity;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private String response;
}

