package org.example.local;

import org.example.products.Käse;
import org.example.products.Product;
import org.example.products.Wein;
import org.example.worker.FileWorker;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocaleProductsBase {
    private static LocaleProductsBase instance;
    private final String productFileName = "src/main/resources/products.csv";
    private final String discardedProductFileName = "src/main/resources/discardedProducts.csv";
    private final FileWorker fileWorker = new FileWorker();
    private final List<Product> productsList;
    private final List<Product> discardedProducts;

    public LocaleProductsBase() {
        productsList = fileWorker.readProductsAusCSV(productFileName);
        discardedProducts = fileWorker.readProductsAusCSV(discardedProductFileName);
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
        fileWorker.writeProductsInCSV(productFileName, productsList);
        fileWorker.writeProductsInCSV(discardedProductFileName, discardedProducts);

    }

    public List<Product> getProductsList() {
        return productsList;
    }

}

