package com.haltwinizki.repository;

import com.haltwinizki.products.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> getAllProducts();

    Product removeProduct(long id);

    Product update(Product productToUpdate);

    Product create(Product product);

    Product getById(long id);

    List<Product> getDiscardedProducts();
}
