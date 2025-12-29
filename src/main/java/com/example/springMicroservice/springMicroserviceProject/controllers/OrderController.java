package com.example.springMicroservice.springMicroserviceProject.controllers;

import com.example.springMicroservice.springMicroserviceProject.models.OrderVo;
import com.example.springMicroservice.springMicroserviceProject.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private  final OrderService orderService;

    @PostMapping("/save")
    public ResponseEntity<OrderVo> saveCart(@RequestParam("X-User-ID") UUID userId) {
        return new ResponseEntity<>(orderService.saveOrder(userId), HttpStatus.CREATED);
    }
}
