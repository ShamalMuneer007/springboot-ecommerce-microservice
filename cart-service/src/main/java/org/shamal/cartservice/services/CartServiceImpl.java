package org.shamal.cartservice.services;

import lombok.extern.slf4j.Slf4j;
import org.shamal.cartservice.entities.Cart;
import org.shamal.cartservice.entities.Item;
import org.shamal.cartservice.repositories.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Cart getCartItemsFromUser(UUID userId) {
        try {
            log.info("Fetching cart items");
            return cartRepository.findByUserId(userId);
        }
        catch (Exception e){
            log.error("Something went wrong while fetching user's cart from db");
            throw new RuntimeException("Something went wrong while fetching user's cart from db");
        }
    }

    @Override
    @Transactional
    public void addItemToTheCart(UUID productId, UUID userId) {
        try {
            Cart cart = getCart(userId);
            addItemToCart(cart,productId);
            cartRepository.save(cart);
        }
        catch (Exception e){
            log.error("Something went wrong while adding item to the cart");
            throw new RuntimeException("Something went wrong while adding item to the cart");
        }
    }

    @Override
    public void removeItemFromCart(UUID productId, UUID userId) {
        try {
            Cart cart = getCart(userId);
            removeItemFromCart(cart, productId);
            cartRepository.save(cart);
        }
        catch (Exception e){
            throw new RuntimeException("Something went wrong while removing item from the cart");
        }
    }

    @Override
    public void changeItemQuantity(Item item, String userId) {
        try {
            Cart cart = getCart(UUID.fromString(userId));
            Item cartItem = cart.getItems().stream()
                    .filter(i -> i.getProductId().equals(item.getProductId()))
                    .findFirst()
                    .orElse(null);
            if (Objects.isNull(cartItem)) {
                cart.getItems().add(item);
            } else {
                cartItem.setQuantity(item.getQuantity());
            }
            cartRepository.save(cart);
        }
        catch (Exception e){
            log.error("Something went wrong while changing the items quantity");
            throw new RuntimeException("Something went wrong while changing the items quantity");
        }
    }

    @Override
    @Transactional
    public void removeItemFromEveryCart(UUID productId) {
        //Works if admin removes the product from the inventory
        List<Cart> carts = cartRepository.findAll();
        carts.forEach(cart ->
        {
            removeItemFromCart(cart,productId);
        });
        log.warn("Removed product");
        cartRepository.saveAll(carts);
    }

    private Cart getCart(UUID userId) {
        Cart cart = cartRepository.findByUserId(userId);
        //If cart is empty : add new cart for the userId
        if(cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
            cart.setCartId(UUID.randomUUID());
        }
        return cart;
    }

    private void addItemToCart(Cart cart, UUID productId) {
        //If item is already present : Add the 1 quantity of the item in cart  Else add a new item to the cart
        Item item = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .orElse(new Item(productId, 0L));
        item.setQuantity(item.getQuantity() + 1);
        if(item.getQuantity() == 1) {
            cart.getItems().add(item);
        }
    }

    private void removeItemFromCart(Cart cart, UUID productId) {
        cart.getItems().removeIf(item -> item.getProductId().equals(productId));
    }
}
