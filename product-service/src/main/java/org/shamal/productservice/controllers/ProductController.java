package org.shamal.productservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.shamal.productservice.entities.Product;
import org.shamal.productservice.exceptions.ProductNotFoundException;
import org.shamal.productservice.exceptions.ProductValidationError;
import org.shamal.productservice.models.dtos.CartItemResponseDto;
import org.shamal.productservice.models.dtos.ItemDto;
import org.shamal.productservice.models.dtos.ProductDto;
import org.shamal.productservice.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@Tag(name = "Product",description = "Product for users APIs")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/get-products")
    @Operation(
            summary = "Get all active products",
            description = "Get all active products",
            tags = {"products","get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "500")
    })
    public ResponseEntity<List<ProductDto>> getProducts(){
        log.info("fetching every product details");
        try{
            List<Product> productList = productService.getAllProducts();
            if(productList.isEmpty()){
                log.error("No products in db");
                return ResponseEntity.notFound().build();
            }
            List<ProductDto> productDtoList = new ArrayList<>();
            productList.forEach(product -> productDtoList.add(new ProductDto(product)));
            return ResponseEntity.ok(productDtoList);
        }
        catch (ProductValidationError e){
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping("/get-product/{productId}")
    @Operation(
            description = "Get product by product Id",
            summary = "Get product by product id",
            tags = {"products","get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "500")
    })
    public ResponseEntity<ProductDto> getProduct(@PathVariable String productId){
        try{
            Product product = productService
                    .getProductByProductId(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product not found with given ID"));
            return ResponseEntity.ok(new ProductDto(product));
        }
        catch (ProductNotFoundException e){
            log.error("Product not found");
            return ResponseEntity.notFound().build();
        }
        catch (Exception e){
            log.error("Something went wrong while fetching the product");
            return ResponseEntity.internalServerError().build();
        }
    }



    @GetMapping("/verify-product/{productId}")
    @Operation(
            summary = "Verify if product exists by productID",
            tags = {"products","get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "500")
    })
    public boolean verifyProduct(@PathVariable String productId){
        try {
            return productService.verifyProductId(UUID.fromString(productId));
        }
        catch (Exception e){
            log.error("Something went wrong while processing productId validation");
            throw new RuntimeException("Something went wrong while processing productId validation");
        }
    }

    @PostMapping("/get-products-from-list-of-cart-items")
    public List<CartItemResponseDto> getProductsFromListOfCartItems(
            @RequestBody List<ItemDto> items){
        if(items.isEmpty()){
            log.error("No items in request body");
            throw new ProductNotFoundException("No items in RequestBody");
        }
        List<CartItemResponseDto> cartItemsList = new ArrayList<>();
        try{
            cartItemsList = productService.getProductInfoFromCartItem(items);
        }
        catch (Exception e){
            log.error("Something went wrong with getting product info from cart item list");
            throw new RuntimeException("Something went wrong while processing cart items");
        }
        return cartItemsList;
    }


}
