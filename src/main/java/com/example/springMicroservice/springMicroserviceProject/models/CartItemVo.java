package com.example.springMicroservice.springMicroserviceProject.models;

import com.example.springMicroservice.springMicroserviceProject.entity.Products;
import com.example.springMicroservice.springMicroserviceProject.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemVo {

    private UUID cartItemId;
    private UUID userId;
    private UUID productId;
    private User user;
    private Products products;
    private BigDecimal price;
    private BigInteger quantity;
    private Timestamp createdOn;
    private Timestamp updatedOn;
    private String response;
}

