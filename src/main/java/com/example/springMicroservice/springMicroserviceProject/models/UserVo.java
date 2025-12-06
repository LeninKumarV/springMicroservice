package com.example.springMicroservice.springMicroserviceProject.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserVo {

    private UUID userId;
    private String firstName;
    private String lastName;
    private String response;
}