package org.shamal.productservice.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.shamal.productservice.proxies.cart.CartFeignProxy;
import org.shamal.productservice.entities.Product;
import org.shamal.productservice.exceptions.ProductValidationError;
import org.shamal.productservice.models.dtos.CartItemResponseDto;
import org.shamal.productservice.models.dtos.ItemDto;
import org.shamal.productservice.models.dtos.ProductDto;
import org.shamal.productservice.repository.ProductRepository;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final CartFeignProxy cartFeignProxy;

    public ProductServiceImpl(ProductRepository productRepository, CartFeignProxy cartFeignProxy) {
        this.productRepository = productRepository;
        this.cartFeignProxy = cartFeignProxy;
    }

    @Override
    public List<Product> getAllProducts() {
        try {
            return productRepository.findAll();
        }
        catch (Exception e){
            log.error("Something went wrong while fetching products",e);
            throw new
                    DataAccessResourceFailureException(
                            "Something went wrong while accessing product data from database");
        }
    }

    @Override
    @Transactional
    public void addProduct(ProductDto productDto) {
        validateProductDto(productDto);
        try {
            Product product = new Product();
            product.setProductName(productDto.getProductName());
            product.setProductPrice(productDto.getProductPrice());
            product.setProductImageUrl(productDto.getProductImageUrl());
            productRepository.save(product);
        }
        catch (Exception e){
            log.error("Something went wrong while saving the product");
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Transactional
    @CircuitBreaker(name = "deleteProduct",fallbackMethod = "failureFallback")
    public void deleteProduct(String productId,String authorizationHeader) {
        if(!productRepository.existsById(UUID.fromString(productId))){
            log.error("Product does not exists");
            throw new RuntimeException("Product does not exists");
        }
        try {
          productRepository.deleteById(UUID.fromString(productId));
          cartFeignProxy.removeProduct(productId,authorizationHeader);
        }
        catch (Exception e){
            log.error("something went wrong while deleting the product",e);
            throw new RuntimeException("Something went wrong while deleting the product");
        }
    }

    @Override
    public Optional<Product> getProductByProductId(String productId) {
        return productRepository.findById(UUID.fromString(productId));
    }

    @Override
    public boolean verifyProductId(UUID productId) {
        return productRepository.existsById(productId);
    }

    @Override
    public List<CartItemResponseDto> getProductInfoFromCartItem(List<ItemDto> items) {
        List<CartItemResponseDto> cartItemsList = new ArrayList<>();
        try{
            items.forEach(item -> {
                cartItemsList.add(CartItemResponseDto.builder()
                                .quantity(item.getQuantity())
                                .productId(item.getProductId())
                                .build());
            });
            List<UUID> productIds = new ArrayList<>();
            items.forEach(item -> {
                productIds.add(UUID.fromString(item.getProductId()));
            });
            List<Product> productList = productRepository.findByProductIdIn(productIds);
            productList.forEach(product -> {
                cartItemsList.forEach(cartItem -> {
                    if(product.getProductId().toString().equals(cartItem.getProductId())){
                        cartItem.setProductName(product.getProductName());
                        cartItem.setProductImageUrl(product.getProductImageUrl());
                    }
                });
            });
            return cartItemsList;
        }
        catch (Exception e){
            log.error("Something went wrong while processing cart items");
            throw new RuntimeException();
        }
    }

    private void validateProductDto(ProductDto productDto) {

        if(productRepository.existsByProductName(productDto.getProductName())){
            log.error("Product name already exists");
            throw new ProductValidationError("product already exists with this name");
        }

        if(productDto.getProductName().isEmpty()) {
            log.error("Product name is empty");
            throw new ProductValidationError("Product name is required");
        }

        if(productDto.getProductPrice() == null) {
            log.error("Product price is required");
            throw new ProductValidationError("Product price is required");
        }

        if(productDto.getProductPrice()<0) {
            log.error("Product price cannot be negative");
            throw new ProductValidationError("Price cannot be negative");
        }
    }
    //Failure fallback method
    public void failureFallback(Exception e){
        log.warn("Too many request..Timing out");
        log.warn("Error : {}",e.getMessage());
    }
}
