package com.haltwinizki.local;

import com.haltwinizki.products.Product;
import com.haltwinizki.worker.FileWorker;
import org.apache.log4j.Logger;


import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class LocaleProductsBase {
    private static final String PRODUCT_FILE_NAME = "src/main/resources/products.csv";
    private static final String DISCARDED_PRODUCT_FILE_NAME = "src/main/resources/discardedProducts.csv";
    private static final Logger log = Logger.getLogger(LocaleProductsBase.class);
    private static LocaleProductsBase instance;
    private final FileWorker fileWorker;

    public AtomicLong getMaxId() {
        return maxId;
    }

    private AtomicLong maxId;
    private CopyOnWriteArrayList<Product> productsList;
    private CopyOnWriteArrayList <Product> discardedProducts;

    public LocaleProductsBase() {
        fileWorker = new FileWorker();
        maxId=new AtomicLong(0);
        try {
            productsList = fileWorker.readProductsAusCSVReflection(PRODUCT_FILE_NAME);
            discardedProducts = fileWorker.readProductsAusCSVReflection(DISCARDED_PRODUCT_FILE_NAME);
        } catch (Exception e) {
            System.out.println("Datenbank nicht geladen");
            log.info("Datenbank nicht geladen", e);
        }
        initMaxId();
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

    private void initMaxId() {
        this.maxId = new AtomicLong();
        productsList.stream().map(Product::getId).max(Long::compareTo).ifPresent(maxId::set);
    }

    public List<Product> getDiscardedProducts() {
        return discardedProducts;
    }

    public void save() {
        try {
            fileWorker.writeProductsInCSVReflection(PRODUCT_FILE_NAME, productsList);
            fileWorker.writeProductsInCSVReflection(DISCARDED_PRODUCT_FILE_NAME, discardedProducts);
        } catch (IllegalAccessException e) {
            System.out.println("Datenbank nicht gespeichert");
            log.info("Datenbank nicht gespeichert", e);
        }
    }

    public List<Product> getProductsList() {
        return productsList;
    }

}

