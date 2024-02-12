package org.shamal.productservice.proxies.cart;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

@Slf4j
public class CartFeignFallback implements CartFeignProxy{
    @Override
    public ResponseEntity<String> removeProduct(String productId, String request) {
        log.error("Something went wrong while sending removing products from product service");
        return ResponseEntity
                .internalServerError()
                .body("Something went wrong while sending removing products from product service");
    }
}
