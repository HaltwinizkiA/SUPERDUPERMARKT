package org.example.repository;

import org.example.products.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> getAllProducts();

    Product removeProduct(long id);

    Product update(Product productToUpdate);

    Product create(Product product);

    Product get(long id);

    List<Product> getDiscardedProducts();
}
