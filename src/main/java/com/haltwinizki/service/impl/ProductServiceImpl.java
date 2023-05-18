package com.haltwinizki.service.impl;

import com.haltwinizki.products.Product;
import com.haltwinizki.repository.ProductRepository;
import com.haltwinizki.repository.impl.LocalProductRepository;
import com.haltwinizki.service.ProductService;

import java.util.List;

public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository = new LocalProductRepository();

    @Override
    public Product create(Product product) {
        return productRepository.create(product);
    }

    @Override
    public Product read(long id) {
        return productRepository.read(id);
    }

    @Override
    public Product delete(long id) {
        return productRepository.delete(id);
    }

    @Override
    public Product update(Product product) {
        return productRepository.update(product);
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
    public void changeQuality() {
        for (Product product : getAllProducts()) {
            product.changeQuality();
        }
    }
}
