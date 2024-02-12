package org.shamal.productservice.controllers;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.shamal.productservice.exceptions.ProductValidationError;
import org.shamal.productservice.models.dtos.ProductDto;
import org.shamal.productservice.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@Tag(name = "Product",description = "Admin Product Management APIs")
@Slf4j
public class AdminProductController {
    @Autowired
    ProductService productService;

    @PostMapping("/add-product")
    @Operation(
            summary = "Add product",
            tags = {"products","post"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "500")
    })
    public ResponseEntity<String> addProduct(@RequestBody ProductDto productDto){
        try{
            productService.addProduct(productDto);
        }
        catch (ProductValidationError e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body("Something went wrong while saving the product");
        }
        return ResponseEntity.ok("Product added successfully!");
    }

    @DeleteMapping("/delete-product/{productId}")
    @Retry(name = "fetch",fallbackMethod = "failureFallback")
    @CircuitBreaker(name = "deleteProduct",fallbackMethod = "failureFallback")
    @RateLimiter(name = "fetch",fallbackMethod = "failureFallback")
    @Operation(
            summary = "Delete product",
            tags = {"products","delete"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "500")
    })
    public ResponseEntity<String> deleteProduct(@PathVariable String productId,
                                                HttpServletRequest request){
        try{
            productService.deleteProduct(productId,request.getHeader("Authorization"));
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body("Something went wrong while saving the product");
        }
        return ResponseEntity.ok("Product deleted successfully!");
    }

    public ResponseEntity<String> failureFallback(Exception e) {
        // Log the error
        log.error("Error deleting product", e);
        return ResponseEntity.internalServerError().body("Something went wrong while deleting the product");
    }
}
