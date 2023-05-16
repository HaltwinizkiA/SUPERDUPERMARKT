package com.haltwinizki.service;

import com.haltwinizki.products.Product;

import java.util.List;

public interface ProductService {
    Product create(Product productInfoDto);

    Product get(long id);

    Product remove(long id);

    Product update(Product product);

    boolean checkExpiration(Product product);

    List<Product> getAllProducts();

    List<Product> getDiscardedProducts();

    void qualityChange();


}