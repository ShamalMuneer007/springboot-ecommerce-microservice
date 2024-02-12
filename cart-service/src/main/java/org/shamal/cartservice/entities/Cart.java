package org.shamal.cartservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Entity
@Getter
@Setter
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cartId;
    private UUID userId;
    @ElementCollection
    @Column(name="items")
    private Set<Item> items = new HashSet<>();
}

