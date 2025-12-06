package com.example.springMicroservice.springMicroserviceProject.services;

import com.example.springMicroservice.springMicroserviceProject.entity.User;
import com.example.springMicroservice.springMicroserviceProject.models.UserVo;
import com.example.springMicroservice.springMicroserviceProject.respository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserVo saveUser(UserVo userVo) {

        User savedUser = userRepository.save(
                User.builder()
                        .firstName(userVo.getFirstName())
                        .lastName(userVo.getLastName())
                        .build()
        );

        return UserVo.builder()
                .userId(savedUser.getUserId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .response("User saved successfully!")
                .build();
    }

    public List<UserVo> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> UserVo.builder()
                        .userId(user.getUserId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .build())
                .toList();
    }

    public Optional<UserVo> getUserById(UUID id) {
        return userRepository.findById(id)
                .map(user -> UserVo.builder()
                        .userId(user.getUserId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .build());
    }
}

