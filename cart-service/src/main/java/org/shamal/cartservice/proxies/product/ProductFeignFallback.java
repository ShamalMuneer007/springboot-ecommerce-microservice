package org.shamal.cartservice.proxies.product;

import lombok.extern.slf4j.Slf4j;
import org.shamal.cartservice.entities.Item;
import org.shamal.cartservice.models.dtos.CartItemResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
public class ProductFeignFallback implements ProductFeignProxy{
    @Override
    public ResponseEntity<Boolean> verifyProduct(UUID productId, String authorizationHeader) {
        return ResponseEntity.internalServerError().build();
    }

    @Override
    public ResponseEntity<List<CartItemResponse>> getProductsFromListOfCartItems(Set<Item> items, String authorizationHeader) {
        log.warn("There are no items in cart");
//        throw new NoItemsInCart("There are no items in cart");
        return ResponseEntity.badRequest().build();
    }
}
