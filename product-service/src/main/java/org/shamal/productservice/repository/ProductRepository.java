package org.shamal.productservice.repository;

import org.shamal.productservice.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    boolean existsByProductName(String productName);
    List<Product> findByProductIdIn(List<UUID> productIds);
}