package org.shamal.productservice.proxies.cart;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "cart",path = "/cart",fallback = CartFeignFallback.class)
public interface CartFeignProxy {
    @DeleteMapping("/api/v1/admin/remove-product/{productId}")
    ResponseEntity<String> removeProduct(@PathVariable String productId,
                                         @RequestHeader("Authorization") String request);
}
