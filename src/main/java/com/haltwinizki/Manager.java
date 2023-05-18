package com.haltwinizki;


import com.haltwinizki.scheduler.ProductQualityChangeScheduler;
import com.haltwinizki.service.ProductService;
import com.haltwinizki.service.impl.ProductServiceImpl;

public class Manager {
    private final ProductService productService = new ProductServiceImpl();
    private final MarketConsoleRenderer marketConsoleRenderer = new MarketConsoleRenderer(productService);

    public void start() {
        ProductQualityChangeScheduler dailyProductQualityChangeScheduler = new ProductQualityChangeScheduler();
        dailyProductQualityChangeScheduler.schedulerStart(productService);
        marketConsoleRenderer.render();
        dailyProductQualityChangeScheduler.schedulerStop();
    }
}

