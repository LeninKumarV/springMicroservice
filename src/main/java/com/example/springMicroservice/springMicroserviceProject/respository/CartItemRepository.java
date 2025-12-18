package com.example.springMicroservice.springMicroserviceProject.respository;

import com.example.springMicroservice.springMicroserviceProject.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {

    @Query("""
           SELECT c
           FROM CartItem c
           WHERE c.user.userId = :userId
             AND c.products.productId = :productId
             AND c.products.isActive = true
           """)
    Optional<CartItem> findCartItemByUserAndProduct(
            @Param("userId") UUID userId,
            @Param("productId") UUID productId
    );

    @Query("""
           SELECT c
           FROM CartItem c
           WHERE c.user.userId = :userId
           """)
    List<CartItem> findAllCartItemsByUser(
            @Param("userId") UUID userId
    );

    @Modifying
    @Transactional
    @Query("""
           DELETE
           FROM CartItem c
           WHERE c.user.userId = :userId
             AND c.products.productId = :productId
           """)
    int deleteCartItemByUserAndProduct(
            @Param("userId") UUID userId,
            @Param("productId") UUID productId
    );
}


