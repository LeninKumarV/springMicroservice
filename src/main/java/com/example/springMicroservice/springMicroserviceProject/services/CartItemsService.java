package com.example.springMicroservice.springMicroserviceProject.services;

import com.example.springMicroservice.springMicroserviceProject.entity.CartItem;
import com.example.springMicroservice.springMicroserviceProject.entity.Products;
import com.example.springMicroservice.springMicroserviceProject.entity.User;
import com.example.springMicroservice.springMicroserviceProject.models.CartItemVo;
import com.example.springMicroservice.springMicroserviceProject.models.ProductsVo;
import com.example.springMicroservice.springMicroserviceProject.models.UserVo;
import com.example.springMicroservice.springMicroserviceProject.respository.CartItemRepository;
import com.example.springMicroservice.springMicroserviceProject.respository.ProductsRepository;
import com.example.springMicroservice.springMicroserviceProject.respository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartItemsService {

    private final ProductsRepository productsRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final ObjectMapper mapper;

    @Transactional
    public CartItemVo saveCartItem(CartItemVo vo) {

        User user = userRepository.findById(vo.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Products product = productsRepository.findById(vo.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStockQuantity().compareTo(vo.getQuantity()) < 0) {
            return CartItemVo.builder()
                    .response("Selected product stock is not available")
                    .build();
        }

        return cartItemRepository
                .findCartItemByUserAndProduct(
                        user.getUserId(),
                        product.getProductId()
                )
                .map(cartItem -> {

                    // update quantity
                    BigInteger updatedQty =
                            cartItem.getQuantity().add(vo.getQuantity());
                    cartItem.setQuantity(updatedQty);

                    // update total price
                    BigDecimal additionalPrice =
                            product.getPrice().multiply(new BigDecimal(vo.getQuantity()));
                    cartItem.setPrice(cartItem.getPrice().add(additionalPrice));

                    cartItem.setUpdatedOn(LocalDateTime.now());

                    CartItem saved = cartItemRepository.save(cartItem);
                    return mapToVo(saved, "Cart Item Updated Successfully");
                })
                .orElseGet(() -> {
                    CartItem cartItem = CartItem.builder()
                            .cartItemId(vo.getCartItemId())
                            .user(user)
                            .products(product)
                            .price(
                                    product.getPrice()
                                            .multiply(new BigDecimal(vo.getQuantity()))
                            )
                            .quantity(vo.getQuantity())
                            .createdOn(LocalDateTime.now())
                            .updatedOn(LocalDateTime.now())
                            .build();

                    CartItem saved = cartItemRepository.save(cartItem);
                    return mapToVo(saved, "Cart Item Saved Successfully");
                });


    }

    public BigDecimal handlePrice(BigDecimal price, User user, Products product, CartItemVo cartItemVo) {
        return cartItemRepository
                .findCartItemByUserAndProduct(
                        user.getUserId(),
                        product.getProductId()
                )
                .map(existingCartItem -> {

                    // product already exists
                    BigInteger updatedQuantity = cartItemVo.getQuantity();
                    return price.multiply(new BigDecimal(updatedQuantity));
                })
                .orElse(price);
    }


    @Transactional
    public CartItemVo deleteCartItem(CartItemVo vo) {
        int deletedCount = cartItemRepository
                .deleteCartItemByUserAndProduct(
                        vo.getUserId(),
                        vo.getProductId()
                );
        if (deletedCount == 0) {
            throw new IllegalStateException("Cart item not found!");
        }
        return CartItemVo.builder()
                .userId(vo.getUserId())
                .productId(vo.getProductId())
                .response("Cart item deleted successfully")
                .build();
    }


    private CartItemVo mapToVo(CartItem cartItem, String response) {
        return CartItemVo.builder()
                .cartItemId(cartItem.getCartItemId())
                .user(mapper.convertValue(cartItem.getUser(), UserVo.class))
                .products(mapper.convertValue(cartItem.getProducts(), ProductsVo.class))
                .price(cartItem.getPrice())
                .quantity(cartItem.getQuantity())
                .response(response)
                .createdOn(cartItem.getCreatedOn())
                .updatedOn(cartItem.getUpdatedOn())
                .build();
    }

    public List<CartItemVo> getAllCartItem(UUID userId) {
        List<CartItem> cartItem = cartItemRepository.findAllCartItemsByUser(userId);
        return cartItem.stream().map( m -> mapToVo(m, null)).collect(Collectors.toList());
    }
}
