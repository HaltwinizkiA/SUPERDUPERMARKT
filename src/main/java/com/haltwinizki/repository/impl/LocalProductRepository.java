package com.haltwinizki.repository.impl;

import com.haltwinizki.local.LocaleProductsBase;
import com.haltwinizki.products.Product;
import com.haltwinizki.repository.ProductRepository;
import com.haltwinizki.worker.FileWorker;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Objects;

public class LocalProductRepository implements ProductRepository {
    private final LocaleProductsBase localeProductsBase = LocaleProductsBase.getInstance();

    @Override
    public List<Product> getAllProducts() {
        return localeProductsBase.getProductsList();
    }

    @Override
    public Product delete(long id) {
        Product removedProduct = read(id);
        localeProductsBase.getProductsList().removeIf(product -> Objects.equals(id, product.getId()));
        localeProductsBase.getDiscardedProducts().add(removedProduct);
        localeProductsBase.save();
        return removedProduct;
    }

    @Override
    public Product update(Product updatedProduct) {
        Product productInDb = localeProductsBase.getProductsList().stream().filter(product -> product.getId() == updatedProduct.getId()).findFirst().orElse(null);
        if (productInDb == null) {
            return null;
        }
        if (updatedProduct.getQuality() != null && productInDb.getQuality() != updatedProduct.getQuality()) {
            productInDb.getQuality().set(updatedProduct.getQuality().get());
        }
        if (updatedProduct.getExpirationDate() != null && !updatedProduct.getExpirationDate().equals(productInDb.getExpirationDate())) {
            productInDb.setExpirationDate(updatedProduct.getExpirationDate());
        }
        if (updatedProduct.getName() != null && !productInDb.getName().equals(updatedProduct.getName())) {
            productInDb.setName(updatedProduct.getName());
        }
        if (productInDb.getPrice() != updatedProduct.getPrice()) {
            productInDb.setPrice(updatedProduct.getPrice());
        }
        localeProductsBase.save();
        return productInDb;
    }

    @Override
    public Product create(Product product) {
        product.setId(localeProductsBase.getMaxId().incrementAndGet());
        localeProductsBase.getProductsList().add(product);
        localeProductsBase.save();
        return product;
    }

    @Override
    public Product read(long id) {
        return localeProductsBase.getProductsList().stream().filter(product -> product.getId() == id).findFirst().map(Product::clone).orElse(null);
    }

    @Override
    public List<Product> getDiscardedProducts() {
        return localeProductsBase.getDiscardedProducts();
    }
}
