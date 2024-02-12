package org.shamal.productservice.services;

import org.shamal.productservice.entities.Product;
import org.shamal.productservice.models.dtos.CartItemResponseDto;
import org.shamal.productservice.models.dtos.ItemDto;
import org.shamal.productservice.models.dtos.ProductDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {
    List<Product> getAllProducts();

    void addProduct(ProductDto productDto);

    void deleteProduct(String productId,String authorizationHeader);

    Optional<Product> getProductByProductId(String productId);

    boolean verifyProductId(UUID productId);

    List<CartItemResponseDto> getProductInfoFromCartItem(List<ItemDto> items);
}
