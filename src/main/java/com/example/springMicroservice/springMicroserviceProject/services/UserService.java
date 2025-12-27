package com.example.springMicroservice.springMicroserviceProject.services;

import com.example.springMicroservice.springMicroserviceProject.entity.Address;
import com.example.springMicroservice.springMicroserviceProject.entity.User;
import com.example.springMicroservice.springMicroserviceProject.entity.UserRole;
import com.example.springMicroservice.springMicroserviceProject.models.AddressVo;
import com.example.springMicroservice.springMicroserviceProject.models.UserVo;
import com.example.springMicroservice.springMicroserviceProject.respository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserVo saveUser(UserVo userVo) {

        Address address = null;
        if (userVo.getAddress() != null) {
            address = mapToAddressEntity(userVo.getAddress());
        }

        User user = User.builder()
                .firstName(userVo.getFirstName())
                .lastName(userVo.getLastName())
                .email(userVo.getEmail())
                .phoneNumber(userVo.getPhoneNumber())
                .role(fromValue(userVo.getRole()))
                .address(address)
                .createdOn(new Timestamp(System.currentTimeMillis()))
                .updatedOn(new Timestamp(System.currentTimeMillis()))
                .build();

        User savedUser = userRepository.save(user);

        return mapToUserVo(savedUser, "User saved successfully!");
    }

    public List<UserVo> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> mapToUserVo(user, null))
                .toList();
    }

    public Optional<UserVo> getUserById(UUID id) {
        return userRepository.findById(id)
                .map(user -> mapToUserVo(user, null));
    }


    private UserVo mapToUserVo(User user, String responseMessage) {
        return UserVo.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(mapUserRoleToUserVo(user.getRole()))
                .address(user.getAddress() != null ? mapToAddressVo(user.getAddress()) : null)
                .createdOn(LocalDateTime.now().toString())
                .updatedOn(LocalDateTime.now().toString())
                .response(responseMessage)
                .build();
    }

    private Address mapToAddressEntity(AddressVo vo) {
        return Address.builder()
                .street(vo.getStreet())
                .city(vo.getCity())
                .state(vo.getState())
                .country(vo.getCountry())
                .postalCode(vo.getPostalCode())
                .createdOn(new Timestamp(System.currentTimeMillis()))
                .updatedOn(new Timestamp(System.currentTimeMillis()))
                .build();
    }

    private AddressVo mapToAddressVo(Address address) {
        return AddressVo.builder()
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .createdOn(LocalDateTime.now().toString())
                .updatedOn(LocalDateTime.now().toString())
                .build();
    }

    public static List<UserRole> fromValue(List<String> values) {
        List<UserRole> userRoles = new ArrayList<>();

        if (values == null || values.isEmpty()) {
            userRoles.add(UserRole.USER);
            return userRoles;
        }

        for (String value : values) {
            if (value == null || value.isBlank()) {
                continue;
            }

            for (UserRole role : UserRole.values()) {
                if (role.name().equalsIgnoreCase(value)
                        || role.getValue().equalsIgnoreCase(value)) {
                    if (!userRoles.contains(role)) {
                        userRoles.add(role);
                    }
                    break;
                }
            }
        }

        if (userRoles.isEmpty()) {
            throw new IllegalArgumentException("Invalid roles: " + values);
        }

        return userRoles;
    }

    public List<String> mapUserRoleToUserVo(List<UserRole> userRoles) {
        return userRoles == null
                ? List.of()
                : userRoles.stream().map(UserRole::name).toList();
    }

}
