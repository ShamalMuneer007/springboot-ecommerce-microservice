package org.shamal.cartservice.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.shamal.cartservice.entities.Cart;
import org.shamal.cartservice.entities.Item;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private UUID cartId;
    private UUID userId;
    private List<Item> items;

     public CartDto(Cart cart){
         this.cartId = getCartId();
         this.userId = getUserId();
         this.items = getItems();
     }
}
