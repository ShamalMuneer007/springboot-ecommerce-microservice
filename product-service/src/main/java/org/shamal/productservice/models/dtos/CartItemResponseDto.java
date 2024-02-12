package org.shamal.productservice.models.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemResponseDto {
    private String productId;
    private String productName;
    private String productImageUrl;
    private Long quantity;
}
