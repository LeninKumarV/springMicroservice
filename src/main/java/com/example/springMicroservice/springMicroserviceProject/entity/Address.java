package com.example.springMicroservice.springMicroserviceProject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "address")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID addressId;
    private String street;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private Timestamp createdOn;
    private Timestamp updatedOn;
}

