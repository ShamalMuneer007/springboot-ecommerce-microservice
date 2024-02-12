package org.shamal.cartservice.models.dtos;

import org.shamal.cartservice.entities.Item;

import java.util.List;

public class CartRequestDto {
    private List<Item> items;

    public CartRequestDto(CartDto cartDto){
        this.items = cartDto.getItems();
    }
}
