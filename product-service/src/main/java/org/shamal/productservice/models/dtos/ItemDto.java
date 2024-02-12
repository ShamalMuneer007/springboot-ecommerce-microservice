package org.shamal.productservice.models.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private String productId;
    private Long quantity;

}
