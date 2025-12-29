package com.example.springMicroservice.springMicroserviceProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private List<UserRole> role;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "address_id",          // column in users table
            referencedColumnName = "addressId"
    )
    private Address address;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
