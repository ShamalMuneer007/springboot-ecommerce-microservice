package org.shamal.cartservice.services;

import org.shamal.cartservice.entities.Cart;
import org.shamal.cartservice.entities.Item;
import org.shamal.cartservice.models.dtos.CartDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface CartService {


    Cart getCartItemsFromUser(UUID userId);
    void addItemToTheCart(UUID productId, UUID userId);

    void removeItemFromCart(UUID productId, UUID userId);

    void changeItemQuantity(Item item, String userId);

    void removeItemFromEveryCart(UUID productId);
}
