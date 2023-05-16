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
        Product removedProduct = get(id);
        for (Product product : LocaleProductsBase.getInstance().getProductsList()) {
            if (product.getId() == id) {
                LocaleProductsBase.getInstance().getProductsList().remove(product);
            }
        }
        return removedProduct;
    }

    @Override
    public Product update(Product productToUpdate) {
        for (Product product : LocaleProductsBase.getInstance().getProductsList()) {
            if (product.getId() == productToUpdate.getId()) {
                LocaleProductsBase.getInstance().getProductsList().remove(product);
                LocaleProductsBase.getInstance().getProductsList().add(productToUpdate);
            }
        }
        return productToUpdate;
    }

    @Override
    public Product create(Product product) {
        product.setId(getFreeId());
        LocaleProductsBase.getInstance().getProductsList().add(product);
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
    public Product get(long id) {
        Product productGet = null;//todo
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
