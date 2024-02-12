package org.shamal.cartservice.controllers;

import lombok.extern.slf4j.Slf4j;
import org.shamal.cartservice.services.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/api/v1/admin")
public class CartAdminController {
    private final CartService cartService;

    public CartAdminController(CartService cartService) {
        this.cartService = cartService;
    }

    @DeleteMapping("/remove-product/{productId}")
    ResponseEntity<String> removeProduct(@PathVariable String productId){
        try{
            log.error("Processing request to remove the product from every cart");
            cartService.removeItemFromEveryCart(UUID.fromString(productId));
            return ResponseEntity.ok("Product Delete-d from cart successfully");
        }
        catch (Exception e){
            log.error("Something went wrong while deleting the products from carts");
            return ResponseEntity.internalServerError().build();
        }
    }
}
