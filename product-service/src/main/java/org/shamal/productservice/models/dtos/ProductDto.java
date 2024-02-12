package org.shamal.productservice.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.shamal.productservice.entities.Product;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private String productId;
    private String productName;
    private String productImageUrl;
    private Double productPrice;
    public ProductDto(Product product){
        this.productId = product.getProductId().toString();
        this.productName = product.getProductName();
        this.productImageUrl = product.getProductImageUrl();
        this.productPrice = product.getProductPrice();
    }

}
