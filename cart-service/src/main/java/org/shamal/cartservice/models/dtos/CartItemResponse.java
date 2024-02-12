package org.shamal.cartservice.models.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartItemResponse {
    private String productImageUrl;
    private String productName;
    private Long quantity;
}
