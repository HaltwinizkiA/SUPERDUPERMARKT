package com.haltwinizki.service.impl;

import com.haltwinizki.products.Product;
import com.haltwinizki.repository.ProductRepository;
import com.haltwinizki.repository.impl.LocalProductRepository;
import com.haltwinizki.service.ProductService;

import java.util.Date;
import java.util.List;

public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository = new LocalProductRepository();

    @Override
    public Product create(Product product) {
        return productRepository.create(product);
    }

    @Override
    public Product get(long id) {
        return productRepository.get(id);
    }

    @Override
    public Product remove(long id) {
        Product product = productRepository.get(id);
        productRepository.getDiscardedProducts().add(product);
        return productRepository.removeProduct(id);
    }

    @Override
    public Product update(Product product) {
        return productRepository.update(product);
    }

    @Override
    public boolean checkExpiration(Product product) {
        return (product.getExpirationDate() == null || new Date().before(product.getExpirationDate())) && !product.isSpoiled();
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }

    @Override
    public List<Product> getDiscardedProducts() {
        return productRepository.getDiscardedProducts();
    }

    @Override
    public void qualityChange() {
        for (Product product : getAllProducts()) {
            product.qualityChange();
        }
    }
}
