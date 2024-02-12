package org.shamal.cartservice.controllers;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.shamal.cartservice.proxies.product.ProductFeignProxy;
import org.shamal.cartservice.entities.Cart;
import org.shamal.cartservice.entities.Item;
import org.shamal.cartservice.models.dtos.CartDto;
import org.shamal.cartservice.models.dtos.CartItemResponse;
import org.shamal.cartservice.services.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class CartController {
    private final CartService cartService;
    private final ProductFeignProxy productFeignProxy;

    public CartController(CartService cartService, ProductFeignProxy productFeignProxy) {
        this.cartService = cartService;
        this.productFeignProxy = productFeignProxy;
    }
    @GetMapping("/get-cart-items")
//    @CircuitBreaker()
    public ResponseEntity<List<CartItemResponse>> getCartItems(HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        log.info("getting cart items for {}",userId);
        CartDto cartDto;
        try {
            Cart cart = cartService.getCartItemsFromUser(UUID.fromString(userId));
            if(cart == null){
                return ResponseEntity.status(404).build();
            }
            List<CartItemResponse> cartItemResponse = productFeignProxy
                    .getProductsFromListOfCartItems(cart.getItems(),
                            request.getHeader("Authorization")).getBody();

            return ResponseEntity.ok(cartItemResponse);
        }
        catch (Exception e){
            log.error("Something went wrong while fetching the cart details of the user : {}",e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    @PostMapping("/insert-item/{productId}")
    public ResponseEntity<String> insertItemToTheCart(@PathVariable UUID productId,
                                                      HttpServletRequest request){
        try {
            ResponseEntity<Boolean> verifyProductResponse =
                    productFeignProxy.verifyProduct(productId, request.getHeader("Authorization"));
            if (Boolean.FALSE.equals(verifyProductResponse.getBody())) {
                return ResponseEntity.badRequest().body("There exists no product with provided productId");
            }
            String userId = (String) request.getAttribute("userId");
            cartService.addItemToTheCart(productId, UUID.fromString(userId));
            return ResponseEntity.ok("Item inserted to the cart successfully");
        }
        catch (Exception e){
            log.error("Something went wrong while adding product to the cart \n ERROR : {}",e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    @DeleteMapping("/remove-item/{productId}")
    public ResponseEntity<String> removeItemFromCart(HttpServletRequest request,
                                                     @PathVariable UUID productId){
        try {
            ResponseEntity<Boolean> verifyProductResponse =
                    productFeignProxy.verifyProduct(productId, request.getHeader("Authorization"));
            if (Boolean.FALSE.equals(verifyProductResponse.getBody())) {
                return ResponseEntity.badRequest().build();
            }
            String userId = (String) request.getAttribute("userId");
            cartService.removeItemFromCart(productId, UUID.fromString(userId));
            return ResponseEntity.ok("Item removed from the cart successfully");
        }
        catch (Exception e){
            log.error("Something went wrong while removing product to the cart \n ERROR : {}",e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/change-quantity")
    @Operation(
            summary = "Changes the product quantity",
            tags = {"products","get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content =
                            { @Content(schema = @Schema(implementation = CartDto.class),
                                    mediaType = "application/json") }),
            @ApiResponse(responseCode = "500",
                    content =
                            { @Content(schema = @Schema()) }) })
    public ResponseEntity<String> changeItemQuantity(HttpServletRequest request,
                                                     @RequestBody Item item){
        try {
            ResponseEntity<Boolean> verifyProductResponse =
                    productFeignProxy.verifyProduct(item.getProductId(), request.getHeader("Authorization"));
            if (Boolean.FALSE.equals(verifyProductResponse.getBody())) {
                return ResponseEntity.badRequest().build();
            }
            String userId = (String) request.getAttribute("userId");
            cartService.changeItemQuantity(item, userId);
            return ResponseEntity.ok("quantity of the item in cart changed successfully");
        }
        catch (Exception e){
            log.error("Something went wrong while changing quantity of the product to the cart \n ERROR : {}",e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }
}
