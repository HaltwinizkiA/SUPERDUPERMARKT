package com.haltwinizki.service;

import com.haltwinizki.products.Product;

import java.util.List;

public interface ProductService {
    Product create(Product productInfoDto);

    Product read(long id);

    Product delete(long id);

    Product update(Product product);

    List<Product> getAllProducts();

    List<Product> getDiscardedProducts();

    void changeQuality();
}