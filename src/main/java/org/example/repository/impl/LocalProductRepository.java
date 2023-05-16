package org.example.repository.impl;

import org.example.local.LocaleProductsBase;
import org.example.products.Product;
import org.example.repository.ProductRepository;

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
        //  LocaleProductsBase.getInstance().getProductsList()=
        //          LocaleProductsBase.getInstance().getProductsList().stream().filter(id->!(id==idToDelete));
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
