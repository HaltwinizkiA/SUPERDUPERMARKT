package com.haltwinizki.repository.impl;

import com.haltwinizki.local.LocaleProductsBase;
import com.haltwinizki.products.Product;
import com.haltwinizki.repository.ProductRepository;

import java.util.List;
import java.util.Objects;

public class LocalProductRepository implements ProductRepository {

    LocaleProductsBase localeProductsBase;

    @Override
    public List<Product> getAllProducts() {
        return LocaleProductsBase.getInstance().getProductsList();
    }

    @Override
    public Product delete(long id) {
        Product removedProduct = read(id);
        LocaleProductsBase.getInstance().getProductsList().removeIf(product -> Objects.equals(id, product.getId()));
        LocaleProductsBase.getInstance().getDiscardedProducts().add(removedProduct);
        LocaleProductsBase.getInstance().save();
        return removedProduct;
    }

    @Override
    public Product update(Product updatedProduct) {
        if (updatedProduct == null) {
            return null;
        }
        Product productInDb = LocaleProductsBase.getInstance().getProductsList().stream()
                .filter(product -> product.getId() == updatedProduct.getId()).findFirst().orElse(null);
        if (productInDb == null) {
            return null;
        }
        if (updatedProduct.getQuality() != null && productInDb.getQuality() != updatedProduct.getQuality()) {
            productInDb.getQuality().set(updatedProduct.getQuality().get());
        }
        if (updatedProduct.getExpirationDate() != null && !updatedProduct.getExpirationDate().equals(productInDb.getExpirationDate())) {
            productInDb.setExpirationDate(updatedProduct.getExpirationDate());
        }
        if (updatedProduct.getName() != null && !updatedProduct.getName().equals(productInDb.getName())) {
            productInDb.setName(updatedProduct.getName());
        }
        if (productInDb.getPrice() != updatedProduct.getPrice()) {
            productInDb.setPrice(updatedProduct.getPrice());
        }
        LocaleProductsBase.getInstance().save();
        return productInDb;
    }

    @Override
    public Product create(Product product) {
        product.setId(LocaleProductsBase.getInstance().getMaxId().incrementAndGet());
        LocaleProductsBase.getInstance().getProductsList().add(product);
        LocaleProductsBase.getInstance().save();
        return product;
    }

    @Override
    public Product read(long id) {
        return LocaleProductsBase.getInstance().getProductsList().stream().filter(product -> product.getId() == id).findFirst().map(Product::clone).orElse(null);
    }

    @Override
    public List<Product> getDiscardedProducts() {
        return LocaleProductsBase.getInstance().getDiscardedProducts();
    }
}
