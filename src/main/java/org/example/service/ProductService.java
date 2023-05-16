package org.example.service;

import org.example.products.Product;

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