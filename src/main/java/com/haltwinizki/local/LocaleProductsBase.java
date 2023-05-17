package com.haltwinizki.local;

import com.haltwinizki.products.Product;
import com.haltwinizki.worker.FileWorker;
import org.apache.log4j.Logger;


import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class LocaleProductsBase {
    private static final String PRODUCT_FILE_NAME = "src/main/resources/products.csv";
    private static final String DISCARDED_PRODUCT_FILE_NAME = "src/main/resources/discardedProducts.csv";
    private static final Logger log = Logger.getLogger(LocaleProductsBase.class);
    private static LocaleProductsBase instance;
    private final FileWorker fileWorker;
    private List<Product> productsList;
    private List<Product> discardedProducts;

    public AtomicLong maxId;

    public LocaleProductsBase() {
        fileWorker = new FileWorker();
        try {
            productsList = fileWorker.readProductsAusCSV(PRODUCT_FILE_NAME);
            discardedProducts = fileWorker.readProductsAusCSV(DISCARDED_PRODUCT_FILE_NAME);
        } catch (IOException|ParseException e) {
            System.out.println("Datenbank nicht geladen");
            log.info("Datenbank nicht geladen",e);
        }

        checkMaxId();
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

    private void checkMaxId() {
        this.maxId = new AtomicLong();
        productsList.stream().map(Product::getId)
                .max(Long::compareTo)
                .ifPresent(maxId::set);
    }

    public List<Product> getDiscardedProducts() {
        return discardedProducts;
    }

    public void save() {
        try {
            fileWorker.writeProductsInCSV(PRODUCT_FILE_NAME, productsList);

            fileWorker.writeProductsInCSV(DISCARDED_PRODUCT_FILE_NAME, discardedProducts);
        } catch (IOException e) {
            System.out.println("Datenbank nicht gespeichert");
            log.info("Datenbank nicht gespeichert",e);
        }

    }

    public List<Product> getProductsList() {
        return productsList;
    }

}

