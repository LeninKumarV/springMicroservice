package com.example.springMicroservice.springMicroserviceProject.models;

import com.example.springMicroservice.springMicroserviceProject.entity.Order;
import com.example.springMicroservice.springMicroserviceProject.entity.Products;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
public class OrderItemVo {

        private UUID orderItemId;
        @JsonIgnore
        private OrderVo order;
        private ProductsVo products;
        private BigDecimal price;
        private BigInteger quantity;
        private Timestamp createdOn;
        private Timestamp updatedOn;

}
