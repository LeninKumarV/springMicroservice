package com.example.springMicroservice.springMicroserviceProject.controllers;


import com.example.springMicroservice.springMicroserviceProject.models.CartItemVo;
import com.example.springMicroservice.springMicroserviceProject.services.CartItemsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cart-items")
@RequiredArgsConstructor
public class CartItemsController {

    private final CartItemsService cartItemsService;

    @PostMapping("/save")
    public ResponseEntity<CartItemVo> saveCart(@RequestBody CartItemVo cartItemVo) {
        return new ResponseEntity<>(cartItemsService.saveCartItem(cartItemVo), HttpStatus.CREATED);
    }

    @PostMapping("/delete")
    public ResponseEntity<CartItemVo> deleteCartItem(@RequestBody CartItemVo cartItemVo) {
        return new ResponseEntity<>(cartItemsService.deleteCartItem(cartItemVo), HttpStatus.OK);
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<CartItemVo>> getCartItem(@RequestParam("X-User-ID") UUID userId) {
        return new ResponseEntity<>(cartItemsService.getAllCartItem(userId), HttpStatus.OK);
    }

}
