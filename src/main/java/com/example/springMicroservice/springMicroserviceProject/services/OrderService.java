package com.example.springMicroservice.springMicroserviceProject.services;

import com.example.springMicroservice.springMicroserviceProject.entity.*;
import com.example.springMicroservice.springMicroserviceProject.models.OrderItemVo;
import com.example.springMicroservice.springMicroserviceProject.models.OrderVo;
import com.example.springMicroservice.springMicroserviceProject.models.ProductsVo;
import com.example.springMicroservice.springMicroserviceProject.models.UserVo;
import com.example.springMicroservice.springMicroserviceProject.respository.CartItemRepository;
import com.example.springMicroservice.springMicroserviceProject.respository.OrderRepository;
import com.example.springMicroservice.springMicroserviceProject.respository.ProductsRepository;
import com.example.springMicroservice.springMicroserviceProject.respository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

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
        ObjectMapper mapper = new ObjectMapper();
        OrderVo orderVo = mapper.convertValue(order, OrderVo.class);
        orderVo.setUser(mapper.convertValue(order.getUser(), UserVo.class));
        orderVo.setOrderItems(
                order.getOrderItems().stream()
                        .map(item -> {
                            OrderItemVo vo = mapper.convertValue(item, OrderItemVo.class);
                            vo.setProducts(mapper.convertValue(item.getProducts(), ProductsVo.class));
                            return vo;
                        })
                        .toList()
        );
        orderVo.setResponse("Your order has been approved");

        return orderVo;
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

}
