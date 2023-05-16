package com.haltwinizki.repository.impl;

import com.haltwinizki.local.LocaleProductsBase;
import com.haltwinizki.products.Product;
import com.haltwinizki.repository.ProductRepository;

import java.util.List;

public class LocalProductRepository implements ProductRepository {

    @Override
    public List<Product> getAllProducts() {
        return LocaleProductsBase.getInstance().getProductsList();
    }

    @Override
    public Product removeProduct(long id) {
        Product removedProduct = getById(id);
        LocaleProductsBase.getInstance().getProductsList().remove(removedProduct);
        return removedProduct;
    }

    @Override
    public Product update(Product product) {
        Product toRemove = getById(product.getId()); //todo
        LocaleProductsBase.getInstance().getProductsList().remove(toRemove);
        LocaleProductsBase.getInstance().getProductsList().add(product);
        LocaleProductsBase.getInstance().save();
        return product;
    }

    @Override
    public Product create(Product product) {
        product.setId(getFreeId());
        LocaleProductsBase.getInstance().getProductsList().add(product);
        LocaleProductsBase.getInstance().save();
        return product;
    }

    public long getFreeId() {
        long id = 0;
        for (Product product : LocaleProductsBase.getInstance().getProductsList()) {
            if (id < product.getId()) {
                id = product.getId();
            }
        }
        id++;
        return id;
    }

    @Override
    public Product getById(long id) {
        Product productGet = null;
        for (Product product : LocaleProductsBase.getInstance().getProductsList()) {
            if (product.getId() == id) {
                productGet = product;
            }
        }

        return productGet;
    }

    @Override
    public List<Product> getDiscardedProducts() {
        return LocaleProductsBase.getInstance().getDiscardedProducts();
    }
}
