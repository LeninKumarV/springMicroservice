package com.example.springMicroservice.springMicroserviceProject.services;

import com.example.springMicroservice.springMicroserviceProject.entity.*;
import com.example.springMicroservice.springMicroserviceProject.models.*;
import com.example.springMicroservice.springMicroserviceProject.respository.CartItemRepository;
import com.example.springMicroservice.springMicroserviceProject.respository.OrderRepository;
import com.example.springMicroservice.springMicroserviceProject.respository.ProductsRepository;
import com.example.springMicroservice.springMicroserviceProject.respository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final ProductsRepository productsRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final ObjectMapper mapper;
    private final UserService userService;

    @Transactional
    public OrderVo saveOrder(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<CartItem> cartItems = cartItemRepository.findAllCartItemsByUser(userId);
        if(cartItems.isEmpty()){
            OrderVo.builder().response("You have nothing to order on your cart. Please add something.").build();
        }
        Order order = Order.builder()
                .user(user)
                .orderStatus(OrderStatus.PENDING)
                .createdOn(new Timestamp(System.currentTimeMillis()))
                .updatedOn(new Timestamp(System.currentTimeMillis()))
                .build();

        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> {
                    OrderItem item = mapToOrderItemsVo(cartItem);
                    item.setOrder(order);
                    return item;
                })
                .toList();

        order.setOrderItems(orderItems);
        order.setTotalAmount(calculateTotalPrice(orderItems));

        Order orderResponse = orderRepository.save(order);

        if (orderResponse.getOrderId() == null) {
            return OrderVo.builder()
                    .response("Something went wrong while placing the order")
                    .build();
        }

        cartItemRepository.deleteCartItemByUser(userId);

        return  mapToOrderVo(order);
    }

    private OrderItem mapToOrderItemsVo(CartItem cartItem) {
        return OrderItem.builder()
                .products(cartItem.getProducts())
                .price(cartItem.getPrice())
                .quantity(cartItem.getQuantity())
                .createdOn(cartItem.getCreatedOn())
                .updatedOn(cartItem.getUpdatedOn())
                .build();
    }

    private BigDecimal calculateTotalPrice(List<OrderItem> orderItems) {
        return
                orderItems.stream()
                        .map(OrderItem::getPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private OrderVo mapToOrderVo(Order order) {
        return OrderVo.builder()
                .orderId(order.getOrderId())
                .orderStatus(order.getOrderStatus())
                .totalAmount(order.getTotalAmount())
                .createdOn(order.getCreatedOn())
                .updatedOn(order.getUpdatedOn())
                .user(UserVo.builder()
                        .userId(order.getUser().getUserId())
                        .firstName(order.getUser().getFirstName())
                        .lastName(order.getUser().getLastName())
                        .email(order.getUser().getEmail())
                        .phoneNumber(order.getUser().getPhoneNumber())
                        .role(userService.mapUserRoleToUserVo(order.getUser().getRole()))
                        .address(mapper.convertValue(order.getUser().getAddress(), AddressVo.class))
                        .createdOn(order.getUser().getCreatedOn().toString())
                        .updatedOn(order.getUser().getUpdatedOn().toString())
                        .build())
                .orderItems(
                        order.getOrderItems().stream()
                                .map(item -> OrderItemVo.builder()
                                        .orderItemId(item.getOrderItemId())
                                        .price(item.getPrice())
                                        .quantity(item.getQuantity())
                                        .createdOn(item.getCreatedOn())
                                        .updatedOn(item.getUpdatedOn())
                                        .products(ProductsVo.builder()
                                                .productId(item.getProducts().getProductId())
                                                .productName(item.getProducts().getProductName())
                                                .description(item.getProducts().getDescription())
                                                .price(item.getProducts().getPrice())
                                                .stockQuantity(item.getProducts().getStockQuantity())
                                                .category(item.getProducts().getCategory())
                                                .imageUrl(item.getProducts().getImageUrl())
                                                .isActive(item.getProducts().getIsActive())
                                                .createdOn(item.getProducts().getCreatedOn().toString())
                                                .updatedOn(item.getProducts().getUpdatedOn().toString())
                                                .build())
                                        .build())
                                .toList()
                )
                .createdOn(order.getCreatedOn())
                .updatedOn(order.getUpdatedOn())
                .response("Your order has been approved")
                .build();
    }

}
