package org.shamal.cartservice.proxies.product;

import org.shamal.cartservice.entities.Item;
import org.shamal.cartservice.models.dtos.CartItemResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@FeignClient(name = "inventory",path = "/inventory")
public interface ProductFeignProxy {
    @GetMapping("/api/v1/verify-product/{productId}")
    ResponseEntity<Boolean> verifyProduct(@PathVariable(name = "productId")UUID productId,
                                 @RequestHeader("Authorization") String authorizationHeader);
    @PostMapping("/api/v1/get-products-from-list-of-cart-items")
    ResponseEntity<List<CartItemResponse>> getProductsFromListOfCartItems(@RequestBody Set<Item> items,
                                                          @RequestHeader("Authorization") String authorizationHeader);
}












