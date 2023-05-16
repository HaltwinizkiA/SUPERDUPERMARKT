package org.example.service.impl;

import org.example.products.Product;
import org.example.repository.ProductRepository;
import org.example.repository.impl.LocalProductRepository;
import org.example.service.ProductService;

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
        return productRepository.removeProduct(id);
    }

    @Override
    public Product update(Product product) {
        return productRepository.update(product);
    }

    @Override
    public boolean checkExpirationDate(Product product) {
        return product.getExpirationDate() == null || new Date().before(product.getExpirationDate());
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
        getAllProducts().stream().forEach(product->qualityChange());
    }
}
