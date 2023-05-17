package com.haltwinizki;


import com.haltwinizki.products.Product;
import com.haltwinizki.service.ProductService;
import com.haltwinizki.service.impl.ProductServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Manager {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private final ProductService productService = new ProductServiceImpl();
    private final MarketConsoleRenderer marketConsoleRenderer = new MarketConsoleRenderer(productService);

    public void start() {

        ProductQualityChangeScheduler dailyProductQualityChangeScheduler = new ProductQualityChangeScheduler();
        dailyProductQualityChangeScheduler.schedulerStart(productService);
        while (marketConsoleRenderer.render()) {

        }
        dailyProductQualityChangeScheduler.schedulerStop();

    }


}

