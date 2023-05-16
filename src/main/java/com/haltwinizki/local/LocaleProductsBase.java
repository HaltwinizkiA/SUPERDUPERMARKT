package com.haltwinizki.local;

import com.haltwinizki.products.Product;
import com.haltwinizki.worker.FileWorker;

import java.util.List;

public class LocaleProductsBase {
    private static LocaleProductsBase instance;
    private static final String PRODUCT_FILE_NAME = "src/main/resources/products.csv";
    private static final String DISCARDED_PRODUCT_FILE_NAME = "src/main/resources/discardedProducts.csv";
    private final FileWorker fileWorker = new FileWorker();
    private final List<Product> productsList;
    private final List<Product> discardedProducts;

    public LocaleProductsBase() {
        productsList = fileWorker.readProductsAusCSV(PRODUCT_FILE_NAME);
        discardedProducts = fileWorker.readProductsAusCSV(DISCARDED_PRODUCT_FILE_NAME);
    }

    public static LocaleProductsBase getInstance() {
        if (instance == null) {
            synchronized (LocaleProductsBase.class) {
                if (instance == null) {
                    instance = new LocaleProductsBase();
                }
            }

        }
        return instance;
    }

    public List<Product> getDiscardedProducts() {
        return discardedProducts;
    }

    public void save() {
        fileWorker.writeProductsInCSV(PRODUCT_FILE_NAME, productsList);
        fileWorker.writeProductsInCSV(DISCARDED_PRODUCT_FILE_NAME, discardedProducts);

    }

    public List<Product> getProductsList() {
        return productsList;
    }

}

