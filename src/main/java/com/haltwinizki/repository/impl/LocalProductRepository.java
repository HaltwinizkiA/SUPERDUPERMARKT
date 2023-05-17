package com.haltwinizki.repository.impl;

import com.haltwinizki.local.LocaleProductsBase;
import com.haltwinizki.products.Product;
import com.haltwinizki.repository.ProductRepository;
import com.haltwinizki.worker.FileWorker;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Objects;

public class LocalProductRepository implements ProductRepository {
    private static final Logger log = Logger.getLogger(FileWorker.class);

    LocaleProductsBase localeProductsBase = LocaleProductsBase.getInstance();

    @Override
    public List<Product> getAllProducts() {
        return localeProductsBase.getProductsList();
    }

    @Override
    public Product delete(long id) {
        Product removedProduct = get(id);
        localeProductsBase.getProductsList().removeIf(product -> Objects.equals(id, product.getId()));
        localeProductsBase.save();
        return removedProduct;
    }

    @Override
    public Product update(Product updatedProduct) {
        Product productInDb = localeProductsBase.getProductsList().stream().filter(product -> product.getId() == updatedProduct.getId()).findFirst().orElse(null);
        if (productInDb.getQuality() != updatedProduct.getQuality()) {
            productInDb.getQuality().set(updatedProduct.getQuality().get());
        }
        if (updatedProduct.getExpirationDate() != null && !productInDb.getExpirationDate().equals(updatedProduct.getExpirationDate())) {
            productInDb.setExpirationDate(updatedProduct.getExpirationDate());
        }
        if (!productInDb.getName().equals(updatedProduct.getName())) {
            productInDb.setName(updatedProduct.getName());
        }
        if (productInDb.getPrice() != updatedProduct.getPrice()) {
            productInDb.setPrice(updatedProduct.getPrice());
        }
        localeProductsBase.save();
        return get(updatedProduct.getId());
    }

    @Override
    public Product create(Product product) {
        product.setId(localeProductsBase.maxId.incrementAndGet());
        localeProductsBase.getProductsList().add(product);
        localeProductsBase.save();
        return product;
    }


    @Override
    public Product get(long id) {
        try {
            return localeProductsBase.getProductsList().stream().filter(product -> product.getId() == id).findFirst().orElse(null).clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Override
    public List<Product> getDiscardedProducts() {
        return localeProductsBase.getDiscardedProducts();
    }
}
