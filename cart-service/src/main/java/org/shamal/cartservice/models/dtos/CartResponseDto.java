package org.shamal.cartservice.models.dtos;

import org.shamal.cartservice.entities.Item;

import java.util.List;

public class CartResponseDto {
    private List<Item> items;

    public CartResponseDto(CartDto cartDto){
        this.items = cartDto.getItems();
    }
}
